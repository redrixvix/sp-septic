/**
 * S&P Septic Website Audit Script
 * Uses Playwright to audit the website at https://sp-septic.vercel.app/
 */

const { chromium } = require('playwright');
const path = require('path');
const fs = require('fs');

const BASE_URL = 'https://sp-septic.vercel.app';
const SCREENSHOT_DIR = '/home/rixvix/.openclaw/workspace/sp-septic/.screenshots/audit';

const PAGES = [
  { name: 'homepage', path: '/' },
  { name: 'services', path: '/services' },
  { name: 'pricing', path: '/pricing' },
  { name: 'contact', path: '/contact' },
  { name: 'about', path: '/about' },
];

const VIEWPORTS = [
  { name: 'desktop', width: 1280, height: 900 },
  { name: 'mobile', width: 375, height: 812 },
];

async function runAudit() {
  const results = {
    timestamp: new Date().toISOString(),
    pages: {},
    summary: {
      totalPages: PAGES.length,
      successfulPages: 0,
      failedPages: 0,
      consoleErrors: [],
    },
  };

  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();

  for (const pageInfo of PAGES) {
    const pageUrl = `${BASE_URL}${pageInfo.path}`;
    console.log(`\n=== Auditing: ${pageInfo.name} (${pageUrl}) ===`);

    const pageResult = {
      url: pageUrl,
      httpStatus: null,
      loadSuccess: false,
      consoleErrors: [],
      consoleWarnings: [],
      seo: {
        title: null,
        titlePresent: false,
        metaDescription: null,
        metaDescriptionPresent: false,
        h1: null,
        h1Present: false,
      },
      accessibility: {
        imagesWithoutAlt: [],
        formsWithoutLabels: [],
        accessibilityScore: 0,
      },
      screenshots: {},
    };

    for (const vp of VIEWPORTS) {
      const page = await context.newPage();
      await page.setViewportSize({ width: vp.width, height: vp.height });

      // Capture console messages
      const consoleErrors = [];
      const consoleWarnings = [];
      page.on('console', (msg) => {
        if (msg.type() === 'error') {
          consoleErrors.push(msg.text());
        }
        if (msg.type() === 'warning') {
          consoleWarnings.push(msg.text());
        }
      });

      // Capture page errors
      page.on('pageerror', (err) => {
        consoleErrors.push(`PAGE ERROR: ${err.message}`);
      });

      try {
        const response = await page.goto(pageUrl, {
          waitUntil: 'networkidle',
          timeout: 30000,
        });

        pageResult.httpStatus = response ? response.status() : 'NO_RESPONSE';
        pageResult.loadSuccess = response && response.ok();

        if (pageResult.loadSuccess) {
          results.summary.successfulPages++;
        }

        // Take screenshot
        const screenshotPath = path.join(
          SCREENSHOT_DIR,
          `${pageInfo.name}_${vp.name}.png`
        );
        await page.screenshot({
          path: screenshotPath,
          fullPage: false,
        });
        pageResult.screenshots[vp.name] = screenshotPath;
        console.log(`  [${vp.name}] Screenshot saved: ${screenshotPath}`);

        // Only do detailed checks on desktop viewport to avoid duplication
        if (vp.name === 'desktop') {
          // SEO Checks
          pageResult.seo.title = await page.title();
          pageResult.seo.titlePresent = pageResult.seo.title.length > 0;

          const metaDesc = await page.$('meta[name="description"]');
          if (metaDesc) {
            pageResult.seo.metaDescription = await metaDesc.getAttribute('content');
            pageResult.seo.metaDescriptionPresent = pageResult.seo.metaDescription.length > 0;
          }

          const h1 = await page.$('h1');
          if (h1) {
            pageResult.seo.h1 = await h1.textContent();
            pageResult.seo.h1Present = pageResult.seo.h1.trim().length > 0;
          }

          // Accessibility Checks
          const images = await page.$$('img');
          for (const img of images) {
            const alt = await img.getAttribute('alt');
            if (alt === null || alt === '') {
              const src = await img.getAttribute('src');
              pageResult.accessibility.imagesWithoutAlt.push(src || 'unknown');
            }
          }

          const inputs = await page.$$('input');
          for (const input of inputs) {
            const id = await input.getAttribute('id');
            const ariaLabel = await input.getAttribute('aria-label');
            const ariaLabelledby = await input.getAttribute('aria-labelledby');
            const placeholder = await input.getAttribute('placeholder');
            const type = await input.getAttribute('type');

            // Skip hidden inputs
            if (type === 'hidden') continue;

            const hasLabel = !!(id || ariaLabel || ariaLabelledby || placeholder);
            if (!hasLabel) {
              const name = await input.getAttribute('name');
              pageResult.accessibility.formsWithoutLabels.push(name || `input-${Math.random().toString(36).substr(2, 5)}`);
            }
          }

          const textareas = await page.$$('textarea');
          for (const textarea of textareas) {
            const id = await textarea.getAttribute('id');
            const ariaLabel = await textarea.getAttribute('aria-label');
            const ariaLabelledby = await textarea.getAttribute('aria-labelledby');
            if (!id && !ariaLabel && !ariaLabelledby) {
              pageResult.accessibility.formsWithoutLabels.push('textarea');
            }
          }

          const selects = await page.$$('select');
          for (const select of selects) {
            const id = await select.getAttribute('id');
            const ariaLabel = await select.getAttribute('aria-label');
            const ariaLabelledby = await select.getAttribute('aria-labelledby');
            if (!id && !ariaLabel && !ariaLabelledby) {
              pageResult.accessibility.formsWithoutLabels.push('select');
            }
          }

          // Calculate accessibility score
          const totalImages = images.length;
          const imagesWithAlt = totalImages - pageResult.accessibility.imagesWithoutAlt.length;
          const imageScore = totalImages > 0 ? (imagesWithAlt / totalImages) * 50 : 50;
          const formScore = pageResult.accessibility.formsWithoutLabels.length === 0 ? 50 : 25;
          pageResult.accessibility.accessibilityScore = Math.round(imageScore + formScore);
        }

        // Collect console errors/warnings
        pageResult.consoleErrors = [...new Set(consoleErrors)];
        pageResult.consoleWarnings = [...new Set(consoleWarnings)];

        if (pageResult.consoleErrors.length > 0) {
          results.summary.consoleErrors.push({
            page: pageInfo.name,
            errors: pageResult.consoleErrors,
          });
        }

        // Log results
        console.log(`  HTTP Status: ${pageResult.httpStatus}`);
        console.log(`  Load Success: ${pageResult.loadSuccess}`);
        console.log(`  Title: "${pageResult.seo.title}" (present: ${pageResult.seo.titlePresent})`);
        console.log(`  Meta Desc: ${pageResult.seo.metaDescriptionPresent}`);
        console.log(`  H1: "${pageResult.seo.h1}" (present: ${pageResult.seo.h1Present})`);
        console.log(`  Console Errors: ${pageResult.consoleErrors.length}`);
        console.log(`  Images w/o alt: ${pageResult.accessibility.imagesWithoutAlt.length}`);
        console.log(`  Forms w/o labels: ${pageResult.accessibility.formsWithoutLabels.length}`);
        console.log(`  A11y Score: ${pageResult.accessibility.accessibilityScore}/100`);

      } catch (err) {
        pageResult.consoleErrors.push(`NAVIGATION ERROR: ${err.message}`);
        pageResult.loadSuccess = false;
        results.summary.failedPages++;
        console.log(`  ERROR: ${err.message}`);
      }

      await page.close();
    }

    results.pages[pageInfo.name] = pageResult;
  }

  await browser.close();

  // Write results JSON
  const resultsPath = path.join(SCREENSHOT_DIR, 'audit-results.json');
  fs.writeFileSync(resultsPath, JSON.stringify(results, null, 2));
  console.log(`\n=== Results saved to ${resultsPath} ===`);

  return results;
}

runAudit().catch((err) => {
  console.error('Audit failed:', err);
  process.exit(1);
});

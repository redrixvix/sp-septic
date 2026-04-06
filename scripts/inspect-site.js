const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch({ headless: true });
  const page = await browser.newPage();
  page.setViewportSize({ width: 1280, height: 900 });

  console.log('Opening https://sp-septic.vercel.app/ ...');
  await page.goto('https://sp-septic.vercel.app/', { waitUntil: 'networkidle', timeout: 30000 });

  // Take hero section screenshot
  await page.screenshot({ path: '/home/rixvix/.openclaw/workspace/sp-septic/.screenshots/homepage-hero.png', fullPage: false });
  console.log('Hero screenshot saved');

  // Scroll down and take full page
  await page.evaluate(() => window.scrollTo(0, 0));
  await page.waitForTimeout(1000);
  await page.screenshot({ path: '/home/rixvix/.openclaw/workspace/sp-septic/.screenshots/home-top.png', fullPage: true });
  console.log('Full page screenshot saved');

  // Scroll to middle
  await page.evaluate(() => window.scrollTo(0, 1500));
  await page.waitForTimeout(1500);
  await page.screenshot({ path: '/home/rixvix/.openclaw/workspace/sp-septic/.screenshots/home-mid.png', fullPage: true });
  console.log('Mid page screenshot saved');

  // Scroll to bottom
  await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
  await page.waitForTimeout(1500);
  await page.screenshot({ path: '/home/rixvix/.openclaw/workspace/sp-septic/.screenshots/home-bottom.png', fullPage: true });
  console.log('Bottom page screenshot saved');

  // Check for console errors
  const errors = [];
  page.on('console', msg => {
    if (msg.type() === 'error') errors.push(msg.text());
  });

  // Check the services page
  await page.goto('https://sp-septic.vercel.app/services', { waitUntil: 'networkidle', timeout: 20000 });
  await page.screenshot({ path: '/home/rixvix/.openclaw/workspace/sp-septic/.screenshots/services.png', fullPage: true });
  console.log('Services page screenshot saved');

  // Check pricing
  await page.goto('https://sp-septic.vercel.app/pricing', { waitUntil: 'networkidle', timeout: 20000 });
  await page.screenshot({ path: '/home/rixvix/.openclaw/workspace/sp-septic/.screenshots/pricing.png', fullPage: true });
  console.log('Pricing page screenshot saved');

  // Check contact
  await page.goto('https://sp-septic.vercel.app/contact', { waitUntil: 'networkidle', timeout: 20000 });
  await page.screenshot({ path: '/home/rixvix/.openclaw/workspace/sp-septic/.screenshots/contact.png', fullPage: true });
  console.log('Contact page screenshot saved');

  // Check mobile view
  await page.setViewportSize({ width: 375, height: 812 });
  await page.goto('https://sp-septic.vercel.app/', { waitUntil: 'networkidle', timeout: 20000 });
  await page.screenshot({ path: '/home/rixvix/.openclaw/workspace/sp-septic/.screenshots/mobile-home.png', fullPage: true });
  console.log('Mobile screenshot saved');

  console.log('\n=== CONSOLE ERRORS ===');
  if (errors.length === 0) {
    console.log('No console errors found!');
  } else {
    errors.forEach(e => console.log('ERROR:', e));
  }

  await browser.close();
  console.log('\nAll screenshots saved to .screenshots/');
})();
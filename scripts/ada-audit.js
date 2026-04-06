const { chromium } = require('playwright');

const pages = [
  { name: 'Homepage', url: 'https://sp-septic.vercel.app/' },
  { name: 'Services', url: 'https://sp-septic.vercel.app/services' },
  { name: 'Contact', url: 'https://sp-septic.vercel.app/contact' },
  { name: 'Pricing', url: 'https://sp-septic.vercel.app/pricing' },
  { name: 'About', url: 'https://sp-septic.vercel.app/about' },
];

async function runPa11y(url) {
  const { pa11y } = require('pa11y');
  try {
    const result = await pa11y(url, {
      browser: {
        launcher: require('playwright').chromium.executablePath(),
      },
      standard: 'WCAG2AA',
    });
    return result;
  } catch (e) {
    return { error: e.message };
  }
}

(async () => {
  for (const p of pages) {
    console.log(`\n=== ${p.name}: ${p.url} ===`);
    try {
      const result = await runPa11y(p.url);
      if (result.error) {
        console.log('ERROR:', result.error);
      } else {
        console.log(`Issues: ${result.issues.length}`);
        result.issues.forEach(i => {
          console.log(`  [${i.type}] ${i.code} - ${i.message} (${i.selector})`);
        });
      }
    } catch (e) {
      console.log('FAILED:', e.message);
    }
  }
})();
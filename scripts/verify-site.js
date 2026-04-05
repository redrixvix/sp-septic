const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch({ headless: true });
  const page = await browser.newPage();
  
  console.log('Opening http://localhost:3000 ...');
  await page.goto('http://localhost:3000', { waitUntil: 'networkidle' });
  
  const title = await page.title();
  console.log('Page title:', title);
  
  // Check key elements
  const hero = await page.$('h1');
  const heroText = hero ? await hero.textContent() : 'NOT FOUND';
  console.log('Hero headline:', heroText);
  
  // Check if animations are present (look for framer-motion classes)
  const bodyClass = await page.evaluate(() => document.body.className);
  console.log('Body classes:', bodyClass);
  
  // Check for primary color in styles
  const bgColor = await page.evaluate(() => {
    const el = document.querySelector('section');
    return el ? window.getComputedStyle(el).backgroundColor : 'N/A';
  });
  console.log('First section bg:', bgColor);
  
  // Screenshot
  await page.screenshot({ path: '/home/rixvix/.openclaw/workspace/sp-septic/.site-preview.png', fullPage: true });
  console.log('Screenshot saved to .site-preview.png');
  
  await browser.close();
  console.log('Done!');
})();
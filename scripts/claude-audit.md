Audit the S&P Septic website at https://sp-septic.vercel.app/

Steps:
1. Install playwright if not available: npm install -g playwright && npx playwright install chromium
2. Write a Node.js script to the workspace that:
   - Uses Playwright to open the homepage, services, pricing, contact, and about pages
   - Takes screenshots of each page at desktop (1280x900) and mobile (375x812)
   - Checks for console errors (page.on('console', error))
   - Checks if pages load without crash (HTTP 200)
   - Evaluates basic SEO: title tag present, meta description, h1 exists
   - Checks accessibility basics: alt text on images, form labels
3. Run the script and save screenshots to /home/rixvix/.openclaw/workspace/sp-septic/.screenshots/audit/
4. Report back with:
   - What pages load successfully
   - Any console errors found
   - SEO status per page
   - Accessibility status
   - Overall design/UX assessment from the screenshots
   - Top 5 specific issues that need fixing
   - Top 5 things that are done well

Be thorough and honest. Use --print to output findings.
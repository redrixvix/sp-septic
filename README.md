# S&P Septic & Excavating — Website Project

## Overview

A professional, conversion-focused website for **S&P Septic and Excavating Inc.**, a family-owned, BBB-Accredited septic and excavation contractor in Warren, Ohio.

**Live at:** `http://localhost:3131`

## Company Info

- **Business:** S&P Septic and Excavating Inc.
- **Phone:** (330) 979-3930
- **Address:** 2900 Elm Rd NE, Warren, OH 44483-2623
- **BBB:** Accredited since 8/18/2025 — A+ Rating
- **Founded:** 2021
- **Facebook:** https://www.facebook.com/profile.php?id=61577002508149
- **Service Area:** Trumbull County, Mahoning County, Portage County, OH

## Pages

- `/` — Homepage: hero, how-it-works strip, trust stats, services grid, why-choose-us + CTA, gallery teaser, testimonials, service areas, final CTA
- `/services` — Full service listings with process steps, transparency features, FAQ accordion
- `/about` — Company story, values, milestone timeline, credentials
- `/gallery` — Project gallery (9 placeholder projects ready for real photos)
- `/contact` — Contact form (server action ready), info sidebar, emergency CTA, Google Maps embed
- `/not-found` — Custom 404 page

## Tech Stack

- Next.js 16 (App Router) + TypeScript
- Tailwind CSS
- shadcn/ui (Button, Card, Badge, Input, Textarea, Accordion, Separator)
- Lucide React icons
- Next.js Server Actions (contact form)

## SEO

- `sitemap.xml` at `/sitemap.xml`
- `robots.txt` at `/robots.txt`
- JSON-LD structured data (HomeAndConstructionBusiness schema)
- Open Graph + Twitter card metadata
- Per-page metadata (title, description, keywords)
- SVG favicon at `/favicon.svg`

## Contact Form

Currently logs to console. To enable real email delivery, add an email provider:

```ts
// src/app/actions.ts
// TODO: Add email integration (Resend, SendGrid, etc.)
```

## What Still Needs Doing

- [ ] **Real photos** — Gallery placeholders ready; need crew/truck/project photos
- [ ] **Email integration** — Form backend (see actions.ts TODO)
- [ ] **Ohio contractor license #** — Replace "OH Lic #TODO" in footer
- [ ] **Domain + hosting** — Deploy to Vercel, Netlify, or similar
- [ ] **Google Search Console** — Verify ownership, submit sitemap
- [ ] **Google Analytics / Plausible** — Add tracking
- [ ] **Google Business Profile** — Ensure the listing is claimed and linked
- [ ] **Video content** — Explainer video of services would boost conversions
- [ ] **Blog / Education section** — SEO-focused articles about septic maintenance
- [ ] **Yelp / Houzz / HomeAdvisor profile** — Add review widgets to homepage

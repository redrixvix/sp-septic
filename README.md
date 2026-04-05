# S&P Septic & Excavating — Website

**Location:** `/home/rixvix/.openclaw/workspace/sp-septic`
**Dev server:** `npm run dev` → `http://localhost:3131`
**Build:** `npm run build` → 19 pages, all clean

## Pages

| Route | Title |
|---|---|
| `/` | Homepage — hero, how-it-works, services, why-choose, gallery, testimonials, areas, CTA |
| `/services` | Full service listings, process steps, transparency features, FAQ |
| `/pricing` | Pricing guide — 8 septic services + 5 excavation services with cost ranges |
| `/gallery` | Project gallery — 9 entries (photo placeholders) |
| `/blog` | Blog listing — featured + all articles |
| `/blog/[slug]` | 6 full SEO articles |
| `/service-areas` | Dedicated local SEO page for all 9 service areas |
| `/about` | Company story, credentials, values, milestones |
| `/contact` | Contact form (server action), sidebar, Google Maps |
| `/schedule` | Dedicated service scheduling form |
| `/financing` | Payment options, cost education, FAQ |
| `/not-found` | Custom 404 |

## Tech Stack

- Next.js 16 (App Router) + TypeScript
- Tailwind CSS
- shadcn/ui (Button, Card, Badge, Input, Textarea, Accordion)
- Lucide React icons
- Next.js Server Actions (contact form)
- @base-ui/react (underlying component library)

## SEO

- `sitemap.xml` — 16 URLs
- `robots.txt`
- JSON-LD (HomeAndConstructionBusiness schema)
- FAQ JSON-LD (Google rich results)
- Open Graph + Twitter Card metadata
- Per-page `<title>` and `<meta description>`
- SVG favicon + BBB SVG badge

## Navigation

Full header nav + footer nav with all pages.

## Components

- `SiteHeader` — sticky nav, mobile hamburger
- `SiteFooter` — full footer with links, contact info, service areas
- `FloatingCTA` — fixed mobile call-to-action bar
- `EmergencyBanner` — red 24/7 emergency service banner at top of every page
- `JsonLd` — structured data for all pages
- `FaqJsonLd` — FAQ schema

---

## What Still Needs Doing

- [ ] **Real photos** — Gallery, homepage hero, crew photos (biggest gap)
- [ ] **Email integration** — Contact/schedule forms log to console; add Resend/SendGrid
- [ ] **Ohio contractor license #** — Replace "OH Lic #TODO" in footer
- [ ] **Domain + hosting** — Deploy to Vercel/Netlify
- [ ] **Google Search Console** — Verify ownership, submit sitemap
- [ ] **Google Analytics / Plausible** — Add privacy-friendly analytics
- [ ] **Google Business Profile** — Claim and link to the listing
- [ ] **Open Graph images** — Social share images (1200×630)
- [ ] **Real testimonials** — Replace 3 placeholder reviews with actual BBB/Google reviews
- [ ] **BBB badge** — Replace "A+" text with official SVG badge in About + Contact sidebar

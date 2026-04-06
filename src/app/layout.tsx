import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { SiteHeader } from "@/components/site-header";
import { SiteFooter } from "@/components/site-footer";
import { FloatingCTA } from "@/components/floating-cta";
import { EmergencyBanner } from "@/components/emergency-banner";
import { JsonLd } from "@/components/json-ld";
import { FaqJsonLd } from "@/components/faq-jsonld";
import { LocalBusinessJsonLd } from "@/components/local-business-jsonld";
import { OrganizationJsonLd } from "@/components/organization-jsonld";
import { COMPANY } from "@/lib/data";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: {
    default: `${COMPANY.name} | Septic & Excavating Services | Warren, Ohio`,
    template: `%s | ${COMPANY.name}`,
  },
  description: COMPANY.description,
  keywords: [
    "septic installation Warren Ohio",
    "septic repair Trumbull County",
    "excavation services Ohio",
    "septic pumping Warren",
    "drain field repair",
    "septic inspection Ohio",
    "excavation contractor Warren Ohio",
    "perk testing Ohio",
    "septic tank installation",
    "septic system maintenance",
    "excavation contractor Trumbull County",
    "septic emergency service Ohio",
    "leach field installation",
    "septic camera inspection",
    "septic system cleaning",
  ],
  openGraph: {
    title: COMPANY.name,
    description: COMPANY.description,
    siteName: COMPANY.name,
    locale: "en_US",
    type: "website",
    url: "https://spseptic.com",
    images: [
      {
        url: "/opengraph-image",
        width: 1200,
        height: 630,
        alt: `${COMPANY.name} — Septic & Excavating in Warren, Ohio`,
      },
    ],
  },
  twitter: {
    card: "summary_large_image",
    title: COMPANY.name,
    description: COMPANY.description,
    images: ["/opengraph-image"],
  },
  icons: {
    icon: "/favicon.svg",
  },
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      "max-video-preview": -1,
      "max-image-preview": "large",
      "max-snippet": -1,
    },
  },
  metadataBase: new URL("https://spseptic.com"),
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <head>
        {/* Plausible Analytics — privacy-friendly, no cookies */}
        {/* Replace YOUR_SITE_ID with your actual Plausible site domain (e.g., spseptic.com) */}
        {/* Visit https://plausible.io to create an account and get your site ID */}
        {/*
        <script
          defer
          data-domain="YOUR_SITE_ID"
          src="https://plausible.io/js/script.tagged-events.js"
        />
        */}
      </head>
      <body className={`${inter.className} antialiased`}>
        <a href="#main-content" className="skip-link">Skip to main content</a>
        <JsonLd />
        <LocalBusinessJsonLd />
        <OrganizationJsonLd />
        <FaqJsonLd />
        <EmergencyBanner />
        <SiteHeader />
        <main id="main-content">{children}</main>
        <SiteFooter />
        <FloatingCTA />
      </body>
    </html>
  );
}

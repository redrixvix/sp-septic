import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { SiteHeader } from "@/components/site-header";
import { SiteFooter } from "@/components/site-footer";
import { FloatingCTA } from "@/components/floating-cta";
import { JsonLd } from "@/components/json-ld";
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
  ],
  openGraph: {
    title: COMPANY.name,
    description: COMPANY.description,
    siteName: COMPANY.name,
    locale: "en_US",
    type: "website",
    url: "https://spseptic.com",
  },
  twitter: {
    card: "summary_large_image",
    title: COMPANY.name,
    description: COMPANY.description,
  },
  icons: {
    icon: "/favicon.svg",
  },
  robots: {
    index: true,
    follow: true,
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${inter.className} antialiased`}>
        <JsonLd />
        <SiteHeader />
        <main>{children}</main>
        <SiteFooter />
        <FloatingCTA />
      </body>
    </html>
  );
}

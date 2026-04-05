"use client";

import Link from "next/link";
import { useState } from "react";
import { Menu, X, Phone } from "lucide-react";
import { Button } from "@/components/ui/button";
import { COMPANY } from "@/lib/data";

const NAV_LINKS = [
  { href: "/", label: "Home" },
  { href: "/services", label: "Services" },
  { href: "/pricing", label: "Pricing" },
  { href: "/gallery", label: "Gallery" },
  { href: "/blog", label: "Blog" },
  { href: "/service-areas", label: "Areas" },
  { href: "/about", label: "About" },
  { href: "/contact", label: "Contact" },
];

export function SiteHeader() {
  const [mobileOpen, setMobileOpen] = useState(false);

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-white/95 backdrop-blur supports-[backdrop-filter]:bg-white/80">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex h-16 items-center justify-between">
          {/* Logo */}
          <Link href="/" className="flex items-center gap-2">
            <div className="w-9 h-9 rounded-lg bg-primary flex items-center justify-center">
              <span className="text-white font-bold text-sm">S&P</span>
            </div>
            <div className="hidden sm:block">
              <span className="font-bold text-base text-slate-900 leading-tight block">
                S&P Septic & Excavating
              </span>
              <span className="text-xs text-slate-500 leading-tight">
                Warren, Ohio
              </span>
            </div>
          </Link>

          {/* Desktop Nav */}
          <nav className="hidden md:flex items-center gap-1">
            {NAV_LINKS.map((link) => (
              <Link
                key={link.href}
                href={link.href}
                className="px-4 py-2 text-sm font-medium text-slate-600 hover:text-primary transition-colors rounded-md hover:bg-slate-100"
              >
                {link.label}
              </Link>
            ))}
          </nav>

          {/* CTA */}
          <div className="hidden md:flex items-center gap-3">
            <a href={`tel:${COMPANY.phone}`} className="flex items-center gap-2 text-sm font-semibold text-primary">
              <Phone className="w-4 h-4" />
              {COMPANY.phone}
            </a>
            <Link href="/contact"><Button size="sm">Get Free Estimate</Button></Link>
          </div>

          {/* Mobile Menu Button */}
          <button
            className="md:hidden p-2 text-slate-600"
            onClick={() => setMobileOpen(!mobileOpen)}
            aria-label="Toggle menu"
          >
            {mobileOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
          </button>
        </div>

        {/* Mobile Nav */}
        {mobileOpen && (
          <div className="md:hidden border-t py-4 flex flex-col gap-2">
            {NAV_LINKS.map((link) => (
              <Link
                key={link.href}
                href={link.href}
                onClick={() => setMobileOpen(false)}
                className="px-4 py-2 text-sm font-medium text-slate-700 hover:text-primary hover:bg-slate-50 rounded-md"
              >
                {link.label}
              </Link>
            ))}
            <div className="pt-2 border-t mt-2">
              <a
                href={`tel:${COMPANY.phone}`}
                className="flex items-center gap-2 px-4 py-2 text-sm font-semibold text-primary"
              >
                <Phone className="w-4 h-4" />
                {COMPANY.phone}
              </a>
              <div className="px-4 pt-2">
                <Link
                  href="/contact"
                  onClick={() => setMobileOpen(false)}
                  className="block w-full text-center"
                >
                  <Button className="w-full" size="sm">Get Free Estimate</Button>
                </Link>
              </div>
            </div>
          </div>
        )}
      </div>
    </header>
  );
}

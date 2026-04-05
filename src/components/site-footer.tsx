import Link from "next/link";
import { Phone, MapPin, Mail } from "lucide-react";
import { COMPANY, SERVICES, SERVICE_AREAS } from "@/lib/data";

export function SiteFooter() {
  return (
    <footer className="bg-slate-900 text-slate-300">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          {/* Company Info */}
          <div className="lg:col-span-1">
            <div className="flex items-center gap-2 mb-4">
              <div className="w-9 h-9 rounded-lg bg-primary flex items-center justify-center">
                <span className="text-white font-bold text-sm">S&P</span>
              </div>
              <span className="font-bold text-white text-base">
                {COMPANY.name}
              </span>
            </div>
            <p className="text-sm text-slate-400 leading-relaxed">
              Family-owned septic and excavation contractor serving Warren, Ohio and surrounding Trumbull County since 2021.
            </p>
            <div className="mt-4 flex items-center gap-2 text-sm">
              <MapPin className="w-4 h-4 text-primary" />
              <span>{COMPANY.city}, {COMPANY.state}</span>
            </div>
            <div className="mt-2 flex items-center gap-2 text-sm">
              <Phone className="w-4 h-4 text-primary" />
              <a href={`tel:${COMPANY.phone}`} className="hover:text-white transition-colors">
                {COMPANY.phone}
              </a>
            </div>
            <div className="mt-4 flex gap-3">
              <a
                href={COMPANY.facebook}
                target="_blank"
                rel="noopener noreferrer"
                className="w-9 h-9 rounded-full bg-slate-800 flex items-center justify-center hover:bg-primary transition-colors"
                aria-label="Facebook"
              >
                <svg className="w-4 h-4" viewBox="0 0 24 24" fill="currentColor"><path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/></svg>
              </a>
            </div>
          </div>

          {/* Services */}
          <div>
            <h3 className="font-semibold text-white text-sm uppercase tracking-wider mb-4">
              Services
            </h3>
            <ul className="space-y-2">
              {SERVICES.slice(0, 5).map((service) => (
                <li key={service.title}>
                  <Link
                    href="/services"
                    className="text-sm text-slate-400 hover:text-white transition-colors"
                  >
                    {service.title}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          {/* Quick Links */}
          <div>
            <h3 className="font-semibold text-white text-sm uppercase tracking-wider mb-4">
              Quick Links
            </h3>
            <ul className="space-y-2">
              {[
                { href: "/", label: "Home" },
                { href: "/services", label: "Services" },
                { href: "/pricing", label: "Pricing Guide" },
                { href: "/gallery", label: "Gallery" },
                { href: "/blog", label: "Blog" },
                { href: "/service-areas", label: "Service Areas" },
                { href: "/about", label: "About Us" },
                { href: "/contact", label: "Contact" },
                { href: COMPANY.bbbUrl, label: "BBB Profile" },
              ].map((link) => (
                <li key={link.href}>
                  <Link
                    href={link.href}
                    className="text-sm text-slate-400 hover:text-white transition-colors"
                  >
                    {link.label}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          {/* Service Area */}
          <div>
            <h3 className="font-semibold text-white text-sm uppercase tracking-wider mb-4">
              Service Areas
            </h3>
            <ul className="space-y-1.5">
              {SERVICE_AREAS.slice(0, 6).map((area) => (
                <li key={area} className="text-sm text-slate-400 flex items-center gap-1.5">
                  <MapPin className="w-3 h-3 text-primary flex-shrink-0" />
                  {area}
                </li>
              ))}
            </ul>
          </div>
        </div>

        <div className="border-t border-slate-800 mt-8 pt-8 flex flex-col sm:flex-row justify-between items-center gap-4">
          <p className="text-xs text-slate-500">
            © {new Date().getFullYear()} {COMPANY.fullName}. All rights reserved.
          </p>
          <div className="flex items-center gap-1.5">
            <span className="text-xs text-slate-500">BBB Accredited</span>
            <span className="text-xs text-slate-600">•</span>
            <span className="text-xs text-slate-500">Licensed & Insured</span>
            <span className="text-xs text-slate-600">•</span>
            <span className="text-xs text-slate-500">OH Lic #{"TODO"}</span>
          </div>
        </div>
      </div>
    </footer>
  );
}

import type { Metadata } from "next";
import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { MapPin, Phone, CheckCircle, ArrowRight } from "lucide-react";
import { COMPANY, SERVICE_AREAS } from "@/lib/data";

export const metadata: Metadata = {
  title: "Service Areas — S&P Septic & Excavating | Warren, Ohio",
  description:
    "S&P Septic and Excavating serves Warren, Niles, Cortland, Girard, Trumbull County, Mahoning County, and Portage County, Ohio. Septic and excavation services near you.",
};

const AREA_DETAILS: Record<string, { zip?: string; desc: string }> = {
  "Warren, OH": {
    desc: "Our home base and primary service area. We know Warren's neighborhoods, soil conditions, and local health department requirements.",
  },
  "Niles, OH": {
    desc: "Regular service area for septic installations, repairs, and excavation. Familiar with Niles city and township requirements.",
  },
  "Cortland, OH": {
    desc: "Residential and commercial septic services for Cortland and the surrounding area.",
  },
  "Girard, OH": {
    desc: "Septic pumping, repair, and new installations for Girard homeowners and businesses.",
  },
  "Liberty Township, OH": {
    desc: "Liberty Township residents trust S&P for all septic and excavation needs.",
  },
  "Howland Township, OH": {
    desc: "Serving Howland with new septic systems, drain field work, and emergency repairs.",
  },
  "Trumbull County, OH": {
    desc: "Full county coverage for all septic and excavation services.",
  },
  "Mahoning County, OH": {
    desc: "Expanded service area for septic installations and excavation in Mahoning County.",
  },
  "Portage County, OH": {
    desc: "Serving Portage County with perk testing, septic installations, and excavation.",
  },
};

export default function ServiceAreasPage() {
  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-emerald-600 text-white border-0">Service Area</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Serving Northeast Ohio
          </h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            S&P Septic and Excavating is your local contractor for Warren, Ohio and all of Trumbull County — plus Mahoning and Portage Counties.
          </p>
        </div>
      </section>

      {/* Stats Strip */}
      <section className="bg-emerald-600 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6 text-center text-white">
            <div>
              <div className="text-2xl font-extrabold">3</div>
              <div className="text-xs text-emerald-200">Counties Served</div>
            </div>
            <div>
              <div className="text-2xl font-extrabold">9+</div>
              <div className="text-xs text-emerald-200">Communities</div>
            </div>
            <div>
              <div className="text-2xl font-extrabold">2021</div>
              <div className="text-xs text-emerald-200">Serving Ohio Since</div>
            </div>
            <div>
              <div className="text-2xl font-extrabold">24hr</div>
              <div className="text-xs text-emerald-200">Emergency Response</div>
            </div>
          </div>
        </div>
      </section>

      {/* Areas */}
      <section className="py-16 md:py-24 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-slate-900 mb-3">Areas We Serve</h2>
            <p className="text-slate-600 max-w-2xl mx-auto">
              When you need septic or excavation service, you want someone who knows the local area — the soil, the codes, the community. That&apos;s S&P.
            </p>
          </div>
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {SERVICE_AREAS.map((area) => (
              <Card key={area} className="bg-slate-50 border-0 hover:shadow-md transition-shadow">
                <CardContent className="p-5">
                  <div className="flex items-start gap-3">
                    <div className="w-9 h-9 rounded-lg bg-emerald-100 flex items-center justify-center text-emerald-600 flex-shrink-0">
                      <MapPin className="w-5 h-5" />
                    </div>
                    <div>
                      <h3 className="font-bold text-slate-900 mb-1">{area}</h3>
                      <p className="text-sm text-slate-600 leading-relaxed">
                        {AREA_DETAILS[area]?.desc ?? "Septic and excavation services for this community."}
                      </p>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* Why Local Matters */}
      <section className="py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div>
              <Badge className="mb-3 bg-emerald-100 text-emerald-700 border-0 text-xs">Why Local Matters</Badge>
              <h2 className="text-3xl font-bold text-slate-900 mb-4">
                We Know Northeast Ohio
              </h2>
              <p className="text-slate-600 text-lg leading-relaxed mb-6">
                Northeast Ohio has its own quirks — from clay-heavy soils in some areas to rolling hills in others. We&apos;ve worked in this region for years. We know the local health department requirements, soil conditions, and what works here.
              </p>
              <div className="space-y-3">
                {[
                  "Familiar with Trumbull, Mahoning & Portage County health dept. requirements",
                  "Know which soil types need special drain field designs",
                  "Fast response because we're already in your neighborhood",
                  "Established relationships with local inspectors",
                ].map((item) => (
                  <div key={item} className="flex items-start gap-3">
                    <CheckCircle className="w-5 h-5 text-emerald-600 flex-shrink-0 mt-0.5" />
                    <span className="text-sm text-slate-600">{item}</span>
                  </div>
                ))}
              </div>
            </div>
            <div className="bg-slate-100 rounded-2xl aspect-video flex items-center justify-center">
              <div className="text-center text-slate-400">
                <MapPin className="w-12 h-12 mx-auto mb-2 opacity-40" />
                <p className="text-sm opacity-60">Trumbull County, Ohio</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Not in the area? */}
      <section className="py-14 bg-slate-900 text-white">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-2xl font-bold mb-3">Don&apos;t See Your Area?</h2>
          <p className="text-slate-300 mb-6">
            We serve the greater Northeast Ohio area. If you&apos;re just outside our listed service area, give us a call anyway — we may still be able to help.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <a href="tel:3309793930">
              <div className="inline-flex items-center gap-2 bg-emerald-600 hover:bg-emerald-700 text-white font-bold px-8 py-3 rounded-lg transition-colors">
                <Phone className="w-5 h-5" />
                Call (330) 979-3930
              </div>
            </a>
            <Link href="/contact">
              <div className="inline-flex items-center gap-2 border border-slate-600 hover:bg-slate-800 text-white font-semibold px-8 py-3 rounded-lg transition-colors">
                Request Estimate <ArrowRight className="w-4 h-4" />
              </div>
            </Link>
          </div>
        </div>
      </section>
    </>
  );
}

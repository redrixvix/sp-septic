import type { Metadata } from "next";
import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Phone, CheckCircle, ArrowRight, Info } from "lucide-react";
import { COMPANY } from "@/lib/data";
import { BreadcrumbJsonLd } from "@/components/breadcrumb-jsonld";

export const metadata: Metadata = {
  title: "Pricing Guide",
  description:
    "Honest, upfront septic and excavation pricing for Warren, Ohio homeowners. Learn what septic pumping, new installations, and excavation services cost in 2026.",
};

const SERVICES_PRICING = [
  {
    service: "Septic Tank Pumping",
    range: "$300 – $600",
    factors: "Tank size (1,000–1,500 gal), accessibility, sludge level",
    notes: "Most common residential service. Price varies by tank size and how full the tank is.",
  },
  {
    service: "Septic System Inspection",
    range: "$150 – $350",
    factors: "Tank size, camera inspection, written report",
    notes: "We recommend annual inspection. Includes tank check, water level, baffle condition, and camera of drain lines.",
  },
  {
    service: "Septic System Installation (Conventional)",
    range: "$8,000 – $18,000",
    factors: "Soil conditions, tank size, drain field size, permitting, site prep",
    notes: "Most common for rural properties with good soil. Includes tank, drain field, permit handling, and inspection.",
  },
  {
    service: "Septic System Installation (Aerobic)",
    range: "$12,000 – $25,000+",
    factors: "Tank size, aeration unit, pre-treatment, drain field, permitting",
    notes: "Required where soil percolation is poor. Higher upfront cost, but better treatment for challenging sites.",
  },
  {
    service: "Drain Field Replacement",
    range: "$5,000 – $15,000",
    factors: "Soil type, field size, excavation depth, permits",
    notes: "Full replacement when the leach field has failed. Partial repairs are sometimes possible at lower cost.",
  },
  {
    service: "Septic Tank Repair",
    range: "$400 – $2,500",
    factors: "Type of repair (baffle, inlet/outlet, crack, lid)",
    notes: "Minor repairs are affordable. Structural tank replacement is significantly more.",
  },
  {
    service: "Camera Inspection",
    range: "$175 – $400",
    factors: "Line length, complexity",
    notes: "Essential for diagnosing persistent drain problems. We show you the video.",
  },
  {
    service: "Septic Pumping + Inspection Package",
    range: "$375 – $650",
    factors: "Tank size, inspection depth",
    notes: "Best value for maintenance. Pump the tank AND check its condition at the same time.",
  },
];

const EXCAVATION_PRICING = [
  {
    service: "Driveway Excavation (full build)",
    range: "$8 – $20 / sq ft",
    factors: "Depth, sub-base material, site conditions, length",
    notes: "Proper driveway excavation with correct depth and compaction. Includes grading.",
  },
  {
    service: "Trenching (utility lines)",
    range: "$6 – $15 / linear ft",
    factors: "Depth, soil type, rock, width",
    notes: "Water lines, sewer hookups, gas lines. Price depends heavily on ground conditions.",
  },
  {
    service: "Site Clearing",
    range: "$1,500 – $5,000+",
    factors: "Acreage, vegetation density, stump removal",
    notes: "Clearing land for new construction. Price varies significantly by site.",
  },
  {
    service: "Grading",
    range: "$1 – $3 / sq ft",
    factors: "Area size, amount of earth to move",
    notes: "Proper grading for drainage. Essential before any paving or building.",
  },
  {
    service: "Pond Excavation",
    range: "$3,000 – $15,000+",
    factors: "Size, depth, soil disposal, complexity",
    notes: "Custom quote required. Depends entirely on project specifications.",
  },
];

export default function PricingPage() {
  return (
    <>
      <BreadcrumbJsonLd items={[{ name: "Home", url: "https://spseptic.com" }, { name: "Pricing", url: "https://spseptic.com/pricing" }]} />
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-amber-500 text-white border-0">2026 Pricing Guide</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Honest Pricing for Septic &amp; Excavation
          </h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            No hidden fees. No surprises. Here&apos;s what typical septic and excavation services cost in the Warren, Ohio area — so you know what to expect before we show up.
          </p>
        </div>
      </section>

      {/* Disclaimer */}
      <section className="bg-amber-50 border-b border-amber-100 py-5">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-start gap-3 text-amber-800">
            <Info className="w-5 h-5 flex-shrink-0 mt-0.5" />
            <p className="text-sm leading-relaxed">
              <strong>Prices shown are ranges</strong> — not quotes. Every project is different. The exact cost for your job depends on your specific site conditions, soil type, tank size, accessibility, and other factors. <strong>Call us for a free on-site estimate</strong> — we&apos;ll assess your property and give you an exact price before any work begins.
            </p>
          </div>
        </div>
      </section>

      {/* Septic Pricing */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="mb-10">
            <Badge className="mb-3 bg-secondary/10 text-primary border-0 text-xs">Septic Services</Badge>
            <h2 className="text-3xl font-bold text-slate-900 mb-2">Septic System Pricing</h2>
            <p className="text-slate-600">Typical cost ranges for Warren, Ohio and surrounding Trumbull County, 2026.</p>
          </div>
          <div className="space-y-4">
            {SERVICES_PRICING.map((item) => (
              <Card key={item.service} className="border-slate-200">
                <CardContent className="p-5">
                  <div className="flex flex-col sm:flex-row sm:items-center gap-4">
                    <div className="flex-1">
                      <h3 className="font-bold text-slate-900 mb-1">{item.service}</h3>
                      <p className="text-sm text-slate-500 mb-1">
                        <span className="font-medium text-slate-700">Range:</span> {item.factors}
                      </p>
                      <p className="text-sm text-slate-600">{item.notes}</p>
                    </div>
                    <div className="sm:text-right flex-shrink-0">
                      <div className="text-xl font-extrabold text-secondary">{item.range}</div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* Excavation Pricing */}
      <section className="py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="mb-10">
            <Badge className="mb-3 bg-slate-200 text-slate-700 border-0 text-xs">Excavation Services</Badge>
            <h2 className="text-3xl font-bold text-slate-900 mb-2">Excavation Pricing</h2>
            <p className="text-slate-600">Common excavation services and typical pricing for Northeast Ohio.</p>
          </div>
          <div className="space-y-4">
            {EXCAVATION_PRICING.map((item) => (
              <Card key={item.service} className="border-slate-200">
                <CardContent className="p-5">
                  <div className="flex flex-col sm:flex-row sm:items-center gap-4">
                    <div className="flex-1">
                      <h3 className="font-bold text-slate-900 mb-1">{item.service}</h3>
                      <p className="text-sm text-slate-500 mb-1">
                        <span className="font-medium text-slate-700">Depends on:</span> {item.factors}
                      </p>
                      <p className="text-sm text-slate-600">{item.notes}</p>
                    </div>
                    <div className="sm:text-right flex-shrink-0">
                      <div className="text-xl font-extrabold text-slate-700">{item.range}</div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* Factors That Affect Cost */}
      <section className="py-16 bg-white border-t">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-10">
            <h2 className="text-3xl font-bold text-slate-900 mb-3">What Affects Your Cost?</h2>
            <p className="text-slate-600 max-w-2xl mx-auto">
              No two septic or excavation jobs are exactly alike. Here&apos;s what we consider when giving you a quote.
            </p>
          </div>
          <div className="grid md:grid-cols-3 gap-6">
            {[
              {
                title: "Soil Conditions",
                desc: "Clay soil, rocky ground, and poor percolation require larger drain fields or alternative systems, increasing cost.",
              },
              {
                title: "Accessibility",
                desc: "Equipment access matters. Tight spaces, steep slopes, or limited access require smaller equipment or hand labor.",
              },
              {
                title: "Tank Size & Type",
                desc: "Larger tanks cost more. Alternative systems (aerobic, mound) cost significantly more than conventional.",
              },
              {
                title: "Permit & Inspection Fees",
                desc: "Ohio health department permit fees are separate from installation costs. We handle the paperwork — you pay the permit fee.",
              },
              {
                title: "Existing System Condition",
                desc: "Removing an old tank, dealing with contaminated soil, or repairing damage from a failed system adds to the cost.",
              },
              {
                title: "Distance & Site Prep",
                desc: "Very remote locations or sites requiring significant clearing or demolition before work begins cost more.",
              },
            ].map((item) => (
              <Card key={item.title} className="bg-slate-50 border-0">
                <CardContent className="p-5">
                  <h3 className="font-bold text-slate-900 mb-2">{item.title}</h3>
                  <p className="text-sm text-slate-600 leading-relaxed">{item.desc}</p>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="bg-primary py-14">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white">
          <h2 className="text-3xl font-bold mb-4">Get an Exact Price for Your Project</h2>
          <p className="text-blue-100 text-lg mb-8 max-w-2xl mx-auto">
            The ranges above are a starting point. For an accurate quote, call or fill out our form — we&apos;ll come to your property and give you a real number.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <a href={`tel:${COMPANY.phone}`}>
              <Button size="lg" className="gap-2 bg-white text-primary hover:bg-secondary/10 font-bold text-lg px-10">
                <Phone className="w-5 h-5" />
                Call {COMPANY.phone}
              </Button>
            </a>
            <Link href="/contact">
              <Button size="lg" variant="outline" className="text-lg px-10 border-white text-white hover:bg-white/10 font-bold">
                Get Free Estimate <ArrowRight className="w-4 h-4 ml-2" />
              </Button>
            </Link>
          </div>
          <p className="text-sm text-blue-200 mt-4">Free on-site estimates. No obligation.</p>
        </div>
      </section>
    </>
  );
}



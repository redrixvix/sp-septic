import type { Metadata } from "next";
import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Phone, CheckCircle, FileText, ArrowRight, MapPin } from "lucide-react";
import { COMPANY } from "@/lib/data";

export const metadata: Metadata = {
  title: "Ohio Septic Permits — Trumbull County Requirements | S&P Septic",
  description:
    "Learn about Ohio septic system permits, Trumbull County Health Department requirements, and what you need before installing or repairing a septic system.",
};

const PERMIT_INFO = [
  {
    title: "New Septic System Installation",
    desc: "Requires a permit from your local health department before any work begins. We handle the application process for you.",
    steps: [
      "Site/soil evaluation (perk test)",
      "System design by licensed designer",
      "Health department application submission",
      "Permit approval and issuance",
      "Installation by licensed contractor",
      "Final inspection by health department",
    ],
  },
  {
    title: "Septic System Repair",
    desc: "Minor repairs may not require permits. Major repairs — especially those involving drain field replacement — typically do.",
    steps: [
      "Assessment and scope determination",
      "Health department consultation on permit requirements",
      "Permit application (if required)",
      "Repair work by licensed contractor",
      "Inspection (if required)",
    ],
  },
  {
    title: "Septic Tank Replacement",
    desc: "Tank replacement alone typically requires a permit. We handle the paperwork.",
    steps: [
      "Health department notification",
      "Permit application",
      "Pump-out of existing tank (required before replacement)",
      "New tank installation",
      "Inspection",
    ],
  },
];

const COUNTIES = [
  {
    name: "Trumbull County Health District",
    phone: "(330) 675-2489",
    url: "https://www.trumbull-health.com",
    address: "176 Chestnut Ave NE, Warren, OH 44483",
  },
  {
    name: "Mahoning County Health District",
    phone: "(330) 270-2855",
    url: "https://www.mahoninghealth.org",
    address: "305 Belmont St, Youngstown, OH 44502",
  },
  {
    name: "Portage County Health District",
    phone: "(330) 296-9919",
    url: "https://www.portagehealth.com",
    address: "705 Oakwood St, Ravenna, OH 44266",
  },
];

export default function PermitsPage() {
  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-primary text-white border-0">Ohio Regulations</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Ohio Septic Permitting Guide
          </h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            Understanding Ohio&apos;s septic permitting requirements can feel overwhelming. The good news: we handle the paperwork. Here&apos;s what you need to know.
          </p>
        </div>
      </section>

      {/* What We Handle */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <Badge className="mb-3 bg-secondary/10 text-primary border-0 text-xs">We Handle the Paperwork</Badge>
            <h2 className="text-3xl font-bold text-slate-900 mb-3">
              Permit Handling Is Part of Our Service
            </h2>
            <p className="text-slate-600 max-w-2xl mx-auto">
              When you hire S&P Septic for a new installation or major repair, we handle the entire permit process. You don&apos;t need to navigate the bureaucracy — that&apos;s our job.
            </p>
          </div>
          <div className="grid md:grid-cols-3 gap-6">
            {[
              {
                step: "1",
                title: "We Assess",
                desc: "We evaluate your property, soil conditions, and system needs to design the right solution.",
              },
              {
                step: "2",
                title: "We Apply",
                desc: "We complete and submit all required permit applications to your county health department.",
              },
              {
                step: "3",
                title: "We Inspect",
                desc: "We coordinate and attend all required inspections to ensure your system passes — the first time.",
              },
            ].map((item) => (
              <Card key={item.step} className="bg-slate-50 border-0">
                <CardContent className="p-6 text-center">
                  <div className="w-12 h-12 rounded-full bg-primary text-white flex items-center justify-center text-xl font-bold mx-auto mb-4">
                    {item.step}
                  </div>
                  <h3 className="font-bold text-slate-900 mb-2">{item.title}</h3>
                  <p className="text-sm text-slate-600">{item.desc}</p>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* Permit Types */}
      <section className="py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-slate-900 mb-3">Permit Requirements by Project Type</h2>
            <p className="text-slate-600 max-w-2xl mx-auto">
              Here&apos;s a general overview of what&apos;s required in Ohio. Requirements can vary by county — we know the specifics for yours.
            </p>
          </div>
          <div className="space-y-6">
            {PERMIT_INFO.map((permit) => (
              <Card key={permit.title} className="border-slate-200">
                <CardContent className="p-6">
                  <div className="grid lg:grid-cols-2 gap-6">
                    <div>
                      <h3 className="font-bold text-xl text-slate-900 mb-2">{permit.title}</h3>
                      <p className="text-slate-600 mb-4">{permit.desc}</p>
                      <a href="/contact">
                        <Button size="sm" className="gap-2 bg-primary hover:bg-primary/90">
                          Get Started <ArrowRight className="w-3 h-3" />
                        </Button>
                      </a>
                    </div>
                    <div>
                      <h4 className="font-semibold text-slate-700 text-sm mb-2">Typical Process:</h4>
                      <ul className="space-y-1.5">
                        {permit.steps.map((step) => (
                          <li key={step} className="flex items-start gap-2 text-sm text-slate-600">
                            <CheckCircle className="w-4 h-4 text-primary flex-shrink-0 mt-0.5" />
                            {step}
                          </li>
                        ))}
                      </ul>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* County Info */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-10">
            <h2 className="text-3xl font-bold text-slate-900 mb-3">County Health Departments We Work With</h2>
            <p className="text-slate-600">
              We handle permits in all three counties we serve. Here&apos;s where to find your local health department.
            </p>
          </div>
          <div className="grid md:grid-cols-3 gap-6">
            {COUNTIES.map((county) => (
              <Card key={county.name} className="border-slate-200 hover:shadow-md transition-shadow">
                <CardContent className="p-6">
                  <h3 className="font-bold text-slate-900 mb-3">{county.name}</h3>
                  <div className="space-y-2 text-sm text-slate-600">
                    <div className="flex items-start gap-2">
                      <Phone className="w-4 h-4 text-secondary flex-shrink-0 mt-0.5" />
                      <a href={`tel:${county.phone.replace(/[^0-9]/g, "")}`} className="hover:text-secondary">
                        {county.phone}
                      </a>
                    </div>
                    <div className="flex items-start gap-2">
                      <MapPin className="w-4 h-4 text-secondary flex-shrink-0 mt-0.5" />
                      <span>{county.address}</span>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* Key Regulation */}
      <section className="py-14 bg-primary/90 text-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-start gap-4">
            <FileText className="w-10 h-10 text-blue-300 flex-shrink-0" />
            <div>
              <h2 className="text-2xl font-bold mb-2">Important Ohio Regulation</h2>
              <p className="text-blue-100 leading-relaxed">
                Ohio Administrative Code (OAC) Chapter 3701-36 governs septic systems throughout the state. Key requirements include minimum lot sizes for new systems, required separation distances from wells and water lines, soil evaluation standards, and mandatory inspection schedules. We are fully familiar with these regulations and design all systems to exceed them.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* FAQ */}
      <section className="py-16 bg-slate-50">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-10">
            <h2 className="text-3xl font-bold text-slate-900 mb-3">Common Permit Questions</h2>
          </div>
          <div className="space-y-4">
            {[
              {
                q: "How long does a permit take to get approved?",
                a: "Ohio permit approval timelines vary by county. In Trumbull County, typical processing is 2–4 weeks for new system permits. We know the timelines for each county we serve and plan projects accordingly.",
              },
              {
                q: "Can I get a permit if my lot is small?",
                a: "Ohio has minimum lot size requirements for new septic systems (typically 1 acre for conventional systems, less for lots with excellent percolation). We can evaluate your lot and tell you what options are available — sometimes a smaller lot can still support a system with an alternative design.",
              },
              {
                q: "What happens if I install a septic system without a permit?",
                a: "Installing a septic system without a permit in Ohio is illegal and can result in significant fines, mandatory removal of the system, and difficulty selling your home. Never skip the permit process. We handle it — it&apos;s part of our service.",
              },
              {
                q: "Do I need a permit to pump my septic tank?",
                a: "No — routine pumping does not require a permit. However, the pump-out company should provide a pumping certificate, which is good to keep with your records.",
              },
              {
                q: "What is a perk test and do I need one?",
                a: "A perk test (percolation test) measures how fast water drains through your soil. It is required for all new septic system permits in Ohio. The results determine what type of system your lot can support and how large the drain field must be.",
              },
            ].map((faq) => (
              <Card key={faq.q} className="border-slate-200">
                <CardContent className="p-5">
                  <h3 className="font-bold text-slate-900 mb-2">{faq.q}</h3>
                  <p className="text-sm text-slate-600 leading-relaxed">{faq.a}</p>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="bg-slate-900 py-14">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white">
          <h2 className="text-3xl font-bold mb-4">Ready to Start Your Project?</h2>
          <p className="text-slate-300 text-lg mb-8">
            Don&apos;t navigate the permit process alone. We handle everything — permits, design, installation, and inspection.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <a href={`tel:${COMPANY.phone}`}>
              <div className="inline-flex items-center gap-2 bg-primary hover:bg-primary/90 text-white font-bold text-lg px-10 py-3 rounded-lg transition-colors">
                <Phone className="w-5 h-5" />
                Call {COMPANY.phone}
              </div>
            </a>
            <Link href="/contact">
              <Button size="lg" variant="outline" className="text-lg px-10 border-slate-600 text-white hover:bg-slate-800 font-bold">
                Get Free Estimate <ArrowRight className="w-4 h-4 ml-2" />
              </Button>
            </Link>
          </div>
        </div>
      </section>
    </>
  );
}

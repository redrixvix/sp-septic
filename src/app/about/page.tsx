import type { Metadata } from "next";
import { Phone, Mail, MapPin, Shield, Award, Users } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { COMPANY } from "@/lib/data";

export const metadata: Metadata = {
  title: "About Us",
  description: "Learn about S&P Septic and Excavating Inc. - a family-owned, BBB Accredited contractor serving Warren, Ohio.",
};

const MILESTONES = [
  { year: "2021", event: "Company founded in Warren, Ohio" },
  { year: "2025", event: "BBB Accredited with A+ Rating achieved" },
  { year: "Today", event: "Proudly serving Trumbull County and beyond" },
];

const VALUES = [
  {
    icon: Users,
    title: "Family First",
    description: "We treat every customer like family. Your home is our home, and your septic problem gets the same attention we'd give our own.",
  },
  {
    icon: Shield,
    title: "Integrity Always",
    description: "Upfront pricing, honest assessments, and no surprise charges. We tell you exactly what you need and why — not what costs the most.",
  },
  {
    icon: Award,
    title: "Quality Never Compromised",
    description: "We use premium materials and proven installation methods. Your system is built to last decades, not just years.",
  },
];

export default function AboutPage() {
  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-emerald-600 text-white border-0">About Us</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Your Local, Trusted Septic Experts
          </h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            S&P Septic and Excavating Inc. is a family-owned contractor built on honest work and fair prices. We serve Warren, Ohio and the greater Trumbull County area.
          </p>
        </div>
      </section>

      {/* Story */}
      <section className="py-16 md:py-24 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div>
              <h2 className="text-3xl font-bold text-slate-900 mb-4">Our Story</h2>
              <div className="space-y-4 text-slate-600 leading-relaxed">
                <p>
                  S&P Septic and Excavating Inc. was founded in 2021 with a simple mission: provide Warren, Ohio and surrounding communities with honest, reliable septic and excavation services at fair prices.
                </p>
                <p>
                  As a family-owned and operated business, we understand that septic problems don&apos;t wait for convenient times. When your system fails, you need someone who will show up quickly, diagnose the problem accurately, and fix it right — the first time.
                </p>
                <p>
                  In 2025, we earned our BBB Accredited Business status with an A+ rating — a reflection of our commitment to doing things the right way, on every job, every time.
                </p>
                <p>
                  Whether you need a routine septic pumping, a complete new system installation, or emergency excavation work, you can count on S&P to deliver quality craftsmanship and genuine customer care.
                </p>
              </div>
            </div>
            <div className="space-y-6">
              <Card className="bg-slate-50 border-0">
                <CardContent className="p-6">
                  <div className="flex items-start gap-4">
                    <div className="w-12 h-12 rounded-xl bg-emerald-100 flex items-center justify-center text-emerald-600 flex-shrink-0">
                      <MapPin className="w-6 h-6" />
                    </div>
                    <div>
                      <h3 className="font-bold text-slate-900 mb-1">Location</h3>
                      <p className="text-slate-600 text-sm">
                        {COMPANY.address}<br />
                        {COMPANY.city}, {COMPANY.state} {COMPANY.zip}<br />
                        Trumbull County, Ohio
                      </p>
                    </div>
                  </div>
                </CardContent>
              </Card>
              <Card className="bg-slate-50 border-0">
                <CardContent className="p-6">
                  <div className="flex items-start gap-4">
                    <div className="w-12 h-12 rounded-xl bg-emerald-100 flex items-center justify-center text-emerald-600 flex-shrink-0">
                      <Phone className="w-6 h-6" />
                    </div>
                    <div>
                      <h3 className="font-bold text-slate-900 mb-1">Phone</h3>
                      <a href={`tel:${COMPANY.phone}`} className="text-emerald-600 font-semibold hover:underline">
                        {COMPANY.phone}
                      </a>
                      <p className="text-slate-500 text-xs mt-1">Mon–Sat, 7AM–7PM</p>
                    </div>
                  </div>
                </CardContent>
              </Card>
              <Card className="bg-slate-50 border-0">
                <CardContent className="p-6">
                  <div className="flex items-start gap-4">
                    <div className="w-12 h-12 rounded-xl bg-emerald-100 flex items-center justify-center text-emerald-600 flex-shrink-0">
                      <Shield className="w-6 h-6" />
                    </div>
                    <div>
                      <h3 className="font-bold text-slate-900 mb-1">BBB Accredited A+</h3>
                      <p className="text-slate-600 text-sm">
                        Accredited since August 2025. Committed to BBB Standards for Trust.
                      </p>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>
          </div>
        </div>
      </section>

      {/* Values */}
      <section className="py-16 md:py-24 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-4">
              What We Stand For
            </h2>
          </div>
          <div className="grid md:grid-cols-3 gap-6">
            {VALUES.map((v) => (
              <Card key={v.title} className="bg-white border-0 shadow-sm">
                <CardContent className="p-6 text-center">
                  <div className="w-14 h-14 rounded-2xl bg-emerald-50 flex items-center justify-center text-emerald-600 mx-auto mb-4">
                    <v.icon className="w-7 h-7" />
                  </div>
                  <h3 className="font-bold text-lg text-slate-900 mb-2">{v.title}</h3>
                  <p className="text-sm text-slate-600 leading-relaxed">{v.description}</p>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* Timeline */}
      <section className="py-16 md:py-20 bg-white">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-slate-900 mb-4">Our Milestones</h2>
          </div>
          <div className="space-y-6">
            {MILESTONES.map((m, i) => (
              <div key={m.year} className="flex gap-4">
                <div className="flex flex-col items-center">
                  <div className="w-16 h-16 rounded-full bg-emerald-600 text-white flex items-center justify-center font-bold text-lg flex-shrink-0">
                    {m.year}
                  </div>
                  {i < MILESTONES.length - 1 && (
                    <div className="w-0.5 flex-1 bg-slate-200 my-2" />
                  )}
                </div>
                <div className="pt-4 pb-6">
                  <p className="text-slate-700 font-medium">{m.event}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="bg-emerald-600 py-14">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white">
          <h2 className="text-3xl font-bold mb-4">Meet the Team — Call Us Today</h2>
          <p className="text-emerald-100 text-lg mb-8">Experience the S&P difference for yourself.</p>
          <a href={`tel:${COMPANY.phone}`}>
            <Button size="lg" className="gap-2 bg-white text-emerald-700 hover:bg-emerald-50 text-lg px-8">
              <Phone className="w-5 h-5" />
              Call {COMPANY.phone}
            </Button>
          </a>
        </div>
      </section>
    </>
  );
}

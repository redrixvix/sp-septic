import type { Metadata } from "next";
import Link from "next/link";
import { Phone, MapPin, Shield, Award, Users, CheckCircle } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { COMPANY } from "@/lib/data";

export const metadata: Metadata = {
  title: "About Us — S&P Septic and Excavating Inc.",
  description:
    "Learn about S&P Septic and Excavating Inc. — a family-owned, BBB Accredited contractor serving Warren, Ohio since 2021.",
};

const MILESTONES = [
  { year: "2021", event: "S&P Septic and Excavating Inc. founded in Warren, Ohio" },
  { year: "2021–2024", event: "Grew to serve Trumbull, Mahoning, and Portage Counties" },
  { year: "2025", event: "Earned BBB Accredited Business status with A+ Rating" },
  { year: "2026", event: "50+ successful projects completed — growing every day" },
];

const CREDENTIALS = [
  {
    icon: Shield,
    title: "BBB Accredited",
    desc: "Accredited since August 2025 with an A+ Rating. Committed to BBB Standards for Trust.",
    badge: "A+",
  },
  {
    icon: Award,
    title: "Ohio Licensed Contractor",
    desc: "Fully licensed to perform septic and excavation work throughout the state of Ohio.",
    badge: "Licensed",
  },
  {
    icon: CheckCircle,
    title: "Fully Insured",
    desc: "Comprehensive general liability insurance protects your property during every job.",
    badge: "Insured",
  },
  {
    icon: Users,
    title: "Family Owned & Operated",
    desc: "We live and work in this community. Your project gets the personal attention it deserves.",
    badge: "Local",
  },
];

const VALUES = [
  {
    title: "Honest Assessments",
    description: "We tell you exactly what you need — not what costs the most. No upselling, no scare tactics.",
  },
  {
    title: "Transparent Pricing",
    description: "The quote we give is the price you pay. No surprise charges after the work begins.",
  },
  {
    title: "Quality Materials",
    description: "We use proven products and methods. Your system is built to last decades, not just seasons.",
  },
  {
    title: "Respect for Your Property",
    description: "We leave job sites clean and take care of your landscaping. Your home is our home.",
  },
];

export default function AboutPage() {
  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-primary text-white border-0">About Us</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Your Local, Trusted Septic Experts
          </h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            Family-owned, licensed, and BBB Accredited. We&apos;ve served Warren, Ohio and the greater Trumbull County area since 2021.
          </p>
        </div>
      </section>

      {/* Story Section */}
      <section className="py-16 md:py-24 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12 items-start">
            <div>
              <h2 className="text-3xl font-bold text-slate-900 mb-6">Our Story</h2>
              <div className="space-y-4 text-slate-600 leading-relaxed">
                <p>
                  S&P Septic and Excavating Inc. was founded in 2021 with a straightforward mission: provide Warren, Ohio and the surrounding communities with honest, reliable septic and excavation services at fair prices.
                </p>
                <p>
                  As a family-owned and operated business, we understand that septic problems don&apos;t respect convenient timing. When your system fails, you need someone who shows up quickly, diagnoses the problem accurately, and fixes it right — the first time.
                </p>
                <p>
                  In August 2025, we earned our <strong>BBB Accredited Business</strong> status with an <strong>A+ Rating</strong> — a reflection of our commitment to doing things the right way, on every job, every time.
                </p>
                <p>
                  Whether you need a routine septic pumping, a complete new system installation, or emergency excavation work, you can count on S&P to deliver quality craftsmanship and genuine customer care.
                </p>
              </div>
            </div>

            {/* Credentials Grid */}
            <div className="space-y-4">
              <h3 className="font-bold text-slate-900 text-lg">Our Credentials</h3>
              {CREDENTIALS.map((cred) => (
                <Card key={cred.title} className="bg-slate-50 border-0">
                  <CardContent className="p-5">
                    <div className="flex items-start gap-4">
                      <div className="w-12 h-12 rounded-xl bg-secondary/10 flex items-center justify-center text-secondary flex-shrink-0">
                        <cred.icon className="w-6 h-6" />
                      </div>
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-0.5">
                          <h4 className="font-bold text-slate-900">{cred.title}</h4>
                          <span className="text-xs font-bold text-secondary bg-secondary/10 px-2 py-0.5 rounded-full">
                            {cred.badge}
                          </span>
                        </div>
                        <p className="text-sm text-slate-600">{cred.desc}</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          </div>
        </div>
      </section>

      {/* Values Section */}
      <section className="py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <Badge className="mb-3 bg-secondary/10 text-primary border-0 text-xs">What We Stand For</Badge>
            <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-3">
              How We Do Business
            </h2>
            <p className="text-slate-600 max-w-2xl mx-auto">
              These aren&apos;t just values on a wall — they&apos;re how we actually operate on every single job.
            </p>
          </div>
          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
            {VALUES.map((v) => (
              <Card key={v.title} className="bg-white border-0 shadow-sm text-center">
                <CardContent className="p-6">
                  <div className="w-12 h-12 rounded-full bg-secondary/10 flex items-center justify-center mx-auto mb-4">
                    <CheckCircle className="w-6 h-6 text-secondary" />
                  </div>
                  <h3 className="font-bold text-slate-900 mb-2">{v.title}</h3>
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
            <h2 className="text-3xl font-bold text-slate-900 mb-3">Our Milestones</h2>
          </div>
          <div className="space-y-0">
            {MILESTONES.map((m, i) => (
              <div key={m.year} className="flex gap-6 relative">
                <div className="flex flex-col items-center">
                  <div className="w-16 h-16 rounded-full bg-primary text-white flex items-center justify-center font-bold text-sm flex-shrink-0 z-10">
                    {m.year}
                  </div>
                  {i < MILESTONES.length - 1 && (
                    <div className="w-0.5 flex-1 bg-slate-200 my-1" />
                  )}
                </div>
                <div className="pt-4 pb-8">
                  <p className="text-slate-700 font-medium leading-relaxed">{m.event}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="bg-slate-900 py-14">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white">
          <h2 className="text-3xl font-bold mb-4">Ready to Work With Us?</h2>
          <p className="text-slate-300 text-lg mb-8 max-w-2xl mx-auto">
            Whether it&apos;s a new septic system, an emergency repair, or excavation work — we&apos;d love to show you the S&P difference.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <a href={`tel:${COMPANY.phone}`}>
              <Button size="lg" className="gap-2 bg-primary hover:bg-primary/90 font-bold text-lg px-8">
                <Phone className="w-5 h-5" />
                Call {COMPANY.phone}
              </Button>
            </a>
            <Link href="/contact">
              <Button size="lg" variant="outline" className="text-lg px-8 border-slate-600 text-white hover:bg-slate-800 font-bold">
                Get Free Estimate
              </Button>
            </Link>
          </div>
          <div className="mt-6 flex items-center justify-center gap-2 text-sm text-slate-400">
            <MapPin className="w-4 h-4" />
            <span>{COMPANY.address}, {COMPANY.city}, {COMPANY.state} {COMPANY.zip}</span>
          </div>
        </div>
      </section>
    </>
  );
}

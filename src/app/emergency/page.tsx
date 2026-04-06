import type { Metadata } from "next";
import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Phone, AlertTriangle, Clock, Shield, ArrowRight, Droplets } from "lucide-react";
import { COMPANY } from "@/lib/data";

export const metadata: Metadata = {
  title: "24/7 Emergency Service",
  description:
    "Septic emergency in Warren, Ohio? S&P Septic offers 24/7 emergency septic service. Sewage backup, strong odors, and more — fast response. Call (330) 979-3930.",
};

const EMERGENCIES = [
  {
    icon: Droplets,
    title: "Sewage Backup",
    desc: "Sewage backing up into your home — toilets, drains, or floor drains. This is a health hazard and needs immediate attention.",
    severity: "critical",
  },
  {
    icon: AlertTriangle,
    title: "Strong Sewage Odors",
    desc: "Persistent sewage smell inside your home or in your yard. Indicates a crack, vent issue, or system failure.",
    severity: "high",
  },
  {
    icon: Droplets,
    title: "Wet Spots / Standing Water",
    desc: "Wet, squishy ground or standing water over your drain field or septic tank area. Could indicate a failed system.",
    severity: "high",
  },
  {
    icon: AlertTriangle,
    title: "Burst or Frozen Pipes",
    desc: "Exposed septic pipes that have frozen or burst — especially in Ohio winters — need emergency repair.",
    severity: "critical",
  },
  {
    icon: Droplets,
    title: "Drain Field Flooding",
    desc: "If your drain field is underwater or saturated, wastewater has nowhere to go. Immediate intervention needed.",
    severity: "critical",
  },
  {
    icon: AlertTriangle,
    title: "Complete System Failure",
    desc: "Multiple drains backing up simultaneously, toilets not flushing, and strong odors throughout the house.",
    severity: "critical",
  },
];

export default function EmergencyPage() {
  return (
    <>
      {/* Hero */}
      <section className="bg-red-700 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center gap-3 mb-4">
            <div className="w-3 h-3 rounded-full bg-white animate-pulse" />
            <Badge className="bg-white text-red-700 border-0 font-bold text-xs">24/7 Emergency Service</Badge>
          </div>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Septic Emergency?
          </h1>
          <p className="text-red-100 text-lg max-w-2xl mb-6">
            Don&apos;t wait. A septic emergency is a health hazard. Call S&P now — we prioritize emergency calls and aim for same-day response.
          </p>
          <a href={`tel:${COMPANY.phone}`}>
            <div className="inline-flex items-center gap-3 bg-white text-red-700 hover:bg-red-50 font-black text-2xl px-10 py-4 rounded-xl shadow-2xl transition-all">
              <Phone className="w-7 h-7" />
              {COMPANY.phone}
            </div>
          </a>
          <p className="mt-4 text-red-200 text-sm">Available Mon–Sat, 7AM–7PM for emergency calls</p>
        </div>
      </section>

      {/* Emergency Notice */}
      <section className="bg-amber-50 border-b border-amber-100 py-5">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-start gap-3 text-amber-800">
            <AlertTriangle className="w-5 h-5 flex-shrink-0 mt-0.5" />
            <p className="text-sm leading-relaxed">
              <strong>If you have sewage backing up into your home or believe there is an immediate health risk:</strong> Call us directly at{" "}
              <a href={`tel:${COMPANY.phone}`} className="font-bold underline">
                {COMPANY.phone}
              </a>
              . Do not wait for an online form response.
            </p>
          </div>
        </div>
      </section>

      {/* What Qualifies */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-slate-900 mb-3">
              What Counts as a Septic Emergency?
            </h2>
            <p className="text-slate-600 max-w-2xl mx-auto">
              Not every septic issue is an emergency. Here&apos;s when to call immediately — and when it can wait for a regular appointment.
            </p>
          </div>
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {EMERGENCIES.map((item) => (
              <Card key={item.title} className={`border-0 shadow-sm ${item.severity === "critical" ? "bg-red-50" : "bg-amber-50"}`}>
                <CardContent className="p-5">
                  <div className={`w-10 h-10 rounded-lg flex items-center justify-center mb-3 ${item.severity === "critical" ? "bg-red-100 text-red-600" : "bg-amber-100 text-amber-600"}`}>
                    <item.icon className="w-5 h-5" />
                  </div>
                  <h3 className="font-bold text-slate-900 mb-1">{item.title}</h3>
                  <p className="text-sm text-slate-600 leading-relaxed">{item.desc}</p>
                  {item.severity === "critical" && (
                    <Badge className="mt-2 bg-red-600 text-white border-0 text-xs">Call Now</Badge>
                  )}
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* What NOT to Do */}
      <section className="py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div>
              <Badge className="mb-3 bg-red-100 text-red-700 border-0 text-xs">Critical</Badge>
              <h2 className="text-3xl font-bold text-slate-900 mb-4">
                What to Do While You Wait
              </h2>
              <div className="space-y-3">
                {[
                  { step: "1", text: "Stop using all water in the house immediately — don&apos;t flush, don&apos;t run faucets, don&apos;t do laundry" },
                  { step: "2", text: "Call us right away at (330) 979-3930" },
                  { step: "3", text: "Keep children and pets away from the affected area" },
                  { step: "4", text: "If sewage has backed up into your home, wear rubber gloves and avoid contact with skin" },
                  { step: "5", text: "Open windows for ventilation if safe to do so" },
                ].map((item) => (
                  <div key={item.step} className="flex gap-3">
                    <div className="w-7 h-7 rounded-full bg-red-600 text-white flex items-center justify-center text-xs font-bold flex-shrink-0">
                      {item.step}
                    </div>
                    <p className="text-sm text-slate-600" dangerouslySetInnerHTML={{ __html: item.text }} />
                  </div>
                ))}
              </div>
            </div>
            <Card className="bg-slate-900 border-0 text-white">
              <CardContent className="p-8 text-center">
                <div className="w-16 h-16 rounded-full bg-red-600 flex items-center justify-center mx-auto mb-4">
                  <Phone className="w-8 h-8 text-white" />
                </div>
                <h3 className="text-xl font-bold mb-2">Emergency Line</h3>
                <p className="text-slate-300 mb-6">
                  For septic emergencies during business hours, call our main line.
                </p>
                <a href={`tel:${COMPANY.phone}`}>
                  <div className="inline-flex items-center gap-2 bg-red-600 hover:bg-red-700 text-white font-bold text-xl px-8 py-3 rounded-lg transition-colors">
                    <Phone className="w-6 h-6" />
                    {COMPANY.phone}
                  </div>
                </a>
                <div className="mt-4 text-sm text-slate-400">
                  <Clock className="w-4 h-4 inline mr-1" />
                  Mon–Sat, 7AM–7PM
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* Non-Emergency */}
      <section className="py-12 bg-white border-t">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-xl font-bold text-slate-900 mb-3">
            Not an Emergency?
          </h2>
          <p className="text-slate-600 mb-6">
            Slow drains, routine pumping, or general questions? Use our regular contact options — no need for emergency service.
          </p>
          <div className="flex flex-col sm:flex-row gap-3 justify-center">
            <Link href="/contact">
              <Button size="lg" className="gap-2 bg-primary hover:bg-primary/90 font-semibold">
                Contact Form <ArrowRight className="w-4 h-4" />
              </Button>
            </Link>
            <Link href="/schedule">
              <Button size="lg" variant="outline" className="gap-2">
                Schedule Service <ArrowRight className="w-4 h-4" />
              </Button>
            </Link>
          </div>
        </div>
      </section>

      {/* Trust Strip */}
      <section className="bg-slate-900 py-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-8 text-center text-white">
            {[
              { icon: Shield, label: "Licensed & Insured" },
              { icon: Clock, label: "Same-Day Response" },
              { icon: Phone, label: "(330) 979-3930" },
              { icon: AlertTriangle, label: "Emergency Service" },
            ].map((item) => (
              <div key={item.label} className="flex flex-col items-center gap-2">
                <item.icon className="w-6 h-6 text-primary" />
                <span className="text-sm font-semibold">{item.label}</span>
              </div>
            ))}
          </div>
        </div>
      </section>
    </>
  );
}

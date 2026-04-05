import type { Metadata } from "next";
import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { Phone, CheckCircle, ArrowRight, Calendar, Droplets } from "lucide-react";
import { Button } from "@/components/ui/button";
import { COMPANY } from "@/lib/data";

export const metadata: Metadata = {
  title: "Septic Maintenance Guide — S&P Septic & Excavating | Warren, Ohio",
  description:
    "The complete homeowner's guide to septic system maintenance. Seasonal tips, do's and don'ts, and maintenance schedules for Ohio homeowners with septic systems.",
};

const SEASONS = [
  {
    season: "Spring",
    icon: "🌱",
    color: "bg-secondary/10 border-secondary/30",
    tips: [
      "Inspect your tank after winter — freeze damage can cause cracks",
      "Check for wet spots in your yard that shouldn't be there",
      "Pump your tank if it's been 3-5 years",
      "Redirect downspouts and sump pump discharge away from the drain field",
      "Look for slow drains — catch problems before they become emergencies",
    ],
  },
  {
    season: "Summer",
    icon: "☀️",
    color: "bg-amber-50 border-amber-200",
    tips: [
      "Avoid planting trees or shrubs near the drain field — roots can pierce pipes",
      "Don't park vehicles or place heavy objects on the drain field",
      "Check your garbage disposal use — limit what goes in",
      "Watch for bright green patches over the drain field (possible failure sign)",
      "Get a camera inspection if you're having persistent drain issues",
    ],
  },
  {
    season: "Fall",
    icon: "🍂",
    color: "bg-orange-50 border-orange-200",
    tips: [
      "Pump your tank before winter if needed — easier to schedule in fall",
      "Rake and remove leaves from the drain field area",
      "Check all indoor drains — make sure traps have water",
      "Inspect your riser lids for damage or gaps before cold weather",
      "Consider an annual inspection before the busy winter season",
    ],
  },
  {
    season: "Winter",
    icon: "❄️",
    color: "bg-blue-50 border-blue-200",
    tips: [
      "Prevent freezing: ensure tank lids are well-insulated",
      "Keep the drain field area clear of snow compaction",
      "Never drive on the drain field when it's frozen or saturated",
      "Fix slow drains NOW — don't let them freeze and worsen",
      "Know where your tank is so you can find it quickly in an emergency",
    ],
  },
];

const DO_DONT = [
  { type: "Do", items: ["Pump your tank every 3–5 years", "Use a lint trap on washing machine discharge", "Fix leaks in faucets and toilets promptly", "Keep records of all septic work", "Direct roof gutters and sump pumps away from drain field", "Use single-ply, biodegradable toilet paper"] },
  { type: "Don't", items: ["Flush baby wipes, cleaning wipes, or any wipes", "Use liquid fabric softener — it coats the drain field", "Pour grease, fat, or oil down any drain", "Use septic tank additives — most are unnecessary", "Flush paper towels or facial tissues", "Park on or build over your drain field"] },
];

export default function MaintenanceGuidePage() {
  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-primary text-white border-0">Free Guide</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Homeowner&apos;s Septic Maintenance Guide
          </h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            Everything Ohio homeowners with a septic system need to know. Seasonal maintenance tips, what to avoid, and how to make your system last.
          </p>
        </div>
      </section>

      {/* Key Rule */}
      <section className="bg-primary py-10">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white">
          <h2 className="text-2xl md:text-3xl font-bold mb-3">
            The #1 Rule of Septic Ownership
          </h2>
          <p className="text-blue-100 text-lg max-w-3xl mx-auto">
            <strong>Pump your tank regularly.</strong> No additive, no magic product, no special trick replaces regular pumping. Every 3–5 years for most households. It&apos;s the single most important thing you can do for your system.
          </p>
        </div>
      </section>

      {/* Seasonal Guide */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <Badge className="mb-3 bg-slate-100 text-slate-700 border-0 text-xs">Seasonal Maintenance</Badge>
            <h2 className="text-3xl font-bold text-slate-900 mb-3">Maintenance by Season</h2>
            <p className="text-slate-600 max-w-2xl mx-auto">
              Septic systems face different challenges throughout the year. Here&apos;s what to focus on — and when.
            </p>
          </div>
          <div className="grid md:grid-cols-2 gap-6">
            {SEASONS.map((s) => (
              <Card key={s.season} className={`border ${s.color}`}>
                <CardContent className="p-6">
                  <div className="flex items-center gap-3 mb-4">
                    <span className="text-4xl">{s.icon}</span>
                    <h3 className="font-bold text-xl text-slate-900">{s.season} Maintenance</h3>
                  </div>
                  <ul className="space-y-2.5">
                    {s.tips.map((tip) => (
                      <li key={tip} className="flex items-start gap-2.5 text-sm text-slate-700">
                        <CheckCircle className="w-4 h-4 text-primary flex-shrink-0 mt-0.5" />
                        {tip}
                      </li>
                    ))}
                  </ul>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* Do's and Don'ts */}
      <section className="py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-10">
            <h2 className="text-3xl font-bold text-slate-900 mb-3">The Do&apos;s and Don&apos;ts</h2>
          </div>
          <div className="grid md:grid-cols-2 gap-6 max-w-4xl mx-auto">
            <Card className="bg-secondary/10 border-secondary/30 border-0">
              <CardContent className="p-6">
                <h3 className="font-bold text-lg text-secondary mb-4 flex items-center gap-2">
                  <CheckCircle className="w-5 h-5 text-secondary" />
                  Do
                </h3>
                <ul className="space-y-2">
                  {DO_DONT[0].items.map((item) => (
                    <li key={item} className="flex items-start gap-2 text-sm text-secondary">
                      <CheckCircle className="w-4 h-4 text-secondary flex-shrink-0 mt-0.5" />
                      {item}
                    </li>
                  ))}
                </ul>
              </CardContent>
            </Card>
            <Card className="bg-red-50 border-red-200 border-0">
              <CardContent className="p-6">
                <h3 className="font-bold text-lg text-red-800 mb-4 flex items-center gap-2">
                  <Droplets className="w-5 h-5 text-red-500" />
                  Don&apos;t
                </h3>
                <ul className="space-y-2">
                  {DO_DONT[1].items.map((item) => (
                    <li key={item} className="flex items-start gap-2 text-sm text-red-900">
                      <div className="w-4 h-4 rounded-full bg-red-300 flex items-center justify-center flex-shrink-0 mt-0.5">
                        <div className="w-1.5 h-1.5 rounded-full bg-red-600" />
                      </div>
                      {item}
                    </li>
                  ))}
                </ul>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* Maintenance Schedule */}
      <section className="py-16 bg-white">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-10">
            <h2 className="text-3xl font-bold text-slate-900 mb-3">Recommended Maintenance Schedule</h2>
          </div>
          <div className="space-y-3">
            {[
              { freq: "Every 3–5 years", task: "Septic tank pumping (every 3 years for heavy use, 5 for light)" },
              { freq: "Every 1–2 years", task: "Professional septic inspection with camera" },
              { freq: "Every 6 months", task: "Check tank water level and baffle condition" },
              { freq: "Every month", task: "Inspect drains for slow flow — catch problems early" },
              { freq: "Ongoing", task: "Keep detailed records of all septic work, pumping, and inspections" },
            ].map((item) => (
              <Card key={item.freq} className="border-slate-200">
                <CardContent className="p-4 flex items-start gap-4">
                  <div className="w-28 flex-shrink-0">
                    <Badge className="bg-secondary/10 text-primary border-0 text-xs font-bold whitespace-nowrap">
                      {item.freq}
                    </Badge>
                  </div>
                  <p className="text-sm text-slate-700 font-medium">{item.task}</p>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* Warning Signs */}
      <section className="py-14 bg-red-700 text-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-2xl font-bold mb-4 text-center">When to Call Immediately</h2>
          <div className="grid sm:grid-cols-2 gap-4">
            {[
              "Slow drains throughout your entire house",
              "Strong sewage odors indoors or in the yard",
              "Wet spots or standing water over the drain field",
              "Bright green grass over part of your lawn",
              "Gurgling sounds from your plumbing",
              "Sewage backing up into toilets or drains",
            ].map((sign) => (
              <div key={sign} className="flex items-center gap-2 text-sm text-red-100">
                <div className="w-2 h-2 rounded-full bg-white flex-shrink-0" />
                {sign}
              </div>
            ))}
          </div>
          <div className="text-center mt-8">
            <a href={`tel:${COMPANY.phone}`}>
              <div className="inline-flex items-center gap-2 bg-white text-red-700 hover:bg-red-50 font-bold text-lg px-8 py-3 rounded-lg">
                <Phone className="w-5 h-5" />
                Call {COMPANY.phone}
              </div>
            </a>
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="py-14 bg-white border-t">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <Calendar className="w-10 h-10 text-secondary mx-auto mb-4" />
          <h2 className="text-2xl font-bold text-slate-900 mb-3">
            Need to Schedule Maintenance?
          </h2>
          <p className="text-slate-600 mb-6">
            Regular maintenance prevents emergencies. Pump your tank, get an inspection, or ask about our maintenance plans.
          </p>
          <div className="flex flex-col sm:flex-row gap-3 justify-center">
            <a href={`tel:${COMPANY.phone}`}>
              <Button size="lg" className="gap-2 bg-primary hover:bg-primary/90 font-semibold">
                <Phone className="w-4 h-4" />
                Call {COMPANY.phone}
              </Button>
            </a>
            <Link href="/schedule">
              <Button size="lg" variant="outline" className="gap-2">
                Schedule Service <ArrowRight className="w-4 h-4" />
              </Button>
            </Link>
          </div>
        </div>
      </section>
    </>
  );
}

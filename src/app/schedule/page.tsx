import type { Metadata } from "next";
import { redirect } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import {
  Phone,
  Clock,
  MapPin,
  CheckCircle,
  Calendar,
  AlertCircle,
} from "lucide-react";
import { COMPANY } from "@/lib/data";

export const metadata: Metadata = {
  title: "Schedule Service — S&P Septic & Excavating | Warren, Ohio",
  description:
    "Schedule septic service, excavation work, or a free estimate with S&P Septic and Excavating in Warren, Ohio. Fast response, honest pricing.",
};

const SERVICE_TYPES = [
  "Septic Tank Pumping",
  "Septic System Inspection",
  "Septic System Repair",
  "New Septic Installation",
  "Drain Field Replacement",
  "Excavation Services",
  "Camera Inspection",
  "Perk Testing",
  "Other / Not Sure",
];

const TIME_SLOTS = [
  "Morning (7AM – 12PM)",
  "Afternoon (12PM – 5PM)",
  "Any time (7AM – 7PM)",
];

export default function SchedulePage() {
  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-primary text-white border-0">Schedule Service</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Book a Service Call
          </h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            Fill out the form below and we&apos;ll contact you within 1 business day to confirm your appointment. For immediate scheduling, call us directly.
          </p>
        </div>
      </section>

      {/* Main Content */}
      <section className="py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-3 gap-8">
            {/* Form */}
            <div className="lg:col-span-2">
              <Card className="bg-white border-0 shadow-sm">
                <CardContent className="p-6 md:p-8">
                  <h2 className="text-2xl font-bold text-slate-900 mb-6">
                    Request a Service Appointment
                  </h2>
                  <form className="space-y-5">
                    <div className="grid sm:grid-cols-2 gap-5">
                      <div>
                        <label className="block text-sm font-medium text-slate-700 mb-1.5">
                          Your Name <span className="text-red-500">*</span>
                        </label>
                        <Input placeholder="John Smith" required className="h-11" />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-slate-700 mb-1.5">
                          Phone <span className="text-red-500">*</span>
                        </label>
                        <Input type="tel" placeholder="(330) 000-0000" required className="h-11" />
                      </div>
                    </div>
                    <div className="grid sm:grid-cols-2 gap-5">
                      <div>
                        <label className="block text-sm font-medium text-slate-700 mb-1.5">
                          Email Address
                        </label>
                        <Input type="email" placeholder="john@example.com" className="h-11" />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-slate-700 mb-1.5">
                          Property Address <span className="text-red-500">*</span>
                        </label>
                        <Input placeholder="123 Main St, Warren, OH 44483" required className="h-11" />
                      </div>
                    </div>
                    <div className="grid sm:grid-cols-2 gap-5">
                      <div>
                        <label className="block text-sm font-medium text-slate-700 mb-1.5">
                          Service Needed <span className="text-red-500">*</span>
                        </label>
                        <select
                          required
                          className="flex h-11 w-full rounded-md border border-input bg-white px-3 py-2 text-sm ring-offset-white placeholder:text-slate-400 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                        >
                          <option value="">Select a service...</option>
                          {SERVICE_TYPES.map((s) => (
                            <option key={s} value={s}>{s}</option>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-slate-700 mb-1.5">
                          Preferred Time
                        </label>
                        <select className="flex h-11 w-full rounded-md border border-input bg-white px-3 py-2 text-sm ring-offset-white focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary focus-visible:ring-offset-2">
                          <option value="">Any time works...</option>
                          {TIME_SLOTS.map((t) => (
                            <option key={t} value={t}>{t}</option>
                          ))}
                        </select>
                      </div>
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-slate-700 mb-1.5">
                        Describe the Issue or Your Needs
                      </label>
                      <Textarea
                        rows={4}
                        placeholder="Tell us about your septic or excavation needs, when the problem started, any symptoms you've noticed..."
                        className="resize-none"
                      />
                    </div>
                    <div className="bg-amber-50 border border-amber-200 rounded-lg p-4 flex gap-3">
                      <AlertCircle className="w-5 h-5 text-amber-600 flex-shrink-0 mt-0.5" />
                      <div className="text-sm text-amber-800">
                        <strong>For emergencies</strong> (sewage backup, strong odors, etc.), please call us directly at{" "}
                        <a href={`tel:${COMPANY.phone}`} className="font-bold underline">
                          {COMPANY.phone}
                        </a>{" "}
                        — do not use this form.
                      </div>
                    </div>
                    <Button type="submit" size="lg" className="w-full gap-2 bg-primary hover:bg-primary/90 font-semibold">
                      <Calendar className="w-4 h-4" />
                      Submit Request
                    </Button>
                    <p className="text-xs text-slate-500 text-center">
                      We&apos;ll contact you within 1 business day to confirm your appointment.
                    </p>
                  </form>
                </CardContent>
              </Card>
            </div>

            {/* Sidebar */}
            <div className="space-y-6">
              <Card className="bg-primary border-0 text-white">
                <CardContent className="p-6 text-center">
                  <h3 className="font-bold text-lg mb-2">Prefer to Call?</h3>
                  <p className="text-blue-100 text-sm mb-4">
                    Schedule your service directly by phone. We answer calls Mon–Sat, 7AM–7PM.
                  </p>
                  <a href={`tel:${COMPANY.phone}`} className="block mb-4">
                    <Button className="w-full gap-2 bg-white text-primary hover:bg-secondary/10 font-bold text-lg">
                      <Phone className="w-5 h-5" />
                      {COMPANY.phone}
                    </Button>
                  </a>
                  <div className="flex items-center justify-center gap-2 text-sm text-blue-200">
                    <Clock className="w-4 h-4" />
                    <span>Mon–Sat, 7AM–7PM</span>
                  </div>
                </CardContent>
              </Card>

              <Card className="bg-white border-0 shadow-sm">
                <CardContent className="p-6 space-y-4">
                  <h3 className="font-bold text-slate-900">What to Expect</h3>
                  {[
                    { step: "1", text: "We contact you within 1 business day" },
                    { step: "2", text: "Discuss your needs and answer questions" },
                    { step: "3", text: "Schedule a convenient appointment time" },
                    { step: "4", text: "On-site service — diagnosis and solution" },
                  ].map((item) => (
                    <div key={item.step} className="flex gap-3">
                      <div className="w-6 h-6 rounded-full bg-secondary/10 text-primary flex items-center justify-center text-xs font-bold flex-shrink-0">
                        {item.step}
                      </div>
                      <span className="text-sm text-slate-600">{item.text}</span>
                    </div>
                  ))}
                </CardContent>
              </Card>

              <Card className="bg-white border-0 shadow-sm">
                <CardContent className="p-6">
                  <div className="flex items-start gap-3">
                    <MapPin className="w-5 h-5 text-secondary flex-shrink-0 mt-0.5" />
                    <div>
                      <h3 className="font-bold text-slate-900 text-sm mb-1">Service Area</h3>
                      <p className="text-xs text-slate-500">
                        Warren, Niles, Cortland, Girard, Trumbull County, Mahoning County, and Portage County.
                      </p>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}

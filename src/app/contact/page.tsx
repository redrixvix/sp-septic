"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Phone, Mail, MapPin, Clock, Send, CheckCircle } from "lucide-react";
import { COMPANY } from "@/lib/data";
import { submitContactForm } from "@/app/actions";
import { useFormStatus } from "react-dom";

function SubmitButton() {
  const { pending } = useFormStatus();
  return (
    <Button type="submit" size="lg" className="w-full gap-2 bg-primary hover:bg-primary/90" disabled={pending}>
      {pending ? (
        <>Sending...</>
      ) : (
        <>
          <Send className="w-4 h-4" />
          Send Message
        </>
      )}
    </Button>
  );
}

export default function ContactPage() {
  const [submitted, setSubmitted] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setError(null);
    const formData = new FormData(e.currentTarget);
    const result = await submitContactForm({ success: false, message: "" }, formData);
    if (result.success) {
      setSubmitted(true);
    } else {
      setError(result.message);
    }
  }

  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-primary text-white border-0">Contact Us</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">Get a Free Estimate</h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            Ready to solve your septic or excavation needs? Call us or fill out the form below — we respond within one business day.
          </p>
        </div>
      </section>

      {/* Main */}
      <section className="py-10 md:py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-3 gap-8">
            {/* Form */}
            <div className="lg:col-span-2">
              <Card className="bg-white border-0 shadow-sm">
                <CardContent className="p-6 md:p-8">
                  {submitted ? (
                    <div className="text-center py-12">
                      <div className="w-16 h-16 rounded-full bg-secondary/10 flex items-center justify-center mx-auto mb-4">
                        <CheckCircle className="w-8 h-8 text-secondary" />
                      </div>
                      <h2 className="text-2xl font-bold text-slate-900 mb-2">Message Sent!</h2>
                      <p className="text-slate-600 mb-6">
                        Thank you! We&apos;ll be in touch within one business day. For urgent issues, call us directly at{" "}
                        <a href={`tel:${COMPANY.phone}`} className="text-secondary font-semibold">
                          {COMPANY.phone}
                        </a>
                        .
                      </p>
                      <Button
                        variant="outline"
                        onClick={() => setSubmitted(false)}
                      >
                        Send Another Message
                      </Button>
                    </div>
                  ) : (
                    <>
                      <h2 className="text-2xl font-bold text-slate-900 mb-6">Request a Free Estimate</h2>
                      {error && (
                        <div className="mb-4 p-3 rounded-lg bg-red-50 border border-red-200 text-red-700 text-sm">
                          {error}
                        </div>
                      )}
                      <form onSubmit={handleSubmit} className="space-y-5">
                        <div className="grid sm:grid-cols-2 gap-5">
                          <div>
                            <label htmlFor="name" className="block text-sm font-medium text-slate-700 mb-1.5">
                              Your Name <span className="text-red-500">*</span>
                            </label>
                            <Input
                              id="name"
                              name="name"
                              placeholder="John Smith"
                              required
                              className="h-11"
                            />
                          </div>
                          <div>
                            <label htmlFor="phone" className="block text-sm font-medium text-slate-700 mb-1.5">
                              Phone Number <span className="text-red-500">*</span>
                            </label>
                            <Input
                              id="phone"
                              name="phone"
                              type="tel"
                              placeholder="(330) 000-0000"
                              required
                              className="h-11"
                            />
                          </div>
                        </div>
                        <div className="grid sm:grid-cols-2 gap-5">
                          <div>
                            <label htmlFor="email" className="block text-sm font-medium text-slate-700 mb-1.5">
                              Email Address
                            </label>
                            <Input
                              id="email"
                              name="email"
                              type="email"
                              placeholder="john@example.com"
                              className="h-11"
                            />
                          </div>
                          <div>
                            <label htmlFor="service" className="block text-sm font-medium text-slate-700 mb-1.5">
                              Service Needed <span className="text-red-500">*</span>
                            </label>
                            <select
                              id="service"
                              name="service"
                              required
                              className="flex h-11 w-full rounded-md border border-input bg-white px-3 py-2 text-sm ring-offset-white placeholder:text-slate-400 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                            >
                              <option value="">Select a service...</option>
                              <option>New Septic System Installation</option>
                              <option>Septic System Repair</option>
                              <option>Septic Pumping / Cleaning</option>
                              <option>Excavation Services</option>
                              <option>Perk Testing / Leach Field</option>
                              <option>Camera Inspection</option>
                              <option>Other</option>
                            </select>
                          </div>
                        </div>
                        <div>
                          <label htmlFor="address" className="block text-sm font-medium text-slate-700 mb-1.5">
                            Property Address
                          </label>
                          <Input
                            id="address"
                            name="address"
                            placeholder="123 Main St, Warren, OH 44483"
                            className="h-11"
                          />
                        </div>
                        <div>
                          <label htmlFor="message" className="block text-sm font-medium text-slate-700 mb-1.5">
                            Tell Us About Your Project
                          </label>
                          <Textarea
                            id="message"
                            name="message"
                            placeholder="Describe your septic or excavation needs, any problems you're experiencing, or questions you have..."
                            rows={5}
                            className="resize-none"
                          />
                        </div>
                        <SubmitButton />
                        <p className="text-xs text-slate-500 text-center">
                          We respond to all inquiries within 1 business day. For immediate assistance, call{" "}
                          <a href={`tel:${COMPANY.phone}`} className="text-secondary">
                            {COMPANY.phone}
                          </a>
                          .
                        </p>
                      </form>
                    </>
                  )}
                </CardContent>
              </Card>
            </div>

            {/* Sidebar */}
            <div className="space-y-6">
              <Card className="bg-white border-0 shadow-sm">
                <CardContent className="p-6 space-y-4">
                  <h3 className="font-bold text-slate-900">Contact Information</h3>
                  <div className="space-y-3">
                    <a href={`tel:${COMPANY.phone}`} className="flex items-start gap-3 text-slate-700 hover:text-secondary transition-colors">
                      <Phone className="w-5 h-5 text-secondary flex-shrink-0 mt-0.5" />
                      <div>
                        <div className="font-semibold">{COMPANY.phone}</div>
                        <div className="text-xs text-slate-500">Tap to call</div>
                      </div>
                    </a>
                    <div className="flex items-start gap-3 text-slate-700">
                      <Mail className="w-5 h-5 text-secondary flex-shrink-0 mt-0.5" />
                      <div>
                        <div>{COMPANY.email}</div>
                        <div className="text-xs text-slate-500">Email us anytime</div>
                      </div>
                    </div>
                    <div className="flex items-start gap-3 text-slate-700">
                      <MapPin className="w-5 h-5 text-secondary flex-shrink-0 mt-0.5" />
                      <div>
                        <div>{COMPANY.address}</div>
                        <div className="text-xs text-slate-500">
                          {COMPANY.city}, {COMPANY.state} {COMPANY.zip}
                        </div>
                      </div>
                    </div>
                    <div className="flex items-start gap-3 text-slate-700">
                      <Clock className="w-5 h-5 text-secondary flex-shrink-0 mt-0.5" />
                      <div>
                        <div className="font-semibold">Mon – Sat</div>
                        <div className="text-xs text-slate-500">7:00 AM – 7:00 PM</div>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>

              <Card className="bg-primary border-0 text-white">
                <CardContent className="p-6 text-center">
                  <h3 className="font-bold text-lg mb-2">Emergency Service?</h3>
                  <p className="text-blue-100 text-sm mb-4">
                    A septic emergency doesn&apos;t wait. Call us now for urgent septic and excavation issues.
                  </p>
                  <a href={`tel:${COMPANY.phone}`} className="block">
                    <Button className="w-full gap-2 bg-white text-primary hover:bg-secondary/10 font-bold">
                      <Phone className="w-4 h-4" />
                      {COMPANY.phone}
                    </Button>
                  </a>
                </CardContent>
              </Card>

              <Card className="bg-white border-0 shadow-sm">
                <CardContent className="p-6">
                  <div className="flex items-center gap-3 mb-3">
                    <div className="w-12 h-12 rounded-full bg-amber-100 flex items-center justify-center">
                      <span className="text-xl font-bold text-amber-600">A+</span>
                    </div>
                    <div>
                      <div className="font-bold text-slate-900 text-sm">BBB Accredited</div>
                      <div className="text-xs text-slate-500">Since August 2025</div>
                    </div>
                  </div>
                  <p className="text-xs text-slate-500 leading-relaxed">
                    Committed to BBB Standards for Trust. Verify our accreditation at bbb.org.
                  </p>
                </CardContent>
              </Card>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Bridge */}
      <section className="bg-primary py-8">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white">
          <h2 className="text-xl font-bold mb-1">Located in Warren, Ohio</h2>
          <p className="text-blue-100 text-sm mb-4">
            {COMPANY.address}, {COMPANY.city}, {COMPANY.state} {COMPANY.zip} — Serving all of Trumbull County
          </p>
          <a href={`tel:${COMPANY.phone}`} className="inline-block">
            <Button size="lg" className="gap-2 bg-white text-primary hover:bg-secondary/10 font-bold text-base px-8">
              <Phone className="w-5 h-5" />
              Call {COMPANY.phone}
            </Button>
          </a>
        </div>
      </section>

      {/* Google Maps Embed */}
      <section className="w-full h-64 md:h-80">
        <iframe
          src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2988.0!2d-80.8184!3d41.2376!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x0!2zNDHCsDE0JzE2LjIiTiA4MMKwNDknMDYuNSJX!5e0!3m2!1sen!2sus!4v1700000000000!5m2!1sen!2sus"
          width="100%"
          height="100%"
          style={{ border: 0 }}
          allowFullScreen
          loading="lazy"
          referrerPolicy="no-referrer-when-downgrade"
          title="S&P Septic and Excavating Location"
        />
      </section>
    </>
  );
}

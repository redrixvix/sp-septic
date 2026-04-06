"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Textarea } from "@/components/ui/textarea";
import { Input } from "@/components/ui/input";
import { CheckCircle, Phone, Mail, MapPin, Clock, AlertCircle } from "lucide-react";
import { COMPANY } from "@/lib/data";
import { submitContactForm } from "@/app/actions";

function SubmitButton() {
  const { pending } = require("react-dom").useFormStatus();
  return (
    <Button
      type="submit"
      size="lg"
      disabled={pending}
      className="w-full sm:w-auto bg-primary hover:bg-primary/90 font-bold text-lg disabled:opacity-60"
    >
      {pending ? "Sending..." : "Send Message"}
    </Button>
  );
}

export function ContactForm() {
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
    <Card className="bg-white border-0 shadow-sm">
      <CardContent className="p-6 md:p-8">
        {submitted ? (
          <div className="text-center py-12" role="status" aria-live="polite">
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
            <Button variant="outline" onClick={() => setSubmitted(false)}>
              Send Another Message
            </Button>
          </div>
        ) : (
          <>
            <h2 className="text-2xl font-bold text-slate-900 mb-6">Request a Free Estimate</h2>
            {error && (
              <div className="mb-4 p-3 rounded-lg bg-red-50 border border-red-200 flex items-start gap-2 text-sm text-red-700" role="alert" aria-live="assertive">
                <AlertCircle className="w-4 h-4 mt-0.5 flex-shrink-0" />
                <span>{error}</span>
              </div>
            )}
            <form onSubmit={handleSubmit} className="space-y-5" noValidate>
              <div className="grid sm:grid-cols-2 gap-4">
                <div>
                  <label htmlFor="name" className="block text-sm font-medium text-slate-700 mb-1">
                    Full Name <span className="text-red-500">*</span>
                  </label>
                  <Input id="name" name="name" type="text" placeholder="John Smith" required className="w-full" autoComplete="name" />
                </div>
                <div>
                  <label htmlFor="phone" className="block text-sm font-medium text-slate-700 mb-1">
                    Phone Number <span className="text-red-500">*</span>
                  </label>
                  <Input id="phone" name="phone" type="tel" placeholder="(330) 555-0000" required className="w-full" autoComplete="tel" />
                </div>
              </div>
              <div>
                <label htmlFor="email" className="block text-sm font-medium text-slate-700 mb-1">
                  Email Address
                </label>
                <Input id="email" name="email" type="email" placeholder="you@example.com" className="w-full" autoComplete="email" />
              </div>
              <div>
                <label htmlFor="service" className="block text-sm font-medium text-slate-700 mb-1">
                  Service Needed <span className="text-red-500">*</span>
                </label>
                <select
                  id="service"
                  name="service"
                  required
                  className="w-full h-10 rounded-lg border border-input bg-white px-3 text-sm text-slate-900 ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary focus-visible:ring-offset-2"
                >
                  <option value="">Select a service...</option>
                  <option value="septic-installation">Septic System Installation</option>
                  <option value="septic-repair">Septic System Repair</option>
                  <option value="excavation">Excavation Services</option>
                  <option value="pumping">Septic Pumping &amp; Cleaning</option>
                  <option value="inspection">Camera Inspection</option>
                  <option value="perk-testing">Perk &amp; Leach Field Services</option>
                  <option value="other">Other</option>
                </select>
              </div>
              <div>
                <label htmlFor="message" className="block text-sm font-medium text-slate-700 mb-1">
                  Tell Us About Your Project <span className="text-red-500">*</span>
                </label>
                <Textarea id="message" name="message" rows={4} placeholder="Describe your septic or excavation needs..." required className="w-full resize-none" />
              </div>
              <div className="flex flex-col sm:flex-row gap-3 items-start sm:items-center pt-2">
                <SubmitButton />
                <p className="text-xs text-slate-500">We respond within 1 business day. Fields marked * are required.</p>
              </div>
            </form>
          </>
        )}
      </CardContent>
    </Card>
  );
}

export function ContactPageClient() {
  return (
    <>
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-primary text-white border-0">Contact Us</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">Get a Free Estimate</h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            Ready to solve your septic or excavation needs? Call us or fill out the form below — we respond within one business day.
          </p>
        </div>
      </section>

      <section className="py-10 md:py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-3 gap-8">
            <div className="lg:col-span-2">
              <ContactForm />
            </div>
            <div className="space-y-6">
              <Card className="bg-white border-0 shadow-sm">
                <CardContent className="p-6 space-y-4">
                  <h3 className="font-bold text-slate-900 text-lg">Contact Information</h3>
                  <div className="space-y-3">
                    <a href={`tel:${COMPANY.phone}`} className="flex items-start gap-3 text-sm hover:text-primary transition-colors">
                      <Phone className="w-5 h-5 text-primary flex-shrink-0 mt-0.5" />
                      <div>
                        <div className="font-semibold text-slate-900">{COMPANY.phone}</div>
                        <div className="text-slate-500">Mon–Sat, 7AM–7PM</div>
                      </div>
                    </a>
                    <a href={`mailto:${COMPANY.email}`} className="flex items-start gap-3 text-sm hover:text-primary transition-colors">
                      <Mail className="w-5 h-5 text-primary flex-shrink-0 mt-0.5" />
                      <div>
                        <div className="font-semibold text-slate-900">{COMPANY.email}</div>
                        <div className="text-slate-500">We reply within 1 business day</div>
                      </div>
                    </a>
                    <div className="flex items-start gap-3 text-sm">
                      <MapPin className="w-5 h-5 text-primary flex-shrink-0 mt-0.5" />
                      <div>
                        <div className="font-semibold text-slate-900">{COMPANY.address}</div>
                        <div className="text-slate-500">{COMPANY.city}, {COMPANY.state} {COMPANY.zip}</div>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
              <Card className="bg-white border-0 shadow-sm">
                <CardContent className="p-6 space-y-3">
                  <div className="flex items-center gap-2">
                    <Clock className="w-5 h-5 text-primary" />
                    <h3 className="font-bold text-slate-900">Business Hours</h3>
                  </div>
                  <div className="space-y-1 text-sm">
                    <div className="flex justify-between text-slate-600">
                      <span>Monday – Friday</span><span className="font-medium text-slate-900">7AM – 7PM</span>
                    </div>
                    <div className="flex justify-between text-slate-600">
                      <span>Saturday</span><span className="font-medium text-slate-900">7AM – 7PM</span>
                    </div>
                    <div className="flex justify-between text-slate-600">
                      <span>Sunday</span><span className="text-red-500 font-medium">Closed — Emergency Only</span>
                    </div>
                  </div>
                  <p className="text-xs text-slate-500 pt-2">
                    24/7 emergency septic service available. Call {COMPANY.phone} for emergencies.
                  </p>
                </CardContent>
              </Card>
              <div className="bg-primary text-white rounded-xl p-6">
                <h3 className="font-bold text-lg mb-2">Need Urgent Help?</h3>
                <p className="text-blue-100 text-sm mb-4">
                  Septic emergencies don&apos;t wait for business hours. Call us immediately.
                </p>
                <a href={`tel:${COMPANY.phone}`} className="flex items-center gap-2 font-bold text-lg hover:text-blue-200 transition-colors">
                  <Phone className="w-5 h-5" />
                  {COMPANY.phone}
                </a>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="h-80 w-full">
        <iframe
          src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2999.8637432!2d-80.8184!3d41.2376!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x88338f4!2s2900+Elm+Rd+NE%2C+Warren%2C+OH+44483!5e0!3m2!1sen!2sus!4v1700000000000"
          width="100%"
          height="100%"
          style={{ border: 0 }}
          allowFullScreen
          loading="lazy"
          referrerPolicy="no-referrer-when-downgrade"
          title="S&P Septic and Excavating location map"
        />
      </section>
    </>
  );
}
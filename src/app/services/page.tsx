import type { Metadata } from "next";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Badge } from "@/components/ui/badge";
import {
  Phone,
  CheckCircle,
  ArrowRight,
  Truck,
  Home,
  Clock,
  FileCheck,
  Shield,
  MapPin,
} from "lucide-react";
import { COMPANY, SERVICES } from "@/lib/data";
import { BreadcrumbJsonLd } from "@/components/breadcrumb-jsonld";

export const metadata: Metadata = {
  title: "Services",
  description:
    "Septic system installation, repair, pumping, excavation, perk testing, and camera inspections. Full-service contractor in Warren, Ohio.",
};

const SERVICE_PROCESS = [
  {
    step: "01",
    title: "Free Consultation",
    desc: "Call or fill out our form. We'll discuss your needs and schedule an on-site assessment — no obligation.",
    icon: Phone,
  },
  {
    step: "02",
    title: "Site Assessment",
    desc: "We evaluate soil conditions, property layout, and system requirements to design the right solution.",
    icon: FileCheck,
  },
  {
    step: "03",
    title: "Permit & Design",
    desc: "We handle all Ohio Health Department permit applications and finalize your custom system design.",
    icon: Home,
  },
  {
    step: "04",
    title: "Professional Installation",
    desc: "Our crew completes the job with quality materials and proven methods, then handles all inspections.",
    icon: Truck,
  },
];

const FAQS = [
  {
    q: "How often should I pump my septic tank?",
    a: "Most residential tanks need pumping every 3–5 years, depending on household size, tank capacity, and water usage. We can assess your system and set up a maintenance schedule that works for you.",
  },
  {
    q: "What are signs my septic system is failing?",
    a: "Watch for: slow drains throughout the house, gurgling sounds from pipes, sewage odors inside or outside, wet spots or bright green patches over the drain field, and unusually lush grass growth. Call us right away if you notice any of these.",
  },
  {
    q: "How long does a new septic system installation take?",
    a: "A typical new installation takes 2–5 days depending on soil conditions, system size, and permitting. We manage everything from permits to final inspection — you don't lift a finger.",
  },
  {
    q: "Do I need a permit for septic work in Ohio?",
    a: "Yes. All new septic installations and major repairs in Ohio require permits from your local health department (Trumbull County Health District for our area). We handle all permit applications as part of our service.",
  },
  {
    q: "What's the difference between a conventional and aerobic septic system?",
    a: "Conventional systems rely on natural soil filtration and gravity. Aerobic systems inject air to accelerate treatment and are used where soil conditions are poor or groundwater is close to the surface. We help you choose the right system for your property.",
  },
  {
    q: "Can you pump my septic tank?",
    a: "Absolutely. Regular pumping is the single most important maintenance you can do for your system. We offer septic pumping with transparent pricing and no hidden fees.",
  },
  {
    q: "Do you do excavation for things other than septic?",
    a: "Yes. We handle all types of excavation: driveway grading and installation, utility trenching, pond excavation, site clearing, and more. If it involves moving dirt, we can handle it.",
  },
  {
    q: "What forms of payment do you accept?",
    a: "We accept cash, check, and all major credit cards. Payment is due upon completion of work — we provide clear upfront pricing before any work begins.",
  },
];

export default function ServicesPage() {
  return (
    <>
      <BreadcrumbJsonLd items={[{ name: "Home", url: "https://spseptic.com" }, { name: "Services", url: "https://spseptic.com/services" }]} />
      {/* Page Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-primary text-white border-0">Our Services</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Septic &amp; Excavation Services
          </h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            Complete septic and excavation solutions for residential and commercial properties in Warren, Ohio and all of Trumbull County. Licensed, insured, and BBB Accredited.
          </p>
        </div>
      </section>

      {/* Services Detail — alternating layout */}
      <section className="py-16 md:py-24 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="space-y-16">
            {SERVICES.map((service, i) => (
              <div
                key={service.title}
                className="grid lg:grid-cols-2 gap-10 items-center"
              >
                <div className={i % 2 === 1 ? "lg:order-2" : ""}>
                  <Badge className="mb-3 bg-secondary/10 text-primary border-0 text-xs">
                    {["Residential & Commercial", "Fast Response", "Professional Equipment", "Preventive Care", "Soil & Drainage", "Diagnostic Technology"][i]}
                  </Badge>
                  <h2 className="text-2xl md:text-3xl font-bold text-slate-900 mb-4">
                    {service.title}
                  </h2>
                  <p className="text-slate-600 text-lg leading-relaxed mb-6">
                    {service.description}
                  </p>
                  <ul className="grid grid-cols-2 gap-2 mb-6">
                    {service.features.map((f) => (
                      <li key={f} className="flex items-center gap-2 text-sm text-slate-600">
                        <div className="w-4 h-4 rounded-full bg-secondary/10 flex items-center justify-center flex-shrink-0">
                          <CheckCircle className="w-3 h-3 text-secondary" />
                        </div>
                        {f}
                      </li>
                    ))}
                  </ul>
                  <Link href="/contact">
                    <Button className="gap-2 bg-primary hover:bg-primary/90 font-semibold">
                      Get a Free Estimate <ArrowRight className="w-4 h-4" />
                    </Button>
                  </Link>
                </div>
                {/* Visual panel */}
                <div className={`bg-gradient-to-br from-slate-100 to-slate-50 rounded-2xl aspect-video flex items-center justify-center ${i % 2 === 1 ? "lg:order-1" : ""}`}>
                  <div className="text-center">
                    <div className="text-6xl font-black text-slate-200 mb-2">
                      {service.title.charAt(0)}
                    </div>
                    <div className="text-sm font-semibold text-slate-400">{service.title}</div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Process */}
      <section className="py-16 md:py-24 bg-slate-900 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <Badge className="mb-3 bg-secondary/20 text-blue-300 border-primary/30 border-0 text-xs">Our Process</Badge>
            <h2 className="text-3xl md:text-4xl font-bold mb-3">
              How We Work With You
            </h2>
            <p className="text-slate-400 text-lg max-w-2xl mx-auto">
              From your first call to final inspection — simple, transparent, and stress-free.
            </p>
          </div>
          <div className="grid md:grid-cols-4 gap-6">
            {SERVICE_PROCESS.map((p, i) => (
              <div key={p.step} className="relative">
                {i < SERVICE_PROCESS.length - 1 && (
                  <div className="hidden md:block absolute top-8 left-full w-full h-px bg-slate-700 -translate-x-1/2" />
                )}
                <Card className="bg-slate-800 border-slate-700">
                  <CardContent className="p-6 text-center">
                    <div className="w-12 h-12 rounded-full bg-primary text-white flex items-center justify-center text-xl font-bold mx-auto mb-4">
                      {p.step}
                    </div>
                    <div className="w-10 h-10 rounded-lg bg-slate-700 flex items-center justify-center mx-auto mb-3">
                      <p.icon className="w-5 h-5 text-primary" />
                    </div>
                    <h3 className="font-bold text-white mb-2">{p.title}</h3>
                    <p className="text-slate-400 text-sm leading-relaxed">{p.desc}</p>
                  </CardContent>
                </Card>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Pricing transparency */}
      <section className="py-16 bg-white border-t">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-3 gap-6">
            {[
              {
                icon: FileCheck,
                title: "Free Estimates",
                desc: "On-site assessment and written quote before any work begins. Know exactly what you're paying before we touch a thing.",
                color: "secondary",
              },
              {
                icon: Shield,
                title: "No Surprise Pricing",
                desc: "The quote we give is the price you pay. We don't add hidden fees or unexpected charges after the job starts.",
                color: "secondary",
              },
              {
                icon: Clock,
                title: "Fast Response",
                desc: "Need us now? For urgent septic issues, we prioritize emergency calls and aim to respond same-day.",
                color: "secondary",
              },
            ].map((item) => (
              <Card key={item.title} className="bg-slate-50 border-0">
                <CardContent className="p-6">
                  <div className={`w-12 h-12 rounded-xl bg-secondary/10 flex items-center justify-center text-secondary mb-4`}>
                    <item.icon className="w-6 h-6" />
                  </div>
                  <h3 className="font-bold text-slate-900 mb-2">{item.title}</h3>
                  <p className="text-sm text-slate-600 leading-relaxed">{item.desc}</p>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* FAQ */}
      <section className="py-16 md:py-24 bg-slate-50">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <Badge className="mb-3 bg-secondary/10 text-primary border-0 text-xs">Common Questions</Badge>
            <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-3">
              Questions Homeowners Ask Us
            </h2>
          </div>
          <Accordion className="w-full bg-white rounded-xl shadow-sm overflow-hidden">
            {FAQS.map((faq) => (
              <AccordionItem key={faq.q} value={faq.q}>
                <AccordionTrigger className="text-left font-semibold text-slate-900 hover:text-secondary px-6 py-4 hover:bg-slate-50 transition-colors">
                  {faq.q}
                </AccordionTrigger>
                <AccordionContent className="px-6 pb-5 text-slate-600 leading-relaxed">
                  {faq.a}
                </AccordionContent>
              </AccordionItem>
            ))}
          </Accordion>
        </div>
      </section>

      {/* CTA */}
      <section className="bg-primary py-14">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white">
          <h2 className="text-3xl font-bold mb-4">Ready to Get Started?</h2>
          <p className="text-blue-100 text-lg mb-8">Call now for a free estimate. No obligation, no pressure — just honest advice.</p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <a href={`tel:${COMPANY.phone}`}>
              <Button size="lg" className="gap-2 bg-white text-primary hover:bg-secondary/10 font-bold text-lg px-8">
                <Phone className="w-5 h-5" />
                Call {COMPANY.phone}
              </Button>
            </a>
            <Link href="/contact">
              <Button size="lg" variant="outline" className="text-lg px-8 border-white text-white hover:bg-white/10 font-bold">
                Request Free Estimate
              </Button>
            </Link>
          </div>
        </div>
      </section>
    </>
  );
}

"use client";

import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import {
  Phone,
  Shield,
  CheckCircle,
  Star,
  ArrowRight,
  Clock,
  MapPin,
  ChevronRight,
  Truck,
  ThumbsUp,
  FileCheck,
  Users,
  Calendar,
  Award,
} from "lucide-react";
import { motion, useScroll, useTransform, useInView } from "framer-motion";
import { useRef, useEffect, useState } from "react";
import { COMPANY, SERVICES, WHY_CHOOSE_US, TESTIMONIALS, SERVICE_AREAS } from "@/lib/data";
import { FadeIn } from "@/components/animations/FadeIn";
import { StaggerContainer, StaggerItem } from "@/components/animations/StaggerContainer";
import { CountUp } from "@/components/animations/CountUp";

// 4-step how-it-works strip
const PROCESS_STEPS = [
  { icon: Phone, label: "Call or Fill Form", desc: "Get in touch — we respond within 1 business day" },
  { icon: FileCheck, label: "Free Assessment", desc: "On-site evaluation and honest quote, no obligation" },
  { icon: Truck, label: "We Get It Done", desc: "Professional installation or repair, on schedule" },
  { icon: ThumbsUp, label: "Done Right", desc: "Final inspection and your complete satisfaction guaranteed" },
];

// ── HERO SECTION ──────────────────────────────────────────────────────────────
function HeroSection() {
  const ref = useRef(null);
  const { scrollYProgress } = useScroll({
    target: ref,
    offset: ["start start", "end start"],
  });
  const y = useTransform(scrollYProgress, [0, 1], ["0%", "30%"]);
  const opacity = useTransform(scrollYProgress, [0, 0.8], [1, 0]);

  return (
    <section ref={ref} className="relative bg-slate-900 text-white overflow-hidden">
      {/* Animated gradient background */}
      <div
        className="absolute inset-0 animate-gradient-shift"
        style={{
          background: "linear-gradient(135deg, #0f2318 0%, #1B4332 35%, #2D5A45 60%, #1B4332 100%)",
          backgroundSize: "200% 200%",
        }}
      />
      {/* SVG pattern overlay */}
      <div
        className="absolute inset-0 opacity-5"
        style={{
          backgroundImage: `url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E")`,
        }}
      />
      {/* Decorative floating element */}
      <motion.div
        className="absolute right-8 top-1/2 -translate-y-1/2 w-64 h-64 rounded-full opacity-10 hidden lg:block"
        style={{
          background: "radial-gradient(circle, #D4A843, transparent 70%)",
        }}
        animate={{ scale: [1, 1.15, 1], opacity: [0.08, 0.14, 0.08] }}
        transition={{ duration: 6, repeat: Infinity, ease: "easeInOut" }}
      />
      {/* Gold accent line */}
      <div className="absolute bottom-0 left-0 right-0 h-1 bg-gradient-to-r from-transparent via-amber-500/30 to-transparent" />

      <motion.div style={{ y, opacity }} className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 md:py-28">
        <div className="max-w-3xl">
          {/* Animated badges */}
          <motion.div
            className="flex flex-wrap gap-2 mb-5"
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.2 }}
          >
            <Badge className="text-blue-200 border-secondary/30 hover:text-secondary/20 text-xs font-medium backdrop-blur-sm bg-white/10 border-secondary/20">
              BBB Accredited — A+ Rating
            </Badge>
            <Badge className="bg-white/10 text-white border-white/20 hover:bg-white/10 text-xs backdrop-blur-sm">
              Family Owned &amp; Operated
            </Badge>
            <Badge className="bg-white/10 text-white border-white/20 hover:bg-white/10 text-xs backdrop-blur-sm">
              Licensed &amp; Insured in Ohio
            </Badge>
          </motion.div>

          {/* Staggered headline */}
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-extrabold leading-tight tracking-tight mb-6">
            <motion.span
              initial={{ opacity: 0, y: 40 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.4, ease: [0.25, 0.46, 0.45, 0.94] }}
            >
              Septic &amp; Excavating{" "}
            </motion.span>
            <motion.span
              initial={{ opacity: 0, y: 40 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.55, ease: [0.25, 0.46, 0.45, 0.94] }}
              className="text-primary"
            >
              Done Right
            </motion.span>
            <motion.br
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ duration: 0.3, delay: 0.7 }}
            />
            <motion.span
              initial={{ opacity: 0, y: 40 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.75, ease: [0.25, 0.46, 0.45, 0.94] }}
            >
              {" "}Every Time
            </motion.span>
          </h1>

          <motion.p
            className="text-lg md:text-xl text-slate-300 leading-relaxed mb-8 max-w-2xl"
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.9, ease: [0.25, 0.46, 0.45, 0.94] }}
          >
            Family-owned and BBB Accredited. We install, repair, and maintain septic systems — and handle excavation jobs of all sizes — throughout Warren, Ohio and Trumbull County.
          </motion.p>

          {/* Animated CTAs */}
          <motion.div
            className="flex flex-col sm:flex-row gap-4 mb-10"
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 1.05, ease: [0.25, 0.46, 0.45, 0.94] }}
          >
            <a href={`tel:${COMPANY.phone}`} className="inline-block">
              <Button size="lg" className="gap-2 text-base w-full sm:w-auto bg-primary hover:bg-primary/90 font-bold shadow-lg shadow-primary/20">
                <Phone className="w-5 h-5" />
                Call {COMPANY.phone}
              </Button>
            </a>
            <Link href="/contact">
              <Button size="lg" className="gap-2 text-base w-full sm:w-auto bg-secondary text-white hover:bg-slate-100 font-bold shadow-lg">
                Get Free Estimate <ArrowRight className="w-4 h-4" />
              </Button>
            </Link>
          </motion.div>

          {/* Feature checkmarks */}
          <motion.div
            className="grid grid-cols-2 sm:grid-cols-4 gap-4 text-sm"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.6, delay: 1.2 }}
          >
            {[
              { icon: CheckCircle, text: "Free Estimates" },
              { icon: CheckCircle, text: "Same-Day Service" },
              { icon: CheckCircle, text: "Licensed & Insured" },
              { icon: CheckCircle, text: "Work Guaranteed" },
            ].map(({ icon: Icon, text }) => (
              <div key={text} className="flex items-center gap-2">
                <Icon className="w-4 h-4 text-primary flex-shrink-0" />
                <span className="text-slate-300">{text}</span>
              </div>
            ))}
          </motion.div>
        </div>
      </motion.div>
    </section>
  );
}

// ── PROCESS STRIP ─────────────────────────────────────────────────────────────
function ProcessStrip() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-50px" });

  return (
    <section ref={ref} className="bg-primary py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-2 lg:grid-cols-4 gap-6">
          {PROCESS_STEPS.map((step, i) => (
            <motion.div
              key={step.label}
              initial={{ opacity: 0, x: -20 }}
              animate={isInView ? { opacity: 1, x: 0 } : {}}
              transition={{ duration: 0.5, delay: i * 0.12, ease: [0.25, 0.46, 0.45, 0.94] }}
              className="flex items-start gap-3"
            >
              <div className="w-9 h-9 rounded-full bg-white/20 flex items-center justify-center flex-shrink-0">
                <step.icon className="w-4 h-4 text-white" />
              </div>
              <div>
                <div className="text-white font-bold text-sm">{step.label}</div>
                <div className="text-blue-100 text-xs mt-0.5 leading-snug">{step.desc}</div>
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}

// ── TRUST NUMBERS ─────────────────────────────────────────────────────────────
function TrustStats() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-50px" });

  const stats: Array<{
    label: string;
    sub: string;
    display: string | number;
    countTarget?: number;
    suffix?: string;
  }> = [
    { label: "BBB Rating", sub: "Accredited since 2025", display: "A+" },
    { label: "Projects Done", sub: "All sizes, all types", display: "", countTarget: 50, suffix: "+" },
    { label: "Satisfaction", sub: "Zero comeback calls", display: "", countTarget: 100, suffix: "%" },
    { label: "Est.", sub: "Family owned & operated", display: "2021" },
  ];

  return (
    <section ref={ref} className="bg-white py-10 border-b">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex flex-wrap justify-center gap-12 md:gap-20 text-center">
          {stats.map((stat, i) => (
            <motion.div
              key={stat.label}
              initial={{ opacity: 0, y: 30 }}
              animate={isInView ? { opacity: 1, y: 0 } : {}}
              transition={{ duration: 0.5, delay: i * 0.15, ease: [0.25, 0.46, 0.45, 0.94] }}
            >
              <div className="text-3xl md:text-4xl font-extrabold text-slate-900">
                {stat.display}
                {stat.countTarget !== undefined && (
                  <CountUp end={stat.countTarget} suffix={stat.suffix || ""} duration={2} />
                )}
              </div>
              <div className="text-sm font-semibold text-slate-700">{stat.label}</div>
              <div className="text-xs text-slate-400">{stat.sub}</div>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}

// ── SERVICES ──────────────────────────────────────────────────────────────────
function ServicesSection() {
  return (
    <section className="py-16 md:py-24 bg-slate-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <FadeIn>
          <div className="text-center mb-12">
            <Badge className="mb-3 bg-primary/10 text-primary border-0 text-xs">What We Do</Badge>
            <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-4">
              Full-Service Septic &amp; Excavation
            </h2>
            <p className="text-slate-600 max-w-2xl mx-auto text-lg">
              From new system design to emergency repairs — and everything in between. No job too big, no job too small.
            </p>
          </div>
        </FadeIn>

        <StaggerContainer className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6" staggerDelay={0.08}>
          {SERVICES.map((service) => (
            <StaggerItem key={service.title}>
              <Card
                className="group hover:shadow-xl transition-all duration-250 border-slate-200 hover:border-primary bg-white hover:-translate-y-1"
                style={{
                  boxShadow: "0 4px 6px -1px rgb(0 0 0 / 0.05)",
                  transition: "all 0.25s ease, border-color 0.25s ease",
                }}
              >
                <CardContent className="p-6">
                  <div
                    className="w-12 h-12 rounded-xl bg-secondary/10 flex items-center justify-center text-secondary mb-4 group-hover:bg-primary group-hover:text-white transition-colors duration-200"
                    style={{ border: "1px solid transparent" }}
                  />
                  <h3 className="font-bold text-lg text-slate-900 mb-2">{service.title}</h3>
                  <p className="text-sm text-slate-600 mb-4 leading-relaxed">{service.description}</p>
                  <ul className="space-y-1.5">
                    {service.features.map((f) => (
                      <li key={f} className="flex items-center gap-2 text-xs text-slate-500">
                        <svg className="w-3 h-3 text-primary flex-shrink-0" fill="currentColor" viewBox="0 0 16 16">
                          <path d="M13.78 4.22a.75.75 0 0 1 0 1.06l-7.25 7.25a.75.75 0 0 1-1.06 0L2.22 9.28a.75.75 0 0 1 1.06-1.06L6 10.94l6.72-6.72a.75.75 0 0 1 1.06 0z" />
                        </svg>
                        {f}
                      </li>
                    ))}
                  </ul>
                </CardContent>
              </Card>
            </StaggerItem>
          ))}
        </StaggerContainer>

        <FadeIn delay={0.3}>
          <div className="text-center mt-10">
            <Link href="/services">
              <Button size="lg" className="gap-2 bg-primary hover:bg-primary/90 font-bold">
                View All Services <ChevronRight className="w-4 h-4" />
              </Button>
            </Link>
          </div>
        </FadeIn>
      </div>
    </section>
  );
}

// ── WHY CHOOSE US ─────────────────────────────────────────────────────────────
function WhyChooseUs() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-50px" });

  return (
    <section ref={ref} className="py-16 md:py-24 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid lg:grid-cols-2 gap-12 items-start">
          {/* Left column */}
          <div>
            <FadeIn>
              <Badge className="mb-3 bg-primary/10 text-primary border-0 text-xs">Why Choose Us</Badge>
              <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-4">
                Warren, Ohio Trusts S&amp;P
              </h2>
              <p className="text-slate-600 text-lg leading-relaxed mb-8">
                When your septic fails or you need excavation work, you can&apos;t afford to wait — and you can&apos;t afford a company that does sloppy work. Here&apos;s why neighbors choose us.
              </p>
            </FadeIn>
            <div className="space-y-5">
              {WHY_CHOOSE_US.map((item, i) => (
                <motion.div
                  key={item.title}
                  initial={{ opacity: 0, x: i % 2 === 0 ? -40 : 40 }}
                  animate={isInView ? { opacity: 1, x: 0 } : {}}
                  transition={{ duration: 0.6, delay: i * 0.12, ease: [0.25, 0.46, 0.45, 0.94] }}
                  className="flex gap-4"
                >
                  <div className="w-10 h-10 rounded-lg bg-secondary/10 flex items-center justify-center text-secondary flex-shrink-0">
                    <WhyIcon name={item.icon} />
                  </div>
                  <div>
                    <h3 className="font-semibold text-slate-900 mb-0.5">{item.title}</h3>
                    <p className="text-sm text-slate-600">{item.description}</p>
                  </div>
                </motion.div>
              ))}
            </div>
          </div>

          {/* CTA Card — sticky-ish */}
          <div className="lg:sticky lg:top-24">
            <motion.div
              className="bg-slate-900 rounded-2xl p-8 text-white shadow-2xl"
              initial={{ opacity: 0, y: 40 }}
              animate={isInView ? { opacity: 1, y: 0 } : {}}
              transition={{ duration: 0.6, delay: 0.4, ease: [0.25, 0.46, 0.45, 0.94] }}
            >
              <div className="flex items-center gap-3 mb-2">
                <motion.div
                  className="w-14 h-14 rounded-xl bg-amber-500 flex items-center justify-center text-2xl font-black text-white"
                  animate={{ scale: [1, 1.03, 1] }}
                  transition={{ duration: 2.5, repeat: Infinity, ease: "easeInOut" }}
                >
                  A+
                </motion.div>
                <div>
                  <div className="font-bold text-lg">BBB Accredited</div>
                  <div className="text-slate-400 text-sm">Since August 2025</div>
                </div>
              </div>
              <p className="text-slate-300 text-sm mb-6 mt-4 leading-relaxed">
                We&apos;ve earned trust the hard way — one satisfied customer at a time. BBB Accredited with a perfect A+ rating.
              </p>
              <hr className="border-slate-700 mb-6" />
              <div className="space-y-3 mb-8">
                {[
                  "Free on-site estimates",
                  "Upfront, honest pricing",
                  "All work warranted",
                  "Licensed in Ohio",
                ].map((item) => (
                  <div key={item} className="flex items-center gap-3 text-sm text-slate-300">
                    <CheckCircle className="w-4 h-4 text-primary" />
                    {item}
                  </div>
                ))}
              </div>
              <a href={`tel:${COMPANY.phone}`} className="block mb-3">
                <motion.div
                  className="w-full"
                  animate={{ boxShadow: ["0 0 0 0 rgba(212,168,67,0)", "0 0 0 8px rgba(212,168,67,0)", "0 0 0 0 rgba(212,168,67,0)"] }}
                  transition={{ duration: 2.5, repeat: Infinity, ease: "easeInOut" }}
                >
                  <Button size="lg" className="w-full gap-2 bg-primary hover:bg-primary/90 font-bold text-lg">
                    <Phone className="w-5 h-5" />
                    {COMPANY.phone}
                  </Button>
                </motion.div>
              </a>
              <div className="flex items-center justify-center gap-2 text-sm text-slate-400">
                <Clock className="w-4 h-4" />
                <span>Mon–Sat, 7AM–7PM</span>
              </div>
              <div className="mt-5 pt-5 border-t border-slate-700 flex items-start gap-3">
                <MapPin className="w-4 h-4 text-primary flex-shrink-0 mt-0.5" />
                <span className="text-xs text-slate-400">
                  {COMPANY.address}, {COMPANY.city}, {COMPANY.state} {COMPANY.zip}
                </span>
              </div>
            </motion.div>
          </div>
        </div>
      </div>
    </section>
  );
}

// ── GALLERY TEASER ─────────────────────────────────────────────────────────────
function GalleryTeaser() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-50px" });

  return (
    <section ref={ref} className="py-16 md:py-20 bg-slate-900 text-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <FadeIn>
          <div className="text-center mb-10">
            <h2 className="text-3xl md:text-4xl font-bold mb-3">Our Work Speaks for Itself</h2>
            <p className="text-slate-400 text-lg">See septic installations, excavations, and repairs throughout Trumbull County.</p>
          </div>
        </FadeIn>

        <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
          {Array.from({ length: 8 }).map((_, i) => (
            <motion.div
              key={i}
              initial={{ opacity: 0, scale: 0.95 }}
              animate={isInView ? { opacity: 1, scale: 1 } : {}}
              transition={{ duration: 0.5, delay: i * 0.07, ease: [0.25, 0.46, 0.45, 0.94] }}
              whileHover={{ scale: 1.05, zIndex: 10 }}
              className="aspect-square rounded-xl bg-slate-800 border border-slate-700 flex flex-col items-center justify-center text-slate-600 hover:bg-slate-700 transition-colors cursor-pointer overflow-hidden"
            >
              <Truck className="w-8 h-8 mb-2 opacity-40" />
              <span className="text-xs opacity-60">Project {i + 1}</span>
            </motion.div>
          ))}
        </div>

        <FadeIn delay={0.3}>
          <div className="text-center mt-8">
            <Link href="/gallery">
              <Button size="lg" variant="outline" className="border-primary text-primary hover:bg-primary/5 hover:text-blue-200 font-bold">
                View Full Gallery <ArrowRight className="w-4 h-4 ml-2" />
              </Button>
            </Link>
          </div>
        </FadeIn>
      </div>
    </section>
  );
}

// ── TESTIMONIALS ───────────────────────────────────────────────────────────────
function TestimonialsSection() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-50px" });

  return (
    <section ref={ref} className="py-16 md:py-24 bg-slate-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <FadeIn>
          <div className="text-center mb-12">
            <Badge className="mb-3 bg-amber-100 text-primary border-0 text-xs">Customer Reviews</Badge>
            <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-3">
              What Neighbors Are Saying
            </h2>
            <div className="flex items-center justify-center gap-1">
              {Array.from({ length: 5 }).map((_, i) => (
                <Star key={i} className="w-5 h-5 fill-primary text-primary" />
              ))}
              <span className="text-sm text-slate-500 ml-2">5.0 average · Ohio customers</span>
            </div>
          </div>
        </FadeIn>

        <StaggerContainer className="grid md:grid-cols-3 gap-6" staggerDelay={0.12}>
          {TESTIMONIALS.map((t) => (
            <StaggerItem key={t.name}>
              <Card className="bg-white border-0 shadow-sm relative">
                <CardContent className="p-6">
                  {/* Quote decoration */}
                  <div className="testimonial-quote absolute top-2 left-2" />
                  <div className="flex gap-1 mb-3 mt-4">
                    {Array.from({ length: t.rating }).map((_, i) => (
                      <Star key={i} className="w-4 h-4 fill-primary text-primary" />
                    ))}
                  </div>
                  <p className="text-slate-700 italic mb-4 leading-relaxed">&ldquo;{t.text}&rdquo;</p>
                  <div className="flex items-center gap-3">
                    <div className="w-9 h-9 rounded-full bg-primary/10 flex items-center justify-center text-primary font-bold text-sm">
                      {t.name.charAt(0)}
                    </div>
                    <div>
                      <div className="font-semibold text-slate-900 text-sm">{t.name}</div>
                      <div className="text-xs text-slate-500 flex items-center gap-1">
                        <MapPin className="w-3 h-3" />
                        {t.location}
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </StaggerItem>
          ))}
        </StaggerContainer>
      </div>
    </section>
  );
}

// ── SERVICE AREAS ──────────────────────────────────────────────────────────────
function ServiceAreas() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-50px" });

  return (
    <section ref={ref} className="py-16 md:py-20 bg-white border-t">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <FadeIn>
          <div className="text-center mb-8">
            <Badge className="mb-3 bg-primary/10 text-primary border-0 text-xs">Service Area</Badge>
            <h2 className="text-3xl font-bold text-slate-900 mb-3">Proudly Serving Northeast Ohio</h2>
            <p className="text-slate-600">
              From Warren to Niles, Cortland to Girard — we&apos;re your neighbors, ready to help.
            </p>
          </div>
        </FadeIn>
        <motion.div
          ref={ref}
          className="flex flex-wrap justify-center gap-3"
          initial="hidden"
          animate={isInView ? "visible" : "hidden"}
          variants={{
            hidden: {},
            visible: { transition: { staggerChildren: 0.05 } },
          }}
        >
          {SERVICE_AREAS.map((area) => (
            <motion.div
              key={area}
              variants={{
                hidden: { opacity: 0, y: 15 },
                visible: { opacity: 1, y: 0, transition: { duration: 0.4 } },
              }}
            >
              <Badge
                variant="secondary"
                className="text-sm px-4 py-1.5 bg-slate-100 text-slate-700 hover:bg-slate-100 cursor-default"
              >
                <MapPin className="w-3 h-3 mr-1.5 text-primary" />
                {area}
              </Badge>
            </motion.div>
          ))}
        </motion.div>
      </div>
    </section>
  );
}

// ── FINAL CTA ──────────────────────────────────────────────────────────────────
function FinalCTA() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-50px" });

  return (
    <section ref={ref} className="bg-primary py-16">
      <motion.div
        className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white"
        initial={{ opacity: 0, y: 30 }}
        animate={isInView ? { opacity: 1, y: 0 } : {}}
        transition={{ duration: 0.6, ease: [0.25, 0.46, 0.45, 0.94] }}
      >
        <Calendar className="w-12 h-12 mx-auto mb-4 opacity-80" />
        <h2 className="text-3xl md:text-4xl font-bold mb-4"> septic Problem? Don&apos;t Wait.</h2>
        <p className="text-blue-100 text-lg mb-8 max-w-2xl mx-auto">
          A small septic issue becomes a big, expensive emergency fast. Call S&P today — we&apos;re ready to help, starting now.
        </p>
        <div className="flex flex-col sm:flex-row gap-4 justify-center">
          <a href={`tel:${COMPANY.phone}`}>
            <Button size="lg" className="gap-2 bg-white text-primary hover:bg-secondary/10 font-bold text-lg px-10 shadow-xl">
              <Phone className="w-5 h-5" />
              Call {COMPANY.phone}
            </Button>
          </a>
          <Link href="/contact">
            <Button size="lg" variant="outline" className="text-lg px-10 border-white text-white hover:bg-white/10 font-bold">
              Request Free Estimate
            </Button>
          </Link>
        </div>
      </motion.div>
    </section>
  );
}

// ── MAIN PAGE ───────────────────────────────────────────────────────────────────
export default function HomePage() {
  return (
    <>
      <HeroSection />
      <ProcessStrip />
      <TrustStats />
      <ServicesSection />
      <WhyChooseUs />
      <GalleryTeaser />
      <TestimonialsSection />
      <ServiceAreas />
      <FinalCTA />
    </>
  );
}

// Icon helpers
function ServiceIcon({ name }: { name: string }) {
  switch (name) {
    case "Home":
      return (
        <svg className="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
          <polyline points="9 22 9 12 15 12 15 22" />
        </svg>
      );
    case "Wrench":
      return (
        <svg className="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z" />
        </svg>
      );
    case "HardHat":
      return (
        <svg className="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M2 18a1 1 0 0 0 1 1h18a1 1 0 0 0 1-1v-2a1 1 0 0 0-1-1H3a1 1 0 0 0-1 1v2z" />
          <path d="M10 15V6.5a3.5 3.5 0 0 1 7 0V15" />
          <path d="M7 15v-3a5 5 0 0 1 10 0v3" />
        </svg>
      );
    case "Droplets":
      return (
        <svg className="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M7 16.3c2.2 0 4-1.83 4-4.05 0-1.16-.57-2.26-1.71-3.19S7.29 6.75 7 5.3c-.29 1.45-1.14 2.84-2.29 3.76S3 11.1 3 12.25c0 2.22 1.8 4.05 4 4.05z" />
          <path d="M12.56 14.62c1.42.93 2.44 2.25 2.44 3.38 0 1.47-1.14 2.5-2.5 2.5H3.5" />
          <path d="M18 9.5V5.5a2 2 0 0 0-2-2h-5a2 2 0 0 0-2 2v4" />
        </svg>
      );
    case "TestTube":
      return (
        <svg className="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M14.5 2v17.5c0 1.4-1.1 2.5-2.5 2.5s-2.5-1.1-2.5-2.5V2" />
          <path d="M8.5 2h7" />
          <path d="M14.5 16h-5" />
        </svg>
      );
    case "Camera":
      return (
        <svg className="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M14.5 4h-5L7 7H4a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2h-3l-2.5-3z" />
          <circle cx="12" cy="13" r="3" />
        </svg>
      );
    default:
      return <Phone className="w-6 h-6" />;
  }
}

function WhyIcon({ name }: { name: string }) {
  const lname = name.toLowerCase().replace(" ", "");
  switch (lname) {
    case "shield":
      return <Shield className="w-5 h-5" />;
    case "users":
      return <Users className="w-5 h-5" />;
    case "badgecheck":
      return <CheckCircle className="w-5 h-5" />;
    case "dollarsign":
      return <span className="text-lg font-bold">$</span>;
    case "mappin":
      return <MapPin className="w-5 h-5" />;
    case "award":
      return <Award className="w-5 h-5" />;
    default:
      return <CheckCircle className="w-5 h-5" />;
  }
}

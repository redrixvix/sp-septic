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
  Ruler,
  Droplets,
  Wrench,
  Camera,
  HardHat,
} from "lucide-react";
import { motion, useScroll, useTransform, useInView } from "framer-motion";
import { useRef } from "react";
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

// Expanded service card data — more features and details
const DETAILED_SERVICES = [
  {
    title: "Septic System Installation",
    description:
      "Complete design and installation for new construction and replacements. We handle Ohio Health Department permits, site evaluation, and every step of the installation process.",
    icon: HardHat,
    features: [
      "Complete system design & layout",
      "OH Health Dept. permit handling",
      "Soil evaluation & perk testing",
      "Tank & drain field installation",
      "Final inspection & code compliance",
      "Grading & site restoration",
    ],
    badge: "Most Requested",
  },
  {
    title: "Septic System Repair",
    description:
      "Fast, reliable repairs before small problems become expensive emergencies. We diagnose the root cause and fix it right the first time — not just the symptoms.",
    icon: Wrench,
    features: [
      "Drain field rejuvenation",
      "Tank & baffle repair",
      "Effluent pump replacement",
      "Service line excavation",
      "Emergency response available",
      "Post-repair camera verification",
    ],
    badge: null,
  },
  {
    title: "Excavation Services",
    description:
      "Professional excavation for driveways, utilities, and site prep. From trenching for drainage to complete site clearing, we have the equipment and operators to get it done.",
    icon: Ruler,
    features: [
      "Driveway & road grading",
      "Trench digging & backfill",
      "Utility & drainage trenching",
      "Pond & retention basin work",
      "Site clearing & demolition",
      "Demolition debris removal",
    ],
    badge: null,
  },
  {
    title: "Septic Pumping & Cleaning",
    description:
      "Regular maintenance is the #1 way to extend your system's life and avoid costly failures. Our pumping service thoroughly cleans tanks and inspects components.",
    icon: Droplets,
    features: [
      "Complete tank pumping",
      "Tank & inlet/outlet cleaning",
      "Filter element service",
      "Sludge level assessment",
      "Annual maintenance plans",
      "Emergency pumping service",
    ],
    badge: "Preventative Care",
  },
  {
    title: "Perk Testing & Leach Field",
    description:
      "Required before any new septic installation, perk testing evaluates your soil's absorption rate. We provide accurate results and design leach fields that meet Ohio code.",
    icon: FileCheck,
    features: [
      "Official perk/percolation testing",
      "Soil profile analysis",
      "Leach field sizing & design",
      "Replacement area identification",
      "Ohio EPA compliant reports",
      "Expedited permitting support",
    ],
    badge: null,
  },
  {
    title: "Camera Inspections",
    description:
      "State-of-the-art video inspection locates cracks, blockages, and root intrusion without destructive digging — saving you time and money on diagnosis.",
    icon: Camera,
    features: [
      "High-resolution video inspection",
      "Precise problem location",
      "Pre-purchase system inspections",
      "Post-repair verification",
      "DVD or digital file of footage",
      "Written condition report",
    ],
    badge: "No-Dig Diagnosis",
  },
];

// Project stats counter
const PROJECT_STATS = [
  { value: 50, suffix: "+", label: "Septic Systems Installed" },
  { value: 120, suffix: "+", label: "Drain Fields Serviced" },
  { value: 340, suffix: "+", label: "Excavation Projects" },
  { value: 47, suffix: "", label: "5-Star Google Reviews" },
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
      <div
        className="absolute inset-0 animate-gradient-shift"
        style={{
          background: "linear-gradient(135deg, #0f172a 0%, #1E3A5F 35%, #2563EB 60%, #1E3A5F 100%)",
          backgroundSize: "200% 200%",
        }}
      />
      <div
        className="absolute inset-0 opacity-5"
        style={{
          backgroundImage: `url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E")`,
        }}
      />
      <motion.div
        className="absolute right-8 top-1/2 -translate-y-1/2 w-64 h-64 rounded-full opacity-10 hidden lg:block"
        style={{
          background: "radial-gradient(circle, #2563EB, transparent 70%)",
        }}
        animate={{ scale: [1, 1.15, 1], opacity: [0.08, 0.14, 0.08] }}
        transition={{ duration: 6, repeat: Infinity, ease: "easeInOut" }}
      />
      <div className="absolute bottom-0 left-0 right-0 h-1 bg-gradient-to-r from-transparent via-blue-400/30 to-transparent" />

      <motion.div style={{ y, opacity }} className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 md:py-28">
        <div className="max-w-3xl">
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
              className="text-white font-bold"
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

// ── PROCESS STRIP ──────────────────────────────────────────────────────────────
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

// ── PROJECT STATS ─────────────────────────────────────────────────────────────
function ProjectStats() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-50px" });

  return (
    <section ref={ref} className="bg-slate-900 py-12">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-6 md:gap-8 text-center">
          {PROJECT_STATS.map((stat, i) => (
            <motion.div
              key={stat.label}
              initial={{ opacity: 0, y: 20 }}
              animate={isInView ? { opacity: 1, y: 0 } : {}}
              transition={{ duration: 0.5, delay: i * 0.1, ease: [0.25, 0.46, 0.45, 0.94] }}
            >
              <div className="text-3xl md:text-4xl font-extrabold text-white mb-1">
                <CountUp end={stat.value} suffix={stat.suffix} duration={2} />
              </div>
              <div className="text-sm text-slate-400 font-medium">{stat.label}</div>
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
    { label: "BBB Rating", sub: "Accredited since August 2025", display: "A+" },
    { label: "Satisfaction", sub: "Based on 47+ verified reviews", display: "", countTarget: 100, suffix: "%" },
    { label: "Local Jobs", sub: "Trumbull, Mahoning & Portage", display: "", countTarget: 50, suffix: "+" },
    { label: "Est.", sub: "Family owned & operated", display: "2021" },
  ];

  return (
    <section ref={ref} className="bg-white py-8 border-b">
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
    <section className="py-16 md:py-20 bg-slate-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <FadeIn>
          <div className="text-center mb-10">
            <Badge className="mb-3 bg-primary/10 text-primary border-0 text-xs">What We Do</Badge>
            <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-3">
              Full-Service Septic &amp; Excavation
            </h2>
            <p className="text-slate-600 max-w-2xl mx-auto text-base">
              From new system design to emergency repairs — and everything in between. No job too big, no job too small for our crew.
            </p>
          </div>
        </FadeIn>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
          {DETAILED_SERVICES.map((service) => (
            <Card
              key={service.title}
              className="group hover:shadow-lg transition-all duration-200 border-slate-200 bg-white hover:border-primary/30"
              style={{ boxShadow: "0 2px 4px -1px rgb(0 0 0 / 0.06)" }}
            >
              <CardContent className="p-5">
                {/* Header row */}
                <div className="flex items-start justify-between mb-3">
                  <div className="w-11 h-11 rounded-xl bg-primary/10 flex items-center justify-center text-primary group-hover:bg-primary group-hover:text-white transition-colors duration-200 flex-shrink-0">
                    <service.icon className="w-5 h-5" />
                  </div>
                  {service.badge && (
                    <span className="text-xs font-bold bg-secondary/10 text-secondary px-2 py-0.5 rounded-full whitespace-nowrap">
                      {service.badge}
                    </span>
                  )}
                </div>

                <h3 className="font-bold text-base text-slate-900 mb-1.5">{service.title}</h3>
                <p className="text-sm text-slate-600 mb-4 leading-relaxed line-clamp-3">
                  {service.description}
                </p>

                {/* Compact feature list */}
                <ul className="space-y-1.5">
                  {service.features.map((f) => (
                    <li key={f} className="flex items-center gap-2 text-xs text-slate-600">
                      <CheckCircle className="w-3.5 h-3.5 text-primary flex-shrink-0" />
                      {f}
                    </li>
                  ))}
                </ul>
              </CardContent>
            </Card>
          ))}
        </div>

        <FadeIn delay={0.2}>
          <div className="text-center mt-8">
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
    <section ref={ref} className="py-16 md:py-20 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid lg:grid-cols-2 gap-10 items-start">
          {/* Left column */}
          <div>
            <FadeIn>
              <Badge className="mb-3 bg-primary/10 text-primary border-0 text-xs">Why Choose Us</Badge>
              <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-4">
                Warren, Ohio Trusts S&amp;P
              </h2>
              <p className="text-slate-600 text-base leading-relaxed mb-6">
                When your septic fails or you need excavation work, you can&apos;t afford to wait — and you can&apos;t afford a company that does sloppy work. Here&apos;s why neighbors choose us.
              </p>
            </FadeIn>
            <div className="space-y-4">
              {WHY_CHOOSE_US.map((item, i) => (
                <motion.div
                  key={item.title}
                  initial={{ opacity: 0, x: -30 }}
                  animate={isInView ? { opacity: 1, x: 0 } : {}}
                  transition={{ duration: 0.5, delay: i * 0.1, ease: [0.25, 0.46, 0.45, 0.94] }}
                  className="flex gap-4"
                >
                  <div className="w-9 h-9 rounded-lg bg-secondary/10 flex items-center justify-center text-secondary flex-shrink-0 mt-0.5">
                    <WhyIcon name={item.icon} />
                  </div>
                  <div>
                    <h3 className="font-semibold text-slate-900 text-sm mb-0.5">{item.title}</h3>
                    <p className="text-sm text-slate-600 leading-snug">{item.description}</p>
                  </div>
                </motion.div>
              ))}
            </div>
          </div>

          {/* CTA Card */}
          <div className="lg:sticky lg:top-20">
            <motion.div
              className="bg-slate-900 rounded-2xl p-7 text-white shadow-2xl"
              initial={{ opacity: 0, y: 30 }}
              animate={isInView ? { opacity: 1, y: 0 } : {}}
              transition={{ duration: 0.6, delay: 0.3, ease: [0.25, 0.46, 0.45, 0.94] }}
            >
              <div className="flex items-center gap-3 mb-1">
                <div className="w-12 h-12 rounded-xl bg-blue-600 flex items-center justify-center text-xl font-black text-white">
                  A+
                </div>
                <div>
                  <div className="font-bold text-base">BBB Accredited</div>
                  <div className="text-slate-400 text-xs">Since August 2025</div>
                </div>
              </div>
              <div className="flex items-center gap-1.5 mt-3 mb-5">
                {Array.from({ length: 5 }).map((_, i) => (
                  <Star key={i} className="w-3.5 h-3.5 fill-primary text-primary" />
                ))}
                <span className="text-xs text-slate-400 ml-1">47 verified reviews</span>
              </div>
              <p className="text-slate-300 text-sm mb-5 leading-relaxed">
                Earned through quality work and honest dealings. BBB Accredited with a perfect A+ rating.
              </p>
              <hr className="border-slate-700 mb-5" />
              <div className="space-y-2.5 mb-6">
                {[
                  "Free on-site estimates",
                  "Upfront, honest pricing",
                  "All work warranted",
                  "Licensed in Ohio",
                ].map((item) => (
                  <div key={item} className="flex items-center gap-2.5 text-sm text-slate-300">
                    <CheckCircle className="w-4 h-4 text-primary flex-shrink-0" />
                    {item}
                  </div>
                ))}
              </div>
              <a href={`tel:${COMPANY.phone}`} className="block mb-3">
                <Button size="lg" className="w-full gap-2 bg-primary hover:bg-primary/90 font-bold text-base">
                  <Phone className="w-5 h-5" />
                  {COMPANY.phone}
                </Button>
              </a>
              <div className="flex items-center justify-center gap-2 text-sm text-slate-400">
                <Clock className="w-4 h-4" />
                <span>Mon–Sat, 7AM–7PM</span>
              </div>
              <div className="mt-4 pt-4 border-t border-slate-700 flex items-start gap-2.5">
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

// ── GALLERY TEASER ────────────────────────────────────────────────────────────
function GalleryTeaser() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-50px" });

  const projects = [
    { type: "Septic Installation", location: "Warren, OH", detail: "New 1,500 gal tank + drain field" },
    { type: "Drain Field Replacement", location: "Cortland, OH", detail: "Mound system — rocky soil" },
    { type: "Excavation & Grading", location: "Niles, OH", detail: "1,200 ft² driveway base prep" },
    { type: "Emergency Repair", location: "Liberty Township", detail: "Same-day pump replacement" },
    { type: "Septic Pumping", location: "Girard, OH", detail: "1,000 gal residential tank" },
    { type: "Leach Field Addition", location: "Howland Township", detail: "Extra capacity for growing family" },
  ];

  return (
    <section ref={ref} className="py-14 md:py-18 bg-slate-900 text-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <FadeIn>
          <div className="flex flex-col md:flex-row md:items-end md:justify-between mb-8">
            <div>
              <h2 className="text-2xl md:text-3xl font-bold mb-1">Recent Projects</h2>
              <p className="text-slate-400 text-sm md:text-base">A sample of work completed throughout Trumbull County.</p>
            </div>
            <Link href="/gallery" className="mt-3 md:mt-0">
              <Button size="sm" variant="outline" className="border-primary text-primary hover:bg-primary/10 hover:text-blue-200 font-semibold text-xs gap-1.5">
                View Full Gallery <ArrowRight className="w-3 h-3" />
              </Button>
            </Link>
          </div>
        </FadeIn>

        <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
          {projects.map((project, i) => (
            <motion.div
              key={i}
              initial={{ opacity: 0, scale: 0.95 }}
              animate={isInView ? { opacity: 1, scale: 1 } : {}}
              transition={{ duration: 0.4, delay: i * 0.07, ease: [0.25, 0.46, 0.45, 0.94] }}
              whileHover={{ scale: 1.03, zIndex: 10 }}
              className="bg-slate-800 border border-slate-700 rounded-xl p-4 hover:bg-slate-700 transition-colors cursor-pointer overflow-hidden"
            >
              <div className="flex items-center gap-2 mb-2">
                <div className="w-1.5 h-1.5 rounded-full bg-primary flex-shrink-0" />
                <span className="text-xs font-semibold text-white truncate">{project.type}</span>
              </div>
              <div className="text-xs text-slate-400 mb-1.5 flex items-center gap-1">
                <MapPin className="w-3 h-3 flex-shrink-0" />
                {project.location}
              </div>
              <div className="text-xs text-slate-500 leading-snug">{project.detail}</div>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}

// ── TESTIMONIALS ───────────────────────────────────────────────────────────────
function TestimonialsSection() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-50px" });

  return (
    <section ref={ref} className="py-14 md:py-20 bg-slate-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <FadeIn>
          <div className="text-center mb-10">
            <Badge className="mb-3 bg-blue-50 text-primary border-0 text-xs">Customer Reviews</Badge>
            <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-3">
              What Neighbors Are Saying
            </h2>
            <div className="flex items-center justify-center gap-1">
              {Array.from({ length: 5 }).map((_, i) => (
                <Star key={i} className="w-5 h-5 fill-primary text-primary" />
              ))}
              <span className="text-sm text-slate-500 ml-2">5.0 average · 47+ verified reviews</span>
            </div>
          </div>
        </FadeIn>

        <StaggerContainer className="grid md:grid-cols-3 gap-5" staggerDelay={0.1}>
          {TESTIMONIALS.map((t) => (
            <StaggerItem key={t.name}>
              <Card className="bg-white border-0 shadow-sm relative">
                <CardContent className="p-5">
                  <div className="testimonial-quote absolute top-2 left-2" />
                  <div className="flex gap-1 mb-3 mt-4">
                    {Array.from({ length: t.rating }).map((_, i) => (
                      <Star key={i} className="w-4 h-4 fill-primary text-primary" />
                    ))}
                  </div>
                  <p className="text-slate-700 italic text-sm mb-4 leading-relaxed">&ldquo;{t.text}&rdquo;</p>
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
    <section ref={ref} className="py-14 bg-white border-t">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <FadeIn>
          <div className="text-center mb-6">
            <Badge className="mb-3 bg-primary/10 text-primary border-0 text-xs">Service Area</Badge>
            <h2 className="text-2xl font-bold text-slate-900 mb-2">Proudly Serving Northeast Ohio</h2>
            <p className="text-slate-600 text-sm">
              From Warren to Niles, Cortland to Girard — we&apos;re your neighbors.
            </p>
          </div>
        </FadeIn>
        <motion.div
          ref={ref}
          className="flex flex-wrap justify-center gap-2"
          initial="hidden"
          animate={isInView ? "visible" : "hidden"}
          variants={{
            hidden: {},
            visible: { transition: { staggerChildren: 0.04 } },
          }}
        >
          {SERVICE_AREAS.map((area) => (
            <motion.div
              key={area}
              variants={{
                hidden: { opacity: 0, y: 12 },
                visible: { opacity: 1, y: 0, transition: { duration: 0.35 } },
              }}
            >
              <Badge
                variant="secondary"
                className="text-xs px-3 py-1 bg-slate-100 text-slate-700 hover:bg-slate-100 cursor-default"
              >
                <MapPin className="w-3 h-3 mr-1 text-primary" />
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
    <section ref={ref} className="bg-primary py-14">
      <motion.div
        className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white"
        initial={{ opacity: 0, y: 30 }}
        animate={isInView ? { opacity: 1, y: 0 } : {}}
        transition={{ duration: 0.6, ease: [0.25, 0.46, 0.45, 0.94] }}
      >
        <Calendar className="w-10 h-10 mx-auto mb-3 opacity-80" />
        <h2 className="text-3xl font-bold mb-3">Septic Problem? Don&apos;t Wait.</h2>
        <p className="text-blue-100 text-base mb-7 max-w-2xl mx-auto">
          A small septic issue becomes a big, expensive emergency fast. Call S&P today — we&apos;re ready to help, starting now.
        </p>
        <div className="flex flex-col sm:flex-row gap-3 justify-center">
          <a href={`tel:${COMPANY.phone}`}>
            <Button size="lg" className="gap-2 bg-white text-primary hover:bg-secondary/10 font-bold text-base px-8 shadow-xl">
              <Phone className="w-5 h-5" />
              Call {COMPANY.phone}
            </Button>
          </a>
          <Link href="/contact">
            <Button size="lg" variant="outline" className="text-base px-8 border-white text-white hover:bg-white/10 font-bold">
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
      <ProjectStats />
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
function WhyIcon({ name }: { name: string }) {
  const lname = name.toLowerCase().replace(" ", "");
  switch (lname) {
    case "shield":
      return <Shield className="w-4 h-4" />;
    case "users":
      return <Users className="w-4 h-4" />;
    case "badgecheck":
      return <CheckCircle className="w-4 h-4" />;
    case "dollarsign":
      return <span className="text-base font-bold">$</span>;
    case "mappin":
      return <MapPin className="w-4 h-4" />;
    case "award":
      return <Award className="w-4 h-4" />;
    default:
      return <CheckCircle className="w-4 h-4" />;
  }
}

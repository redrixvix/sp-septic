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
import { COMPANY, SERVICES, WHY_CHOOSE_US, TESTIMONIALS, SERVICE_AREAS } from "@/lib/data";

function DropletIcon() {
  return (
    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="currentColor" stroke="none">
      <path d="M12 2.69l5.66 5.66a8 8 0 1 1-11.31 0z" />
    </svg>
  );
}

// 4-step how-it-works strip
const PROCESS_STEPS = [
  { icon: Phone, label: "Call or Fill Form", desc: "Get in touch — we respond within 1 business day" },
  { icon: FileCheck, label: "Free Assessment", desc: "On-site evaluation and honest quote, no obligation" },
  { icon: Truck, label: "We Get It Done", desc: "Professional installation or repair, on schedule" },
  { icon: ThumbsUp, label: "Done Right", desc: "Final inspection and your complete satisfaction guaranteed" },
];

export default function HomePage() {
  return (
    <>
      {/* ── HERO ── */}
      <section className="relative bg-slate-900 text-white overflow-hidden">
        <div
          className="absolute inset-0 opacity-5"
          style={{
            backgroundImage: `url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E")`,
          }}
        />
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 md:py-28">
          <div className="max-w-3xl">
            <div className="flex flex-wrap gap-2 mb-5">
              <Badge className="bg-emerald-600/20 text-emerald-300 border-emerald-500/30 hover:bg-emerald-600/20 text-xs font-medium backdrop-blur-sm">
                BBB Accredited — A+ Rating
              </Badge>
              <Badge className="bg-white/10 text-white border-white/20 hover:bg-white/10 text-xs backdrop-blur-sm">
                Family Owned &amp; Operated
              </Badge>
              <Badge className="bg-white/10 text-white border-white/20 hover:bg-white/10 text-xs backdrop-blur-sm">
                Licensed &amp; Insured in Ohio
              </Badge>
            </div>

            <h1 className="text-4xl md:text-5xl lg:text-6xl font-extrabold leading-tight tracking-tight mb-6">
              Septic &amp; Excavating{" "}
              <span className="text-emerald-400">Done Right</span>
              <br />
              Every Time
            </h1>

            <p className="text-lg md:text-xl text-slate-300 leading-relaxed mb-8 max-w-2xl">
              Family-owned and BBB Accredited. We install, repair, and maintain septic systems — and handle excavation jobs of all sizes — throughout Warren, Ohio and Trumbull County.
            </p>

            <div className="flex flex-col sm:flex-row gap-4 mb-10">
              <a href={`tel:${COMPANY.phone}`} className="inline-block">
                <Button size="lg" className="gap-2 text-base w-full sm:w-auto bg-emerald-600 hover:bg-emerald-700 font-bold shadow-lg shadow-emerald-900/30">
                  <Phone className="w-5 h-5" />
                  Call {COMPANY.phone}
                </Button>
              </a>
              <Link href="/contact">
                <Button size="lg" className="gap-2 text-base w-full sm:w-auto bg-white text-slate-900 hover:bg-slate-100 font-bold shadow-lg">
                  Get Free Estimate <ArrowRight className="w-4 h-4" />
                </Button>
              </Link>
            </div>

            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 text-sm">
              {[
                { icon: CheckCircle, text: "Free Estimates" },
                { icon: CheckCircle, text: "Same-Day Service" },
                { icon: CheckCircle, text: "Licensed & Insured" },
                { icon: CheckCircle, text: "Work Guaranteed" },
              ].map(({ icon: Icon, text }) => (
                <div key={text} className="flex items-center gap-2">
                  <Icon className="w-4 h-4 text-emerald-400 flex-shrink-0" />
                  <span className="text-slate-300">{text}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      {/* ── HOW IT WORKS STRIP ── */}
      <section className="bg-emerald-600 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-6">
            {PROCESS_STEPS.map((step, i) => (
              <div key={step.label} className="flex items-start gap-3">
                <div className="w-9 h-9 rounded-full bg-white/20 flex items-center justify-center flex-shrink-0">
                  <step.icon className="w-4 h-4 text-white" />
                </div>
                <div>
                  <div className="text-white font-bold text-sm">{step.label}</div>
                  <div className="text-emerald-100 text-xs mt-0.5 leading-snug">{step.desc}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── TRUST NUMBERS ── */}
      <section className="bg-white py-10 border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-wrap justify-center gap-12 md:gap-20 text-center">
            {[
              { number: "A+", label: "BBB Rating", sub: "Accredited since 2025" },
              { number: "50+", label: "Projects Done", sub: "All sizes, all types" },
              { number: "100%", label: "Satisfaction", sub: "Zero comeback calls" },
              { number: "2021", label: "Est.", sub: "Family owned & operated" },
            ].map((stat) => (
              <div key={stat.label}>
                <div className="text-3xl md:text-4xl font-extrabold text-slate-900">{stat.number}</div>
                <div className="text-sm font-semibold text-slate-700">{stat.label}</div>
                <div className="text-xs text-slate-400">{stat.sub}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── SERVICES ── */}
      <section className="py-16 md:py-24 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <Badge className="mb-3 bg-emerald-100 text-emerald-700 border-0 text-xs">What We Do</Badge>
            <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-4">
              Full-Service Septic &amp; Excavation
            </h2>
            <p className="text-slate-600 max-w-2xl mx-auto text-lg">
              From new system design to emergency repairs — and everything in between. No job too big, no job too small.
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {SERVICES.map((service) => (
              <Card
                key={service.title}
                className="group hover:shadow-xl transition-all duration-250 border-slate-200 hover:border-emerald-300 bg-white"
              >
                <CardContent className="p-6">
                  <div className="w-12 h-12 rounded-xl bg-emerald-50 flex items-center justify-center text-emerald-600 mb-4 group-hover:bg-emerald-600 group-hover:text-white transition-colors duration-200">
                    <ServiceIcon name={service.icon} />
                  </div>
                  <h3 className="font-bold text-lg text-slate-900 mb-2">{service.title}</h3>
                  <p className="text-sm text-slate-600 mb-4 leading-relaxed">{service.description}</p>
                  <ul className="space-y-1.5">
                    {service.features.map((f) => (
                      <li key={f} className="flex items-center gap-2 text-xs text-slate-500">
                        <svg className="w-3 h-3 text-emerald-500 flex-shrink-0" fill="currentColor" viewBox="0 0 16 16">
                          <path d="M13.78 4.22a.75.75 0 0 1 0 1.06l-7.25 7.25a.75.75 0 0 1-1.06 0L2.22 9.28a.75.75 0 0 1 1.06-1.06L6 10.94l6.72-6.72a.75.75 0 0 1 1.06 0z" />
                        </svg>
                        {f}
                      </li>
                    ))}
                  </ul>
                </CardContent>
              </Card>
            ))}
          </div>

          <div className="text-center mt-10">
            <Link href="/services">
              <Button size="lg" className="gap-2 bg-emerald-600 hover:bg-emerald-700 font-bold">
                View All Services <ChevronRight className="w-4 h-4" />
              </Button>
            </Link>
          </div>
        </div>
      </section>

      {/* ── WHY CHOOSE US + CTA ── */}
      <section className="py-16 md:py-24 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12 items-start">
            <div>
              <Badge className="mb-3 bg-emerald-100 text-emerald-700 border-0 text-xs">Why Choose Us</Badge>
              <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-4">
                Warren, Ohio Trusts S&amp;P
              </h2>
              <p className="text-slate-600 text-lg leading-relaxed mb-8">
                When your septic fails or you need excavation work, you can&apos;t afford to wait — and you can&apos;t afford a company that does sloppy work. Here&apos;s why neighbors choose us.
              </p>
              <div className="space-y-5">
                {WHY_CHOOSE_US.map((item) => (
                  <div key={item.title} className="flex gap-4">
                    <div className="w-10 h-10 rounded-lg bg-emerald-50 flex items-center justify-center text-emerald-600 flex-shrink-0">
                      <WhyIcon name={item.icon} />
                    </div>
                    <div>
                      <h3 className="font-semibold text-slate-900 mb-0.5">{item.title}</h3>
                      <p className="text-sm text-slate-600">{item.description}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* CTA Card — sticky-ish */}
            <div className="lg:sticky lg:top-24">
              <div className="bg-slate-900 rounded-2xl p-8 text-white shadow-2xl">
                <div className="flex items-center gap-3 mb-2">
                  <div className="w-14 h-14 rounded-xl bg-amber-500 flex items-center justify-center text-2xl font-black text-white">A+</div>
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
                  <div className="flex items-center gap-3 text-sm text-slate-300">
                    <CheckCircle className="w-4 h-4 text-emerald-400" />
                    Free on-site estimates
                  </div>
                  <div className="flex items-center gap-3 text-sm text-slate-300">
                    <CheckCircle className="w-4 h-4 text-emerald-400" />
                    Upfront, honest pricing
                  </div>
                  <div className="flex items-center gap-3 text-sm text-slate-300">
                    <CheckCircle className="w-4 h-4 text-emerald-400" />
                    All work warranted
                  </div>
                  <div className="flex items-center gap-3 text-sm text-slate-300">
                    <CheckCircle className="w-4 h-4 text-emerald-400" />
                    Licensed in Ohio
                  </div>
                </div>
                <a href={`tel:${COMPANY.phone}`} className="block mb-3">
                  <Button size="lg" className="w-full gap-2 bg-emerald-600 hover:bg-emerald-700 font-bold text-lg">
                    <Phone className="w-5 h-5" />
                    {COMPANY.phone}
                  </Button>
                </a>
                <div className="flex items-center justify-center gap-2 text-sm text-slate-400">
                  <Clock className="w-4 h-4" />
                  <span>Mon–Sat, 7AM–7PM</span>
                </div>
                <div className="mt-5 pt-5 border-t border-slate-700 flex items-start gap-3">
                  <MapPin className="w-4 h-4 text-emerald-400 flex-shrink-0 mt-0.5" />
                  <span className="text-xs text-slate-400">
                    {COMPANY.address}, {COMPANY.city}, {COMPANY.state} {COMPANY.zip}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* ── GALLERY TEASER ── */}
      <section className="py-16 md:py-20 bg-slate-900 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-10">
            <h2 className="text-3xl md:text-4xl font-bold mb-3">Our Work Speaks for Itself</h2>
            <p className="text-slate-400 text-lg">See septic installations, excavations, and repairs throughout Trumbull County.</p>
          </div>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
            {Array.from({ length: 8 }).map((_, i) => (
              <div
                key={i}
                className="aspect-square rounded-xl bg-slate-800 border border-slate-700 flex flex-col items-center justify-center text-slate-600 hover:bg-slate-700 transition-colors cursor-pointer"
              >
                <Truck className="w-8 h-8 mb-2 opacity-40" />
                <span className="text-xs opacity-60">Project {i + 1}</span>
              </div>
            ))}
          </div>
          <div className="text-center mt-8">
            <Link href="/gallery">
              <Button size="lg" variant="outline" className="border-emerald-500 text-emerald-400 hover:bg-emerald-950 hover:text-emerald-300 font-bold">
                View Full Gallery <ArrowRight className="w-4 h-4 ml-2" />
              </Button>
            </Link>
          </div>
        </div>
      </section>

      {/* ── TESTIMONIALS ── */}
      <section className="py-16 md:py-24 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <Badge className="mb-3 bg-amber-100 text-amber-700 border-0 text-xs">Customer Reviews</Badge>
            <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-3">
              What Neighbors Are Saying
            </h2>
            <div className="flex items-center justify-center gap-1">
              {Array.from({ length: 5 }).map((_, i) => (
                <Star key={i} className="w-5 h-5 fill-amber-400 text-amber-400" />
              ))}
              <span className="text-sm text-slate-500 ml-2">5.0 average · Ohio customers</span>
            </div>
          </div>

          <div className="grid md:grid-cols-3 gap-6">
            {TESTIMONIALS.map((t) => (
              <Card key={t.name} className="bg-white border-0 shadow-sm">
                <CardContent className="p-6">
                  <div className="flex gap-1 mb-3">
                    {Array.from({ length: t.rating }).map((_, i) => (
                      <Star key={i} className="w-4 h-4 fill-amber-400 text-amber-400" />
                    ))}
                  </div>
                  <p className="text-slate-700 italic mb-4 leading-relaxed">&ldquo;{t.text}&rdquo;</p>
                  <div className="flex items-center gap-3">
                    <div className="w-9 h-9 rounded-full bg-emerald-100 flex items-center justify-center text-emerald-700 font-bold text-sm">
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
            ))}
          </div>
        </div>
      </section>

      {/* ── SERVICE AREAS ── */}
      <section className="py-16 md:py-20 bg-white border-t">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-8">
            <Badge className="mb-3 bg-emerald-100 text-emerald-700 border-0 text-xs">Service Area</Badge>
            <h2 className="text-3xl font-bold text-slate-900 mb-3">Proudly Serving Northeast Ohio</h2>
            <p className="text-slate-600">
              From Warren to Niles, Cortland to Girard — we&apos;re your neighbors, ready to help.
            </p>
          </div>
          <div className="flex flex-wrap justify-center gap-3">
            {SERVICE_AREAS.map((area) => (
              <Badge
                key={area}
                variant="secondary"
                className="text-sm px-4 py-1.5 bg-slate-100 text-slate-700 hover:bg-slate-100 cursor-default"
              >
                <MapPin className="w-3 h-3 mr-1.5 text-emerald-500" />
                {area}
              </Badge>
            ))}
          </div>
        </div>
      </section>

      {/* ── FINAL CTA ── */}
      <section className="bg-emerald-600 py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white">
          <Calendar className="w-12 h-12 mx-auto mb-4 opacity-80" />
          <h2 className="text-3xl md:text-4xl font-bold mb-4"> septic Problem? Don&apos;t Wait.</h2>
          <p className="text-emerald-100 text-lg mb-8 max-w-2xl mx-auto">
            A small septic issue becomes a big, expensive emergency fast. Call S&P today — we&apos;re ready to help, starting now.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <a href={`tel:${COMPANY.phone}`}>
              <Button size="lg" className="gap-2 bg-white text-emerald-700 hover:bg-emerald-50 font-bold text-lg px-10 shadow-xl">
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
        </div>
      </section>
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

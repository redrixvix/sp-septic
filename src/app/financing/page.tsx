import type { Metadata } from "next";
import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { Phone, CheckCircle, DollarSign, Clock, ArrowRight } from "lucide-react";
import { Button } from "@/components/ui/button";
import { COMPANY } from "@/lib/data";

export const metadata: Metadata = {
  title: "Financing & Payment Options — S&P Septic | Warren, Ohio",
  description:
    "Flexible payment options for septic and excavation projects. Learn about financing, payment methods, and how S&P helps make your project affordable.",
};

const OPTIONS = [
  {
    icon: DollarSign,
    title: "Cash & Check",
    desc: "Cash and check payments receive straightforward pricing with no processing fees.",
    color: "bg-secondary/10 text-secondary",
  },
  {
    icon: CheckCircle,
    title: "Credit Cards",
    desc: "We accept all major credit cards. Note: a processing fee may apply for credit card payments.",
    color: "bg-slate-100 text-slate-600",
  },
  {
    icon: Clock,
    title: "Payment Plans",
    desc: "For larger projects, we may be able to arrange a payment plan. Ask us about your options when you get an estimate.",
    color: "bg-amber-100 text-amber-600",
  },
];

const FAQS = [
  {
    q: "Do you offer payment plans?",
    a: "For qualifying larger projects (new septic installations, major excavation), we may be able to arrange a payment plan. This is handled on a case-by-case basis. Ask during your estimate and we&apos;ll let you know your options.",
  },
  {
    q: "Is financing available through a third party?",
    a: "We don&apos;t partner with specific financing companies, but we can provide documentation that homeowner financing programs (like personal loans or home equity lines of credit) may require. Your bank or credit union is often the best place to look for affordable project financing.",
  },
  {
    q: "Do I pay a deposit?",
    a: "Our deposit policy depends on the project scope. We&apos;ll explain the payment schedule when we provide your estimate — no surprises.",
  },
  {
    q: "Is there a fee for credit card payments?",
    a: "Some projects may incur a small processing fee for credit card payments. We&apos;ll tell you before you decide on a payment method.",
  },
  {
    q: "When is payment due?",
    a: "For most services (pumping, repairs, inspections), payment is due upon completion. For larger projects (new installations, major excavation), we typically split payment between a deposit and completion.",
  },
  {
    q: "Do you provide written estimates?",
    a: "Yes — and they&apos;re free. We provide written, detailed estimates before any work begins. The price on the estimate is the price you pay.",
  },
];

export default function FinancingPage() {
  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-primary text-white border-0">Financing & Payments</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Making Your Project Affordable
          </h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            Septic and excavation projects are significant investments. We believe in transparent pricing and flexible payment options to help you manage the cost.
          </p>
        </div>
      </section>

      {/* Payment Options */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-slate-900 mb-3">Payment Methods</h2>
            <p className="text-slate-600 max-w-2xl mx-auto">
              We keep payment simple. Here&apos;s what we accept and what to expect.
            </p>
          </div>
          <div className="grid md:grid-cols-3 gap-6">
            {OPTIONS.map((opt) => (
              <Card key={opt.title} className="border-0 shadow-sm">
                <CardContent className="p-6 text-center">
                  <div className={`w-14 h-14 rounded-2xl ${opt.color} flex items-center justify-center mx-auto mb-4`}>
                    <opt.icon className="w-7 h-7" />
                  </div>
                  <h3 className="font-bold text-lg text-slate-900 mb-2">{opt.title}</h3>
                  <p className="text-sm text-slate-600 leading-relaxed">{opt.desc}</p>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* Cost Comparison */}
      <section className="py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div>
              <Badge className="mb-3 bg-amber-100 text-primary border-0 text-xs">The Reality</Badge>
              <h2 className="text-3xl font-bold text-slate-900 mb-4">
                Why Septic Work Costs What It Costs
              </h2>
              <p className="text-slate-600 text-lg leading-relaxed mb-6">
                A new septic system is a significant investment — but it&apos;s also one that protects your home&apos;s value, your family&apos;s health, and your property. Here&apos;s why the price is what it is.
              </p>
              <div className="space-y-4">
                <div className="flex gap-3">
                  <CheckCircle className="w-5 h-5 text-secondary flex-shrink-0 mt-0.5" />
                  <div>
                    <h4 className="font-semibold text-slate-900">Permits & Inspections</h4>
                    <p className="text-sm text-slate-600">Ohio health department permits, site evaluations, and required inspections add to the cost — but they also protect you.</p>
                  </div>
                </div>
                <div className="flex gap-3">
                  <CheckCircle className="w-5 h-5 text-secondary flex-shrink-0 mt-0.5" />
                  <div>
                    <h4 className="font-semibold text-slate-900">Equipment & Labor</h4>
                    <p className="text-sm text-slate-600">Heavy equipment, trained operators, and licensed installers don&apos;t come cheap — but they also don&apos;t come with callbacks.</p>
                  </div>
                </div>
                <div className="flex gap-3">
                  <CheckCircle className="w-5 h-5 text-secondary flex-shrink-0 mt-0.5" />
                  <div>
                    <h4 className="font-semibold text-slate-900">Quality Materials</h4>
                    <p className="text-sm text-slate-600">Proper gravel, quality piping, and durable tanks cost more upfront — and last significantly longer.</p>
                  </div>
                </div>
                <div className="flex gap-3">
                  <CheckCircle className="w-5 h-5 text-secondary flex-shrink-0 mt-0.5" />
                  <div>
                    <h4 className="font-semibold text-slate-900">The Cost of Doing It Wrong</h4>
                    <p className="text-sm text-slate-600">A failed DIY or cut-rate job can cost $20,000–$40,000 to fix. Doing it right the first time is always cheaper.</p>
                  </div>
                </div>
              </div>
            </div>
            <Card className="bg-primary border-0 text-white">
              <CardContent className="p-8">
                <h3 className="font-bold text-xl mb-4">Get an Exact Price for Your Project</h3>
                <p className="text-blue-100 mb-6 leading-relaxed">
                  Every project is different. The only way to know your real cost is to get a free on-site estimate. We&apos;ll assess your property and give you an honest number — before any work begins.
                </p>
                <div className="space-y-3 mb-8">
                  <div className="flex items-center gap-2 text-sm text-blue-100">
                    <CheckCircle className="w-4 h-4 text-blue-300" />
                    Free on-site assessment
                  </div>
                  <div className="flex items-center gap-2 text-sm text-blue-100">
                    <CheckCircle className="w-4 h-4 text-blue-300" />
                    Written estimate — no obligation
                  </div>
                  <div className="flex items-center gap-2 text-sm text-blue-100">
                    <CheckCircle className="w-4 h-4 text-blue-300" />
                    Price guaranteed before work starts
                  </div>
                  <div className="flex items-center gap-2 text-sm text-blue-100">
                    <CheckCircle className="w-4 h-4 text-blue-300" />
                    No financing pressure
                  </div>
                </div>
                <a href={`tel:${COMPANY.phone}`} className="block">
                  <Button size="lg" className="w-full gap-2 bg-white text-primary hover:bg-secondary/10 font-bold">
                    <Phone className="w-5 h-5" />
                    Call {COMPANY.phone}
                  </Button>
                </a>
                <div className="mt-4 text-center">
                  <Link href="/contact" className="text-sm text-blue-200 hover:text-white flex items-center justify-center gap-1">
                    Or use our online form <ArrowRight className="w-3 h-3" />
                  </Link>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* FAQ */}
      <section className="py-16 bg-white border-t">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-10">
            <h2 className="text-3xl font-bold text-slate-900 mb-3">Common Questions About Payment</h2>
          </div>
          <div className="space-y-4">
            {FAQS.map((faq) => (
              <Card key={faq.q} className="border-slate-200">
                <CardContent className="p-5">
                  <h3 className="font-bold text-slate-900 mb-2">{faq.q}</h3>
                  <p className="text-sm text-slate-600 leading-relaxed" dangerouslySetInnerHTML={{ __html: faq.a }} />
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="bg-slate-900 py-14">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white">
          <h2 className="text-3xl font-bold mb-4">Ready to Talk About Your Project?</h2>
          <p className="text-slate-300 text-lg mb-8">
            No pressure, no financing pitches — just an honest conversation about what your project needs and what it costs.
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
                Get Free Estimate <ArrowRight className="w-4 h-4 ml-2" />
              </Button>
            </Link>
          </div>
        </div>
      </section>
    </>
  );
}

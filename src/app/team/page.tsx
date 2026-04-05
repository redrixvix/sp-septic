import type { Metadata } from "next";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { Phone, Users, Star } from "lucide-react";
import { COMPANY } from "@/lib/data";

export const metadata: Metadata = {
  title: "Meet the Team — S&P Septic & Excavating",
  description:
    "The S&P Septic team. Family-owned, locally operated, and committed to doing the job right on every call in Warren, Ohio and Trumbull County.",
};

const TEAM = [
  {
    name: "Steve P.",
    role: "Owner / Operator",
    bio: "Steve founded S&P Septic and Excavating with one goal: do honest work at fair prices. With years of experience in septic system installation and repair, he oversees every project to make sure it meets our high standards.",
    icon: "👷",
  },
  {
    name: "The S&P Crew",
    role: "Installation & Excavation Team",
    bio: "Our field crew is trained, experienced, and takes pride in their work. They show up on time, work efficiently, and leave every site cleaner than they found it.",
    icon: "🚜",
  },
];

export default function TeamPage() {
  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-primary text-white border-0">Meet the Team</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Real People. Real Work.
          </h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            We&apos;re not a big corporation with a call center. We&apos;re your neighbors — and we take that seriously on every single job.
          </p>
        </div>
      </section>

      {/* Values Strip */}
      <section className="bg-primary py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6 text-center text-white">
            {[
              { label: "Family Owned", sub: "Since 2021" },
              { label: "BBB Accredited", sub: "A+ Rating" },
              { label: "Licensed", sub: "Ohio Contractor" },
              { label: "Fully Insured", sub: "General Liability" },
            ].map((item) => (
              <div key={item.label}>
                <div className="font-bold text-lg">{item.label}</div>
                <div className="text-xs text-blue-200">{item.sub}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Team */}
      <section className="py-16 md:py-24 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="max-w-3xl mx-auto space-y-12">
            {TEAM.map((member) => (
              <div key={member.name} className="flex flex-col sm:flex-row gap-6 items-start">
                <div className="w-24 h-24 rounded-2xl bg-slate-100 flex items-center justify-center text-5xl flex-shrink-0">
                  {member.icon}
                </div>
                <div>
                  <h2 className="font-bold text-2xl text-slate-900 mb-0.5">{member.name}</h2>
                  <p className="text-secondary font-semibold text-sm mb-3">{member.role}</p>
                  <p className="text-slate-600 leading-relaxed">{member.bio}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Philosophy */}
      <section className="py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-slate-900 mb-3">How We Do Business</h2>
            <p className="text-slate-600 max-w-2xl mx-auto">
              Every septic company says they care about quality and customer service. Here&apos;s how we actually practice it.
            </p>
          </div>
          <div className="grid md:grid-cols-3 gap-6">
            {[
              {
                title: "We Show Up",
                desc: "On time, every time. We call if we&apos;re running late — and we don&apos;t no-show. Your time is valuable.",
              },
              {
                title: "We Give Straight Answers",
                desc: "No technical jargon designed to confuse you. No upselling. Just honest assessments of what your system needs.",
              },
              {
                title: "We Leave It Clean",
                desc: "Every job site cleaned up when we&apos;re done. You shouldn&apos;t know we were there except that the work is perfect.",
              },
            ].map((item) => (
              <Card key={item.title} className="bg-white border-0 shadow-sm">
                <CardContent className="p-6">
                  <h3 className="font-bold text-slate-900 mb-2">{item.title}</h3>
                  <p className="text-sm text-slate-600 leading-relaxed">{item.desc}</p>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="bg-primary py-14">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-white">
          <h2 className="text-3xl font-bold mb-4">Ready to Work With Us?</h2>
          <p className="text-blue-100 text-lg mb-8">Give us a call — we&apos;d love to help with your septic or excavation project.</p>
          <a href={`tel:${COMPANY.phone}`}>
            <div className="inline-flex items-center gap-2 bg-white text-primary hover:bg-secondary/10 font-bold text-lg px-10 py-3 rounded-lg transition-colors">
              <Phone className="w-5 h-5" />
              Call {COMPANY.phone}
            </div>
          </a>
        </div>
      </section>
    </>
  );
}

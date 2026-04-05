import type { Metadata } from "next";
import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { Calendar, Clock, ArrowRight } from "lucide-react";

export const metadata: Metadata = {
  title: "Septic & Excavation Blog — Tips & Education",
  description:
    "Helpful articles about septic system maintenance, excavation services, and septic education for homeowners in Ohio.",
};

const ARTICLES = [
  {
    slug: "how-often-should-you-pump-your-septic-tank",
    title: "How Often Should You Pump Your Septic Tank?",
    excerpt:
      "One of the most common questions we get: 'How often does my septic tank need to be pumped?' Here's the short answer — and why it matters more than you think.",
    category: "Septic Maintenance",
    readTime: "4 min read",
    date: "April 2026",
    featured: true,
  },
  {
    slug: "signs-your-septic-system-is-failing",
    title: "7 Warning Signs Your Septic System Is Failing",
    excerpt:
      "A failing septic system is more than inconvenient — it can cause serious health hazards and expensive damage. Learn the red flags every homeowner should know.",
    category: "Septic Repair",
    readTime: "5 min read",
    date: "March 2026",
    featured: true,
  },
  {
    slug: "septic-vs-sewer-pros-cons-ohio",
    title: "Septic vs. City Sewer: Which Is Better for Ohio Homeowners?",
    excerpt:
      "If you're building new or deciding whether to connect to city sewer, weigh the pros and cons carefully. We break it down for Ohio homeowners.",
    category: "Education",
    readTime: "6 min read",
    date: "March 2026",
    featured: false,
  },
  {
    slug: "what-not-to-flush-septic-system",
    title: "What NOT to Flush: A Homeowner's Guide to Septic Safety",
    excerpt:
      "Your septic system is not a trash can. Flushing the wrong items is one of the fastest ways to cause costly damage. Here's the definitive list.",
    category: "Septic Maintenance",
    readTime: "3 min read",
    date: "February 2026",
    featured: false,
  },
  {
    slug: "excavation-for-driveways-what-ohio-homeowners-need-to-know",
    title: "Excavation for Driveways: What Ohio Homeowners Need to Know",
    excerpt:
      "From grading to gravel selection, proper driveway excavation prevents drainage problems, frost heave, and expensive repairs down the road.",
    category: "Excavation",
    readTime: "5 min read",
    date: "February 2026",
    featured: false,
  },
  {
    slug: "perk-testing-soil-evaluation-ohio",
    title: "Perk Testing & Soil Evaluation: Why It Matters for Your Septic System",
    excerpt:
      "Before any new septic installation, Ohio health departments require perk testing. Here's what it is, why it's necessary, and what results mean for your system.",
    category: "Septic Installation",
    readTime: "4 min read",
    date: "January 2026",
    featured: false,
  },
];

export default function BlogPage() {
  const featured = ARTICLES.filter((a) => a.featured);
  const rest = ARTICLES.filter((a) => !a.featured);

  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-emerald-600 text-white border-0">Septic Education</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
            Septic &amp; Excavation Blog
          </h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            Honest advice for Ohio homeowners. Learn how to protect your septic system, understand your excavation options, and avoid costly repairs.
          </p>
        </div>
      </section>

      {/* Featured Articles */}
      <section className="py-12 bg-white border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-xl font-bold text-slate-900 mb-6">Featured Articles</h2>
          <div className="grid md:grid-cols-2 gap-6">
            {featured.map((article) => (
              <Card key={article.slug} className="bg-slate-50 border-0 overflow-hidden hover:shadow-md transition-shadow">
                <CardContent className="p-6">
                  <div className="flex items-center gap-3 mb-3">
                    <Badge className="bg-emerald-100 text-emerald-700 border-0 text-xs">{article.category}</Badge>
                    <span className="text-xs text-slate-400 flex items-center gap-1">
                      <Clock className="w-3 h-3" />
                      {article.readTime}
                    </span>
                  </div>
                  <h3 className="font-bold text-lg text-slate-900 mb-2 hover:text-emerald-600 transition-colors cursor-pointer">
                    {article.title}
                  </h3>
                  <p className="text-sm text-slate-600 mb-4 leading-relaxed">{article.excerpt}</p>
                  <div className="flex items-center justify-between">
                    <span className="text-xs text-slate-400 flex items-center gap-1">
                      <Calendar className="w-3 h-3" />
                      {article.date}
                    </span>
                    <Link
                      href={`/blog/${article.slug}`}
                      className="text-sm font-semibold text-emerald-600 hover:text-emerald-700 flex items-center gap-1"
                    >
                      Read More <ArrowRight className="w-3 h-3" />
                    </Link>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* All Articles */}
      <section className="py-16 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-xl font-bold text-slate-900 mb-6">All Articles</h2>
          <div className="grid md:grid-cols-3 gap-6">
            {rest.map((article) => (
              <Card key={article.slug} className="bg-white border-0 shadow-sm hover:shadow-md transition-shadow">
                <CardContent className="p-5">
                  <div className="flex items-center gap-3 mb-3">
                    <Badge className="bg-slate-100 text-slate-700 border-0 text-xs">{article.category}</Badge>
                    <span className="text-xs text-slate-400 flex items-center gap-1">
                      <Clock className="w-3 h-3" />
                      {article.readTime}
                    </span>
                  </div>
                  <h3 className="font-bold text-slate-900 mb-2 leading-snug hover:text-emerald-600 transition-colors cursor-pointer">
                    {article.title}
                  </h3>
                  <p className="text-sm text-slate-600 mb-4 leading-relaxed line-clamp-3">
                    {article.excerpt}
                  </p>
                  <div className="flex items-center justify-between">
                    <span className="text-xs text-slate-400 flex items-center gap-1">
                      <Calendar className="w-3 h-3" />
                      {article.date}
                    </span>
                    <Link
                      href={`/blog/${article.slug}`}
                      className="text-sm font-semibold text-emerald-600 hover:text-emerald-700 flex items-center gap-1"
                    >
                      Read More <ArrowRight className="w-3 h-3" />
                    </Link>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* Newsletter CTA */}
      <section className="py-14 bg-emerald-600 text-white">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-2xl font-bold mb-3">Get Tips Straight to Your Inbox</h2>
          <p className="text-emerald-100 mb-6">
            Subscribe for seasonal septic maintenance reminders and honest advice — no spam, ever.
          </p>
          <div className="flex flex-col sm:flex-row gap-3 max-w-md mx-auto">
            <input
              type="email"
              placeholder="your@email.com"
              className="flex-1 h-11 rounded-lg px-4 text-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-emerald-300"
            />
            <button className="h-11 px-6 bg-slate-900 hover:bg-slate-800 text-white font-semibold rounded-lg transition-colors">
              Subscribe
            </button>
          </div>
          <p className="text-xs text-emerald-200 mt-3">No spam. Unsubscribe anytime.</p>
        </div>
      </section>
    </>
  );
}

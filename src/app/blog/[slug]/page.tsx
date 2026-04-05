import type { Metadata } from "next";
import { notFound } from "next/navigation";
import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Calendar, Clock, ArrowLeft } from "lucide-react";
import { getArticleBySlug, getAllSlugs, BLOG_ARTICLES } from "@/lib/blog-data";

type Props = { params: Promise<{ slug: string }> };

export async function generateStaticParams() {
  return getAllSlugs().map((slug) => ({ slug }));
}

export async function generateMetadata({ params }: Props): Promise<Metadata> {
  const { slug } = await params;
  const article = getArticleBySlug(slug);
  if (!article) return {};
  return {
    title: article.title,
    description: article.excerpt,
  };
}

export default async function BlogArticlePage({ params }: Props) {
  const { slug } = await params;
  const article = getArticleBySlug(slug);
  if (!article) notFound();

  const related = BLOG_ARTICLES.filter(
    (a) => a.slug !== slug && a.category === article.category
  ).slice(0, 3);

  // Simple markdown-to-HTML for basic formatting
  const htmlContent = article.content
    .replace(/^## (.+)$/gm, '<h2 class="text-2xl font-bold text-slate-900 mt-8 mb-4">$1</h2>')
    .replace(/^### (.+)$/gm, '<h3 class="text-xl font-bold text-slate-800 mt-6 mb-3">$1</h3>')
    .replace(/\*\*(.+?)\*\*/g, '<strong class="font-semibold">$1</strong>')
    .replace(/\*(.+?)\*/g, '<em>$1</em>')
    .replace(/\n\n/g, '</p><p class="text-slate-700 leading-relaxed mb-4">')
    .replace(/^- (.+)$/gm, '<li class="ml-4 list-disc text-slate-700 mb-1">$1</li>')
    .trim();

  return (
    <>
      {/* Article Hero */}
      <section className="bg-slate-900 text-white py-14 md:py-18">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
          <Link
            href="/blog"
            className="inline-flex items-center gap-2 text-slate-400 hover:text-white text-sm mb-6 transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
            Back to Blog
          </Link>
          <div className="flex items-center gap-3 mb-4">
            <Badge className="bg-primary text-white border-0 text-xs">
              {article.category}
            </Badge>
            <span className="text-slate-400 text-sm flex items-center gap-1">
              <Clock className="w-3 h-3" />
              {article.readTime}
            </span>
            <span className="text-slate-400 text-sm flex items-center gap-1">
              <Calendar className="w-3 h-3" />
              {article.date}
            </span>
          </div>
          <h1 className="text-3xl md:text-4xl font-extrabold leading-tight">
            {article.title}
          </h1>
        </div>
      </section>

      {/* Article Content */}
      <section className="py-12 bg-white">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
          <p className="text-lg text-slate-600 font-medium mb-8 leading-relaxed border-l-4 border-primary pl-4 italic">
            {article.excerpt}
          </p>
          <div
            className="prose max-w-none"
            dangerouslySetInnerHTML={{
              __html: `<p class="text-slate-700 leading-relaxed mb-4">${htmlContent}</p>`,
            }}
          />
        </div>
      </section>

      {/* CTA */}
      <section className="py-12 bg-secondary/10 border-t">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h3 className="text-xl font-bold text-slate-900 mb-2">Have a Septic Problem?</h3>
          <p className="text-slate-600 mb-6">
            Skip the Google search — talk to a real professional. We serve Warren, Ohio and surrounding Trumbull County.
          </p>
          <a
            href="tel:3309793930"
            className="inline-flex items-center gap-2 bg-primary hover:bg-primary/90 text-white font-bold px-8 py-3 rounded-lg transition-colors"
          >
            Call (330) 979-3930
          </a>
        </div>
      </section>

      {/* Related Articles */}
      {related.length > 0 && (
        <section className="py-12 bg-slate-50">
          <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
            <h2 className="text-xl font-bold text-slate-900 mb-6">Related Articles</h2>
            <div className="space-y-4">
              {related.map((r) => (
                <Link
                  key={r.slug}
                  href={`/blog/${r.slug}`}
                  className="block bg-white rounded-lg p-5 shadow-sm hover:shadow-md transition-shadow border border-slate-100"
                >
                  <div className="flex items-center gap-2 mb-2">
                    <Badge className="bg-secondary/10 text-primary border-0 text-xs">
                      {r.category}
                    </Badge>
                    <span className="text-xs text-slate-400">{r.readTime}</span>
                  </div>
                  <h3 className="font-semibold text-slate-900 hover:text-secondary transition-colors">
                    {r.title}
                  </h3>
                  <p className="text-sm text-slate-500 mt-1 line-clamp-2">{r.excerpt}</p>
                </Link>
              ))}
            </div>
          </div>
        </section>
      )}
    </>
  );
}

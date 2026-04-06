import { MetadataRoute } from "next";

export default function robots(): MetadataRoute.Robots {
  return {
    rules: [
      {
        // Allow all standard crawlers
        userAgent: "*",
        allow: "/",
        disallow: ["/api/", "/_next/"],
      },
      // AI Training Crawlers — ALLOWED
      // Allowing these helps the site get into AI training data and enables
      // citations in ChatGPT, Claude, Perplexity, and other AI responses.
      // For a local small business, being findable via AI search is valuable.
      {
        userAgent: "GPTBot",
        allow: "/",
      },
      {
        userAgent: "ChatGPT-User",
        allow: "/",
      },
      {
        userAgent: "ClaudeBot",
        allow: "/",
      },
      {
        userAgent: "PerplexityBot",
        allow: "/",
      },
      {
        userAgent: "CCBot",
        allow: "/",
      },
      {
        userAgent: "Bytespider",
        allow: "/",
      },
      {
        userAgent: "Google-Extended",
        allow: "/",
      },
      {
        userAgent: "anthropic-ai",
        allow: "/",
      },
      // Only block clearly malicious or non-beneficial bots
      {
        userAgent: "AhrefsBot",
        disallow: "/",
      },
    ],
    sitemap: "https://spseptic.com/sitemap.xml",
  };
}

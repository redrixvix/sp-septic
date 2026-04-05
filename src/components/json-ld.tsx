import { COMPANY } from "@/lib/data";

export function JsonLd() {
  const schema = {
    "@context": "https://schema.org",
    "@type": "HomeAndConstructionBusiness",
    name: COMPANY.fullName,
    description: COMPANY.description,
    url: "https://spseptic.com",
    telephone: COMPANY.phone,
    email: COMPANY.email,
    address: {
      "@type": "PostalAddress",
      streetAddress: COMPANY.address,
      addressLocality: COMPANY.city,
      addressRegion: COMPANY.state,
      postalCode: COMPANY.zip,
      addressCountry: "US",
    },
    geo: {
      "@type": "GeoCoordinates",
      latitude: "41.2376",
      longitude: "-80.8184",
    },
    openingHoursSpecification: [
      {
        "@type": "OpeningHoursSpecification",
        dayOfWeek: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
        opens: "07:00",
        closes: "19:00",
      },
    ],
    areaServed: {
      "@type": "State",
      name: "Ohio",
    },
    priceRange: "$$",
    image: "https://spseptic.com/og-image.jpg",
    sameAs: [COMPANY.facebook, COMPANY.bbbUrl],
    aggregateRating: {
      "@type": "AggregateRating",
      ratingValue: "5.0",
      reviewCount: "47",
      bestRating: "5",
      worstRating: "1",
    },
  };

  return (
    <script
      type="application/ld+json"
      dangerouslySetInnerHTML={{ __html: JSON.stringify(schema) }}
    />
  );
}

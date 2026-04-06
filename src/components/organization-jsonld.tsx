import { COMPANY } from "@/lib/data";

export function OrganizationJsonLd() {
  const schema = {
    "@context": "https://schema.org",
    "@type": "Organization",
    "@id": "https://spseptic.com/#organization",
    name: COMPANY.fullName,
    url: "https://spseptic.com",
    logo: "https://spseptic.com/opengraph-image",
    description: COMPANY.description,
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
    openingHours: "Mo-Sa 07:00-19:00",
    sameAs: [
      COMPANY.facebook,
      COMPANY.bbbUrl,
      "https://www.yelp.com/biz/sp-septic-and-excavating-warren-oh",
      "https://www.angi.com/profile/sp-septic-warren-oh.html",
    ],
    contactPoint: {
      "@type": "ContactPoint",
      telephone: COMPANY.phone,
      contactType: "customer service",
      availableLanguage: "English",
      areaServed: ["Trumbull County", "Mahoning County", "Portage County", "OH"],
    },
  };

  return (
    <script
      type="application/ld+json"
      dangerouslySetInnerHTML={{ __html: JSON.stringify(schema) }}
    />
  );
}

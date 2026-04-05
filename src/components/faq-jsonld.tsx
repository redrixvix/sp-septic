const FAQ_SCHEMA = [
  {
    question: "How often should I pump my septic tank?",
    answer: "Most residential septic tanks need pumping every 3-5 years, depending on household size, tank capacity, and water usage. We can assess your system and set up a maintenance schedule.",
  },
  {
    question: "What are signs my septic system is failing?",
    answer: "Watch for slow drains throughout the house, gurgling sounds from pipes, sewage odors inside or outside, wet spots over the drain field, and bright green grass patches. Call us immediately if you notice any of these.",
  },
  {
    question: "How long does a new septic system installation take?",
    answer: "A typical new installation takes 2-5 days depending on soil conditions, system size, and permitting. We manage everything from permits to final inspection.",
  },
  {
    question: "Do I need a permit for septic work in Ohio?",
    answer: "Yes. All new septic installations and major repairs require permits from your local health department. We handle all permit applications as part of our service.",
  },
  {
    question: "What forms of payment do you accept?",
    answer: "We accept cash, check, and all major credit cards. Payment is due upon completion of work with clear upfront pricing before any work begins.",
  },
  {
    question: "Do you offer free estimates?",
    answer: "Yes! We offer free on-site assessments and written estimates before any work begins. Know exactly what you're paying before we touch a thing.",
  },
];

export function FaqJsonLd() {
  return (
    <script
      type="application/ld+json"
      dangerouslySetInnerHTML={{
        __html: JSON.stringify({
          "@context": "https://schema.org",
          "@type": "FAQPage",
          mainEntity: FAQ_SCHEMA.map((faq) => ({
            "@type": "Question",
            name: faq.question,
            acceptedAnswer: {
              "@type": "Answer",
              text: faq.answer,
            },
          })),
        }),
      }}
    />
  );
}

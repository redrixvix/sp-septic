import { ContactPageClient } from "./ContactForm";
import { BreadcrumbJsonLd } from "@/components/breadcrumb-jsonld";

export const metadata = {
  title: "Contact Us — Get a Free Estimate",
  description:
    "Contact S&P Septic & Excavating for a free estimate. Serving Warren, Ohio and Trumbull County. Call (330) 979-3930 or use our online form.",
};

export default function ContactPage() {
  return (
    <>
      <BreadcrumbJsonLd items={[{ name: "Home", url: "https://spseptic.com" }, { name: "Contact", url: "https://spseptic.com/contact" }]} />
      <ContactPageClient />
    </>
  );
}
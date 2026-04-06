import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Accessibility Statement | S&P Septic & Excavating",
  description:
    "Accessibility statement for S&P Septic & Excavating website. We are committed to ensuring digital accessibility for all users.",
};

export default function AccessibilityStatement() {
  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">Accessibility Statement</h1>
          <p className="text-slate-300 text-lg">
            S&P Septic & Excavating is committed to ensuring digital accessibility for people with disabilities.
          </p>
        </div>
      </section>

      {/* Content */}
      <section className="py-16 md:py-20 bg-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="prose prose-slate max-w-none">
            <p className="text-lg text-slate-600 mb-8">
              Last reviewed: April 6, 2026
            </p>

            <h2 className="text-2xl font-bold text-slate-900 mt-8 mb-4">Our Commitment</h2>
            <p className="text-slate-600 mb-6">
              We believe that access to information is a fundamental right. We are continuously working to improve the accessibility of our website to ensure it is usable by all people, including those with disabilities.
            </p>

            <h2 className="text-2xl font-bold text-slate-900 mt-8 mb-4">Accessibility Features</h2>
            <ul className="list-disc pl-6 text-slate-600 space-y-2 mb-6">
              <li>
                <strong>Skip Navigation</strong> — A "Skip to main content" link is provided as the first focusable element, allowing keyboard users to bypass the navigation and jump directly to page content.
              </li>
              <li>
                <strong>Keyboard Navigation</strong> — All interactive elements, including navigation menus, forms, and buttons, are fully keyboard accessible using standard Tab, Enter, and Space keys.
              </li>
              <li>
                <strong>Color Contrast</strong> — Text and background color combinations meet WCAG 2.1 AA standards (minimum 4.5:1 contrast ratio for normal text, 3:1 for large text).
              </li>
              <li>
                <strong>Focus Indicators</strong> — All interactive elements have clearly visible focus states when navigating via keyboard.
              </li>
              <li>
                <strong>Screen Reader Support</strong> — All images include descriptive alt text, form fields have associated labels, and ARIA attributes are used appropriately.
              </li>
              <li>
                <strong>Reduced Motion</strong> — Animations and transitions are disabled for users who prefer reduced motion (via the <code>prefers-reduced-motion</code> media query).
              </li>
              <li>
                <strong>Semantic HTML</strong> — Pages use proper HTML structural elements (<code>&lt;header&gt;</code>, <code>&lt;main&gt;</code>, <code>&lt;nav&gt;</code>, <code>&lt;footer&gt;</code>) for reliable screen reader interpretation.
              </li>
              <li>
                <strong>Text Resizing</strong> — Text on this site scales properly up to 200% without loss of content or functionality.
              </li>
            </ul>

            <h2 className="text-2xl font-bold text-slate-900 mt-8 mb-4">Known Limitations</h2>
            <p className="text-slate-600 mb-6">
              While we strive for full accessibility, the following areas may have limitations:
            </p>
            <ul className="list-disc pl-6 text-slate-600 space-y-2 mb-6">
              <li>
                Some older PDF documents may not be fully accessible. We are working to remediate or provide accessible alternatives.
              </li>
              <li>
                Third-party content (such as embedded Google Maps) may not be fully accessible. Contact us directly for assistance with directions or location information.
              </li>
              <li>
                Project gallery images are currently displayed as illustrative placeholders. Alt text describes the intended content type; real photography will be added in future updates.
              </li>
            </ul>

            <h2 className="text-2xl font-bold text-slate-900 mt-8 mb-4">Feedback and Contact</h2>
            <p className="text-slate-600 mb-6">
              We welcome your feedback on the accessibility of our website. If you encounter any barriers while using our site, please contact us:
            </p>
            <ul className="list-none pl-0 text-slate-600 space-y-3 mb-8">
              <li>
                <strong>Phone:</strong>{" "}
                <a href="tel:+13309793930" className="text-secondary hover:underline">
                  (330) 979-3930
                </a>{" "}
                (Mon–Sat, 7AM–7PM)
              </li>
              <li>
                <strong>Email:</strong>{" "}
                <a href="mailto:info@spseptic.com" className="text-secondary hover:underline">
                  info@spseptic.com
                </a>
              </li>
              <li>
                <strong>Address:</strong> 2900 Elm Rd NE, Warren, OH 44483
              </li>
            </ul>
            <p className="text-slate-600 mb-6">
              We aim to respond to accessibility feedback within 2 business days.
            </p>

            <h2 className="text-2xl font-bold text-slate-900 mt-8 mb-4">Compliance Status</h2>
            <p className="text-slate-600 mb-6">
              This website aims to conform to the{" "}
              <a
                href="https://www.w3.org/TR/WCAG21/"
                target="_blank"
                rel="noopener noreferrer"
                className="text-secondary hover:underline"
              >
                Web Content Accessibility Guidelines (WCAG) 2.1 Level AA
              </a>
              .
            </p>

            <h2 className="text-2xl font-bold text-slate-900 mt-8 mb-4">Technical Information</h2>
            <p className="text-slate-600 mb-4">
              This website is built using modern web technologies including:
            </p>
            <ul className="list-disc pl-6 text-slate-600 space-y-2 mb-6">
              <li>Next.js (React framework)</li>
              <li>Tailwind CSS</li>
              <li>Framer Motion (animations)</li>
              <li>Vercel hosting</li>
            </ul>
          </div>
        </div>
      </section>
    </>
  );
}
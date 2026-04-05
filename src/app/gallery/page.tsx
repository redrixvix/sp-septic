import type { Metadata } from "next";
import { Badge } from "@/components/ui/badge";
import { Camera, Image as ImageIcon } from "lucide-react";

export const metadata: Metadata = {
  title: "Project Gallery",
  description: "See our septic and excavation work throughout Trumbull County, Ohio. From new installations to repairs — quality you can see.",
};

// Placeholder gallery items — replace src with real photos
const GALLERY = [
  {
    category: "Septic Installation",
    title: "New Septic System — Warren, OH",
    description: "Complete aerobic septic system installation for new home construction. 3-day project, passed all inspections.",
    location: "Warren, OH",
    year: "2025",
  },
  {
    category: "Excavation",
    title: "Driveway Grading & Trenching",
    description: "Professional excavation for new driveway installation. Proper grading ensured excellent drainage.",
    location: "Cortland, OH",
    year: "2025",
  },
  {
    category: "Septic Repair",
    title: "Drain Field Restoration",
    description: "Failed drain field replacement. Complete excavation, new gravel bed, and fresh soil. System works like new.",
    location: "Niles, OH",
    year: "2025",
  },
  {
    category: "Septic Pumping",
    title: "Annual Tank Pumping Service",
    description: "Regular maintenance pumping for a 1500-gallon residential tank. Removed 2 years of sludge buildup.",
    location: "Girard, OH",
    year: "2025",
  },
  {
    category: "Septic Installation",
    title: "Conventional System Replacement",
    description: "Full replacement of an aging conventional system. New tank, distribution box, and leach field.",
    location: "Liberty Township, OH",
    year: "2024",
  },
  {
    category: "Excavation",
    title: "Utility Trenching",
    description: "Underground utility installation trenching. Precise grading for water line and sewer hookup.",
    location: "Howland Township, OH",
    year: "2024",
  },
  {
    category: "Camera Inspection",
    title: "Pre-Purchase Septic Inspection",
    description: "Comprehensive camera inspection for home buyer. Full video documentation of tank and line condition.",
    location: "Warren, OH",
    year: "2025",
  },
  {
    category: "Septic Repair",
    title: "Emergency Line Repair",
    description: "Same-day emergency service. Busted line between house and tank causing surface seepage. Fixed within hours.",
    location: "Niles, OH",
    year: "2025",
  },
  {
    category: "Excavation",
    title: "Pond Aeration System Installation",
    description: "Large-scale excavation for pond aeration system. Installed circulation system for water quality management.",
    location: "Trumbull County, OH",
    year: "2024",
  },
];

export default function GalleryPage() {
  return (
    <>
      {/* Hero */}
      <section className="bg-slate-900 text-white py-16 md:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <Badge className="mb-4 bg-emerald-600 text-white border-0">Our Work</Badge>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-4">Project Gallery</h1>
          <p className="text-slate-300 text-lg max-w-2xl">
            Real work, real results. Browse septic installations, excavations, and repairs completed throughout Trumbull County and beyond.
          </p>
        </div>
      </section>

      {/* Intro */}
      <section className="py-12 bg-white border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-wrap gap-8 justify-center text-center">
            <div>
              <div className="text-3xl font-extrabold text-emerald-600">50+</div>
              <div className="text-sm text-slate-500">Projects Completed</div>
            </div>
            <div>
              <div className="text-3xl font-extrabold text-emerald-600">100%</div>
              <div className="text-sm text-slate-500">Inspection Pass Rate</div>
            </div>
            <div>
              <div className="text-3xl font-extrabold text-emerald-600">5+</div>
              <div className="text-sm text-slate-500">Years Serving Ohio</div>
            </div>
            <div>
              <div className="text-3xl font-extrabold text-emerald-600">0</div>
              <div className="text-sm text-slate-500">Comeback Calls</div>
            </div>
          </div>
        </div>
      </section>

      {/* Gallery Grid */}
      <section className="py-16 md:py-24 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {GALLERY.map((item) => (
              <div
                key={item.title}
                className="bg-white rounded-xl overflow-hidden shadow-sm hover:shadow-md transition-shadow group"
              >
                {/* Image placeholder — replace with real photos */}
                <div className="aspect-video bg-slate-100 relative overflow-hidden">
                  <div className="absolute inset-0 flex flex-col items-center justify-center text-slate-300">
                    <Camera className="w-10 h-10 mb-2 opacity-40" />
                    <span className="text-xs opacity-60">Photo coming soon</span>
                  </div>
                  <div className="absolute top-3 left-3">
                    <Badge className="bg-white/90 text-slate-700 border-0 text-xs shadow-sm">
                      {item.category}
                    </Badge>
                  </div>
                </div>
                <div className="p-5">
                  <h3 className="font-bold text-slate-900 mb-1 group-hover:text-emerald-600 transition-colors">
                    {item.title}
                  </h3>
                  <p className="text-sm text-slate-600 mb-3 leading-relaxed">{item.description}</p>
                  <div className="flex justify-between text-xs text-slate-400">
                    <span>{item.location}</span>
                    <span>{item.year}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="py-14 bg-slate-900 text-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl font-bold mb-4">Your Project Could Be Next</h2>
          <p className="text-slate-300 text-lg mb-8 max-w-2xl mx-auto">
            Whether it&apos;s a new septic system, excavation work, or an emergency repair — we&apos;d love to show you the S&P difference.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <a
              href="tel:3309793930"
              className="inline-flex items-center justify-center gap-2 bg-emerald-600 hover:bg-emerald-700 text-white font-semibold px-8 py-3 rounded-lg transition-colors"
            >
              Call (330) 979-3930
            </a>
            <a
              href="/contact"
              className="inline-flex items-center justify-center gap-2 border border-slate-600 hover:bg-slate-800 text-white font-semibold px-8 py-3 rounded-lg transition-colors"
            >
              Get Free Estimate
            </a>
          </div>
        </div>
      </section>
    </>
  );
}

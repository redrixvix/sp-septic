"use client";

import { Phone } from "lucide-react";
import Link from "next/link";
import { COMPANY } from "@/lib/data";

export function FloatingCTA() {
  return (
    <div className="fixed bottom-4 left-4 right-4 z-50 md:hidden">
      <a
        href={`tel:${COMPANY.phone}`}
        className="flex items-center justify-center gap-2 bg-primary hover:bg-primary/90 text-white font-bold py-3 px-6 rounded-full shadow-lg shadow-primary/20 transition-all active:scale-95"
      >
        <Phone className="w-5 h-5" />
        Call Now — {COMPANY.phone}
      </a>
    </div>
  );
}

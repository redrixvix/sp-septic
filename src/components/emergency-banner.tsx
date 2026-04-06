import { Phone } from "lucide-react";
import { COMPANY } from "@/lib/data";

export function EmergencyBanner() {
  return (
    <div className="bg-red-700 text-white py-3 text-sm text-center flex flex-wrap items-center justify-center gap-x-2 gap-y-1">
      <span className="font-semibold">24/7 Emergency Service Available — </span>
      <a
        href={`tel:${COMPANY.phone}`}
        className="inline-flex items-center gap-1 font-bold underline underline-offset-2 hover:text-red-100 transition-colors"
      >
        <Phone className="w-3.5 h-3.5" />
        {COMPANY.phone}
      </a>
      <span className="mx-2 opacity-50">|</span>
      <span className="opacity-90">Fast response for septic emergencies</span>
    </div>
  );
}

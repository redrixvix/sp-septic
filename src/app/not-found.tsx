import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Home, ArrowLeft } from "lucide-react";

export default function NotFound() {
  return (
    <div className="min-h-screen bg-slate-50 flex items-center justify-center px-4">
      <div className="text-center max-w-md">
        <div className="text-8xl font-black text-slate-200 mb-4">404</div>
        <h1 className="text-2xl font-bold text-slate-900 mb-3">Page Not Found</h1>
        <p className="text-slate-600 mb-8 leading-relaxed">
          Sorry, the page you&apos;re looking for doesn&apos;t exist. Head back home or use the navigation to find what you need.
        </p>
        <div className="flex flex-col sm:flex-row gap-3 justify-center">
          <Link href="/">
            <Button className="gap-2 bg-primary hover:bg-primary/90 font-semibold">
              <Home className="w-4 h-4" />
              Go Home
            </Button>
          </Link>
          <Link href="/contact">
            <Button variant="outline" className="gap-2">
              <ArrowLeft className="w-4 h-4" />
              Contact Us
            </Button>
          </Link>
        </div>
      </div>
    </div>
  );
}

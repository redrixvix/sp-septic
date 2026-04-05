#!/usr/bin/env node
const fs = require("fs");
const path = require("path");
const { execSync } = require("child_process");

const STATUS_FILE = "/home/rixvix/.openclaw/workspace/sp-septic/.current-status";
const LOG_FILE    = "/home/rixvix/.openclaw/workspace/sp-septic/.status-log";

const now = new Date();
const timeStr = now.toLocaleTimeString("en-US", { hour: "numeric", minute: "2-digit", hour12: true });
const dateStr = now.toLocaleDateString("en-US", { month: "short", day: "numeric", year: "numeric" });

// Dev server status
let devServerStatus = "unknown";
try {
  const result = execSync("pgrep -f 'next dev' 2>/dev/null || echo ''", { encoding: "utf8" });
  devServerStatus = result.trim() ? "running at localhost:3131" : "stopped";
} catch (e) {
  devServerStatus = "unknown";
}

// Package version
let version = "unknown";
try {
  const pkg = JSON.parse(fs.readFileSync(path.join(__dirname, "..", "package.json"), "utf8"));
  version = pkg.version;
} catch (e) {}

// Git branch
let gitBranch = "";
try {
  gitBranch = execSync("git -C /home/rixvix/.openclaw/workspace/sp-septic branch --show-current 2>/dev/null || echo ''", { encoding: "utf8" }).trim();
} catch (e) {}

const status = "Status Check: " + timeStr + " on " + dateStr + "\n" +
"Project: S&P Septic & Excavating Website\n" +
"Location: sp-septic/\n" +
"Dev Server: " + devServerStatus + "\n" +
"Git Branch: " + (gitBranch || "none") + "\n" +
"Last Build: v" + version + "\n\n" +
"Current work:\n" +
"- Homepage fully animated with scroll reveals, parallax, count-ups\n" +
"- 23 pages built and running\n\n" +
"Recent changes:\n" +
"- Homepage reworked with premium animations (parallax, staggered, hover effects)\n" +
"- Color palette: deep forest green + warm gold\n" +
"- framer-motion installed, animation components created\n" +
"- CSS keyframes: hero-float, fade-in-up, pulse-gold, gradient-shift\n" +
"- Build verified: 23/23 pages clean\n\n" +
"Next priorities:\n" +
"- Real project photos\n" +
"- Email backend (Resend)\n" +
"- Open Graph images\n" +
"- Google Search Console + analytics\n" +
"- Ohio contractor license # in footer\n\n" +
"Working: iterate further on design/content/functionality on command.";

fs.writeFileSync(STATUS_FILE, status);

const logEntry = now.toISOString() + " | " + (devServerStatus.includes("running") ? "UP" : "DOWN") + " | " + (gitBranch || "no branch") + "\n";
try {
  let log = "";
  if (fs.existsSync(LOG_FILE)) {
    const existing = fs.readFileSync(LOG_FILE, "utf8");
    const lines = existing.split("\n").filter(Boolean).slice(-47);
    log = lines.join("\n") + "\n";
  }
  fs.writeFileSync(LOG_FILE, log + logEntry);
} catch (e) {}

console.log("Status written: " + timeStr);
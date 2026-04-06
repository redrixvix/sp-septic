#!/usr/bin/env node
const fs = require("fs");
const path = require("path");
const { execSync } = require("child_process");

const STATUS_FILE   = "/home/rixvix/.openclaw/workspace/sp-septic/.current-status";
const ACTIVE_FILE   = "/home/rixvix/.openclaw/workspace/sp-septic/.active-work.txt";
const LOG_FILE      = "/home/rixvix/.openclaw/workspace/sp-septic/.status-log";

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

// Git branch + recent commit check
let gitBranch = "";
let recentCommit = false;
try {
  gitBranch = execSync("git -C /home/rixvix/.openclaw/workspace/sp-septic branch --show-current 2>/dev/null || echo ''", { encoding: "utf8" }).trim();
  // Check for commit in last 45 minutes
  const since = Date.now() - 45 * 60 * 1000;
  const commitTime = execSync("git -C /home/rixvix/.openclaw/workspace/sp-septic log -1 --format=%ct 2>/dev/null || echo '0'", { encoding: "utf8" }).trim();
  recentCommit = parseInt(commitTime) * 1000 > since;
} catch (e) {}

// Active work check
let activeWork = null;
if (fs.existsSync(ACTIVE_FILE)) {
  try {
    const content = fs.readFileSync(ACTIVE_FILE, "utf8").trim();
    const fileAge = (now.getTime() - fs.statSync(ACTIVE_FILE).mtimeMs) / 1000 / 60; // minutes
    if (content && fileAge > 45 && !recentCommit) {
      activeWork = content;
    }
  } catch (e) {}
}

const status = "Status Check: " + timeStr + " on " + dateStr + "\n" +
"Project: S&P Septic & Excavating Website\n" +
"Location: sp-septic/\n" +
"Dev Server: " + devServerStatus + "\n" +
"Git Branch: " + (gitBranch || "none") + "\n" +
"Last Build: v" + version + "\n" +
(recentCommit ? "Recent commit: YES (active development)\n" : "Recent commit: no\n") +
(activeWork ? "\n!!! SILENT WORK DETECTED !!!\n" + activeWork + "\n" : "\n");

fs.writeFileSync(STATUS_FILE, status);

const logEntry = now.toISOString() + " | " + (devServerStatus.includes("running") ? "UP" : "DOWN") + " | " + (gitBranch || "no branch") + (activeWork ? " | ⚠️ SILENT" : " | OK") + "\n";
try {
  let log = "";
  if (fs.existsSync(LOG_FILE)) {
    const existing = fs.readFileSync(LOG_FILE, "utf8");
    const lines = existing.split("\n").filter(Boolean).slice(-47);
    log = lines.join("\n") + "\n";
  }
  fs.writeFileSync(LOG_FILE, log + logEntry);
} catch (e) {}

console.log("Status written: " + timeStr + (activeWork ? " [⚠️ SILENT WORK DETECTED]" : " [OK]"));
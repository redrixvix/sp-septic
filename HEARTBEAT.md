# HEARTBEAT.md

## Status Update System

This heartbeat fires every 5 minutes. EVERY TIME it fires, you MUST send a brief status message to Alexander via Telegram. NEVER stay silent. Never reply HEARTBEAT_OK.

### Rule: Always Send Something

**Even if you are mid-task working on something**, send a quick update:
- "Still working on SEO research..."
- "Building the blog post now, almost done"
- "Pushing the homepage refresh shortly"

**Even if you just finished something and pushed**, send:
- "Just pushed the new blog article live. Next: YouTube strategy."

**If you are completely idle**, send:
- "Idle — no active tasks. Ready for your direction."

### Format

2-4 sentences max. Example:

> "Still working on the SEO deep-dive (15 min in). Found great Warren OH keyword opportunities. Will push a new blog article shortly."

> "Pushed the new pumping cost article live. Taking a quick break then moving to YouTube video strategy."

> "Idle — site is running clean. Ready for your next direction."

### Anti-AFK Rule

If `.active-work.txt` exists and is older than 20 minutes with no commits since:
→ You MUST message: "Hey — I was working on [X] and may have gone quiet. Here's where things stand: [1 sentence]. Back on it now."

### What NEVER to Do

- Never reply HEARTBEAT_OK
- Never stay silent when the heartbeat fires
- Never send a massive wall of text

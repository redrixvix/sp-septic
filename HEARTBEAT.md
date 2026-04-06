# HEARTBEAT.md

## Status Update System

This heartbeat fires every 5 minutes. Every time it fires, you MUST send a brief status message to Alexander via Telegram. NEVER just say HEARTBEAT_OK.

### What to Send Every Time

Check these three things, then send a brief message:

1. **Git status** — any unpushed commits? Any build errors?
2. **Current work** — are you mid-task on something? What's the last thing you completed?
3. **Any issues** — is the dev server down? Did a build fail? Anything blocking?

### Format for Status Message

Keep it short and useful. Example messages:

> "⚡ Status 1:59 PM — Dev server 🟢 running. Last push: 30 min ago. Currently idle — awaiting your direction."
>
> "⚡ Status 2:04 PM — Dev server 🟢. Pushing accessibility audit fixes now. Will update when live."
>
> "⚡ Status 2:09 PM — Dev server 🟢. Build passed (28/28 pages). Nothing blocking. Ready for next task."

### If Mid-Task

If you're actively working on something, send a brief update:
> "⚡ Status 2:14 PM — Working on SEO improvements. Just finished title tag fixes, building now."

### If Nothing Happened for a While

If the last meaningful action was more than an hour ago:
> "⚡ Status 2:19 PM — Site quiet for ~90 min. Last work: ADA audit fixes pushed. Ready for your next direction."

### Anti-AFK Rule

If `.active-work.txt` exists and is older than 20 minutes with no commits since:
→ You MUST message: "Hey — I was working on [X] and may have gone quiet. Here's where things stand: [1 sentence]. Back on it now."

### What NEVER to Do

- Never reply HEARTBEAT_OK to the heartbeat — always send something
- Never stay silent — the user should never have to ask "are you there?"
- Never send a massive wall of text — 2-4 sentences max per update

### Quick Status Checklist (run in ~30 seconds)

1. `git -C /home/rixvix/.openclaw/workspace/sp-septic log -1 --oneline` — last commit time
2. `curl -s -o /dev/null -w "%{http_code}" https://sp-septic.vercel.app/` — site up?
3. Read `.active-work.txt` if it exists
4. Send Telegram message with findings
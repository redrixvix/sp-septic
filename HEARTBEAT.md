# HEARTBEAT.md

## Silent Work Prevention

This file drives the heartbeat agent. Follow it strictly.

### Active Work Tracking

When working on a task that takes more than 20 minutes:
- Write a short status to `.active-work.txt` in the workspace
- Format: `[TIMESTAMP] Working on: [what you're doing]. Next update in ~20min.`
- Update it every time you make significant progress or a decision
- When the task is done, delete the file or write `[DONE]`

### Heartbeat Check Rules

Every 30 minutes, the cron job runs. When it fires:

1. **Read `.active-work.txt`** if it exists
2. **If file is older than 45 minutes** and no commit/push has happened since:
   → The agent went silent. This is a bug.
   → The agent should wake up, assess what was being worked on,
     and message the user: "Hey — I was working on [X] and went quiet. Here's where things stand: [quick status]. I'm back on it now."
3. **If file is fresh (< 45 min old)** → agent is active, do nothing (HEARTBEAT_OK)
4. **If file doesn't exist** and no commits in last hour → agent is idle, do nothing (HEARTBEAT_OK)

### Status Reporter

The `scripts/status-reporter.js` cron job runs every 30 minutes. It writes to `.current-status`.
If the agent goes silent, the status file will show what was last being worked on,
so the agent can recover context quickly.

### Quick Status Format

When updating `.active-work.txt`, keep it to one line:
```
[YYYY-MM-DD HH:MM] TASK: <what> | STATUS: <in-progress/done/blocked> | NEXT: <what comes next>
```

This is the safety net. The goal: **the user should never have to ask "what happened, did you go AFK again?"**
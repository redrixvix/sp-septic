#!/bin/bash
# Status reporter script — run every 30 minutes via cron

STATUS_FILE="/home/rixvix/.openclaw/workspace/sp-septic/.current-status"

# Read current status if it exists
if [ -f "$STATUS_FILE" ]; then
  STATUS=$(cat "$STATUS_FILE")
else
  STATUS="Idle — no active project"
fi

echo "⚡ Status Check $(date '+%l:%M %p')" > /tmp/status-output.txt
echo "" >> /tmp/status-output.txt
echo "$STATUS" >> /tmp/status-output.txt
echo "" >> /tmp/status-output.txt
echo "_That's where things stand right now._" >> /tmp/status-output.txt

cat /tmp/status-output.txt
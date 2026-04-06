#!/usr/bin/env python3
import sys

DATA_FILE = "/home/rixvix/.openclaw/workspace/sp-septic/src/lib/blog-data.ts"
ARTICLE_FILE = "/home/rixvix/.openclaw/workspace/sp-septic/scripts/new-pumping-article.ts"

with open(ARTICLE_FILE) as f:
    article = f.read().rstrip("\n,")

with open(DATA_FILE) as f:
    lines = f.readlines()

# Find the line that closes the array: "];"
closing_idx = None
for i, line in enumerate(lines):
    stripped = line.strip()
    if stripped == "];":
        closing_idx = i
        break

if closing_idx is None:
    print("ERROR: could not find ];")
    sys.exit(1)

print(f"Found ]; at line {closing_idx+1}")

# Insert article BEFORE the "];"
new_lines = lines[:closing_idx]
new_lines.append(article + "\n")
new_lines.append("];\n")
new_lines.extend(lines[closing_idx+1:])

with open(DATA_FILE, "w") as f:
    f.writelines(new_lines)

# Verify
with open(DATA_FILE) as f:
    content = f.read()
print(f"Done. Articles: {content.count('slug:')}")
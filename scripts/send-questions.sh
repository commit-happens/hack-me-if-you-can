#!/usr/bin/env bash
# Čte otázku z JSON a uloží ji do DB
set -euo pipefail

INPUT_FILE="${1:-questions_draft.json}"
API_URL="${API_URL:-http://localhost:8080}"

[ -f "$INPUT_FILE" ] || { echo "❌ Soubor neexistuje: $INPUT_FILE"; exit 1; }
curl -fsS --max-time 2 "$API_URL/hello" >/dev/null || {
    echo "❌ Backend není dostupný"; exit 1
}

REQUESTS=$(jq '
    if type == "array" then . else [.] end
    | map({
        platform: ((.platform // "") | ascii_downcase),
        subject: (.subject // .metadata.subject // ""),
        sender: (.sender // .metadata.sender // ""),
        content: (.content // ""),
        explanation: (.explanation // ""),
        category_tag: (.category_tag // .category // ""),
        difficulty: ((.difficulty // "") | ascii_upcase),
        is_phishing: (.is_phishing // ((.category_tag // .category // "") | ascii_upcase != "LEGIT"))
    })
' "$INPUT_FILE")

COUNT=$(echo "$REQUESTS" | jq 'length')
[ "$COUNT" -gt 0 ] || { echo "❌ V souboru nejsou žádné otázky"; exit 1; }

echo "📋 Payload ($COUNT otázek):"
echo "$REQUESTS" | jq .
read -r -p "Odeslat? (y/n): " confirm
[ "$confirm" = "y" ] || { echo "❌ Zrušeno"; exit 0; }

RESPONSE=$(curl -sS -X POST "$API_URL/questions/batch" -H "Content-Type: application/json" -d "$REQUESTS" -w "\n%{http_code}")
HTTP_CODE=$(echo "$RESPONSE" | tail -n 1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "200" ] || [ "$HTTP_CODE" = "201" ]; then
    echo "✅ Uloženo (HTTP $HTTP_CODE)"
    echo "$BODY" | jq .
else
    echo "❌ Selhalo (HTTP $HTTP_CODE)"
    echo "$BODY" | jq . 2>/dev/null || echo "$BODY"
    exit 1
fi

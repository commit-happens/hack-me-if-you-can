#!/usr/bin/env bash
# Čte otázku z JSON a uloží ji do DB
set -euo pipefail

INPUT_FILE="${1:-questions_draft.json}"
API_URL="${API_URL:-http://localhost:8080}"

[ -f "$INPUT_FILE" ] || { echo "❌ Soubor neexistuje: $INPUT_FILE"; exit 1; }
curl -fsS --max-time 2 "$API_URL/hello" >/dev/null || {
    echo "❌ Backend není dostupný"; exit 1
}

# Extrahuj první otázku
Q=$(jq '.[0]' "$INPUT_FILE")
PLATFORM=$(echo "$Q" | jq -r '.platform')
CATEGORY=$(echo "$Q" | jq -r '.category_tag // .category' | tr '[:lower:]' '[:upper:]')
DIFFICULTY=$(echo "$Q" | jq -r '.difficulty' | tr '[:lower:]' '[:upper:]')
PHISHING=$(echo "$Q" | jq -r '.is_phishing // false')

# Ověř kategorii
curl -fsS "$API_URL/phishing-categories/$CATEGORY" >/dev/null || {
    echo "❌ Kategorie $CATEGORY neexistuje"; exit 1
}

# Payload dle platformy
case "$PLATFORM" in
    EMAIL)
        EP="/questions/email"
        PAYLOAD=$(jq -n \
            --arg subject "$(echo "$Q" | jq -r '.subject // ""')" \
            --arg sender "$(echo "$Q" | jq -r '.sender // ""')" \
            --arg content "$(echo "$Q" | jq -r '.content // ""')" \
            --arg explanation "$(echo "$Q" | jq -r '.explanation // ""')" \
            --arg category_tag "$CATEGORY" \
            --arg difficulty "$DIFFICULTY" \
            --argjson is_phishing "$PHISHING" \
            '{subject:$subject, sender:$sender, content:$content, explanation:$explanation, category_tag:$category_tag, difficulty:$difficulty, is_phishing:$is_phishing}')
        ;;
    SMS)
        EP="/questions/sms"
        PAYLOAD=$(jq -n \
            --arg sender "$(echo "$Q" | jq -r '.sender // ""')" \
            --arg phoneNumber "$(echo "$Q" | jq -r '.phoneNumber // ""')" \
            --arg content "$(echo "$Q" | jq -r '.content // ""')" \
            --arg explanation "$(echo "$Q" | jq -r '.explanation // ""')" \
            --arg category_tag "$CATEGORY" \
            --arg difficulty "$DIFFICULTY" \
            --argjson is_phishing "$PHISHING" \
            '{sender:$sender, phoneNumber:$phoneNumber, content:$content, explanation:$explanation, category_tag:$category_tag, difficulty:$difficulty, is_phishing:$is_phishing}')
        ;;
    *) echo "❌ Neznámá platforma: $PLATFORM"; exit 1 ;;
esac

echo "📋 Payload:"
echo "$PAYLOAD" | jq .
read -p "Odeslat? (y/n): " confirm
[ "$confirm" = "y" ] || { echo "❌ Zrušeno"; exit 0; }

RESPONSE=$(curl -sS -X POST "$API_URL$EP" -H "Content-Type: application/json" -d "$PAYLOAD" -w "\n%{http_code}")
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


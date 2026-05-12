#!/usr/bin/env bash
# Interaktivně generuje otázku přes LLM
set -euo pipefail

API_URL="${API_URL:-http://localhost:8080}"
OUTPUT_FILE="questions_draft.json"

curl -fsS --max-time 2 "$API_URL/hello" >/dev/null || {
    echo "❌ Backend není dostupný"; exit 1
}

echo "📱 Vyberte platformu:"
select PLATFORM in EMAIL SMS; do [ -n "$PLATFORM" ] && break; done

echo "📂 Vyberte kategorii:"
select CATEGORY in LEGIT FAKE_URL URGENT FAKE_DOC CRED_THEFT SPEAR_PHISH LOTTERY; do
    [ -n "$CATEGORY" ] && break
done

echo "📊 Vyberte obtížnost:"
select DIFFICULTY in EASY MEDIUM HARD; do [ -n "$DIFFICULTY" ] && break; done

echo "🌍 Vyberte jazyk:"
select LANG in "cs (čeština)" "en (angličtina)"; do
    case "$LANG" in
        cs*) LANGUAGE="cs"; break ;;
        en*) LANGUAGE="en"; break ;;
    esac
done

echo ""
echo "Bude generována otázka pro parametry: $PLATFORM / $CATEGORY / $DIFFICULTY / $LANGUAGE"
read -p "Pokračovat? (y/n): " confirm
[ "$confirm" = "y" ] || { echo "❌ Zrušeno"; exit 0; }

echo "🚀 Generuji..."
URL="$API_URL/llm/generate-question?platform=$PLATFORM&category=$CATEGORY&difficulty=$DIFFICULTY&language=$LANGUAGE"
RESPONSE=$(curl -sS -X GET "$URL" -w "\n%{http_code}")
HTTP_CODE=$(echo "$RESPONSE" | tail -n 1)
BODY=$(echo "$RESPONSE" | sed '$d')

[ "$HTTP_CODE" = "200" ] || {
    echo "❌ HTTP $HTTP_CODE"
    echo "$BODY" | jq . 2>/dev/null || echo "$BODY"
    exit 1
}

RESULT=$(echo "$BODY" | jq --arg p "$PLATFORM" '. + {platform: $p}')
echo "[$RESULT]" > "$OUTPUT_FILE"

echo "✅ Uloženo do $OUTPUT_FILE"
jq . "$OUTPUT_FILE" | head -15

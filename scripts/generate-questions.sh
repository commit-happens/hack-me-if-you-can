#!/usr/bin/env bash
set -euo pipefail

API_URL="${API_URL:-http://localhost:8080}"
OUTPUT_FILE="${OUTPUT_FILE:-questions_draft.json}"

curl -fsS --max-time 2 "$API_URL/hello" >/dev/null || {
    echo "❌ Backend není dostupný"
    exit 1
}

echo "📱 Vyberte platformu:"
select PLATFORM in email sms; do [ -n "$PLATFORM" ] && break; done

LLM_PLATFORM=$(printf '%s' "$PLATFORM" | tr '[:lower:]' '[:upper:]')

echo "📂 Vyberte kategorii:"
select CATEGORY in LEGIT FAKE_URL URGENT FAKE_DOC CRED_THEFT SPEAR_PHISH LOTTERY; do [ -n "$CATEGORY" ] && break; done

curl -fsS "$API_URL/phishing-categories/$CATEGORY" >/dev/null || {
    echo "❌ Kategorie $CATEGORY neexistuje v backendu"
    exit 1
}

echo "📊 Vyberte obtížnost:"
select DIFFICULTY in EASY MEDIUM HARD; do [ -n "$DIFFICULTY" ] && break; done

echo "🌍 Vyberte jazyk:"
select LANG in "cs (čeština)" "en (angličtina)"; do
    case "$LANG" in
        cs*) LANGUAGE="cs"; break ;;
        en*) LANGUAGE="en"; break ;;
    esac
done

read -r -p "Počet otázek [1]: " COUNT
COUNT="${COUNT:-1}"
case "$COUNT" in
    ''|*[!0-9]*) echo "❌ Počet musí být celé číslo"; exit 1 ;;
esac
[ "$COUNT" -gt 0 ] || { echo "❌ Počet musí být větší než 0"; exit 1; }

IS_PHISHING=false
[ "$CATEGORY" = "LEGIT" ] || IS_PHISHING=true

echo ""
echo "Bude generováno: $COUNT × $PLATFORM / $CATEGORY / $DIFFICULTY / $LANGUAGE"
read -r -p "Pokračovat? (y/n): " confirm
[ "$confirm" = "y" ] || { echo "❌ Zrušeno"; exit 0; }

actions=()
for _ in $(seq 1 "$COUNT"); do
    URL="$API_URL/llm/generate-question"
    RESPONSE=$(curl -sS -G \
        --data-urlencode "platform=$LLM_PLATFORM" \
        --data-urlencode "category=$CATEGORY" \
        --data-urlencode "difficulty=$DIFFICULTY" \
        --data-urlencode "language=$LANGUAGE" \
        -w "\n%{http_code}" \
        "$URL")
    HTTP_CODE=$(echo "$RESPONSE" | tail -n 1)
    BODY=$(echo "$RESPONSE" | sed '$d')

    [ "$HTTP_CODE" = "200" ] || {
        echo "❌ HTTP $HTTP_CODE při generování LLM otázky"
        echo "$BODY" | jq . 2>/dev/null || echo "$BODY"
        exit 1
    }

    RESULT=$(echo "$BODY" | jq -c \
        --arg platform "$PLATFORM" \
        --arg subject "$(echo "$BODY" | jq -r '.metadata.subject // ""')" \
        --arg sender "$(echo "$BODY" | jq -r '.metadata.sender // ""')" \
        --arg content "$(echo "$BODY" | jq -r '.content // ""')" \
        --arg explanation "$(echo "$BODY" | jq -r '.explanation // ""')" \
        --arg category_tag "$CATEGORY" \
        --arg difficulty "$DIFFICULTY" \
        --argjson is_phishing "$IS_PHISHING" \
        '{platform:$platform, subject:$subject, sender:$sender, content:$content, explanation:$explanation, category_tag:$category_tag, difficulty:$difficulty, is_phishing:$is_phishing}')

    actions+=("$RESULT")
done

printf '%s\n' "${actions[@]}" | jq -s '.' > "$OUTPUT_FILE"

echo "✅ Uloženo do $OUTPUT_FILE"
jq . "$OUTPUT_FILE" | head -40

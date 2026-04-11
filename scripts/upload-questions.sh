#!/bin/bash

# Script pro načtení otázky ze souboru a uložení do databáze
# Transformuje JSON z LLM API do správného formátu pro backend API
# Použití: ./upload-questions.sh [input_file] [environment]
# Příklady:
#   ./upload-questions.sh                          (použije výchozí soubor a prostředí z .env)
#   ./upload-questions.sh questions_draft.json     (specifikuje soubor)
#   ./upload-questions.sh questions_draft.json docker  (specifikuje soubor a docker prostředí)

set -e

# ===== NAČTENÍ KONFIGURACI =====
# Načtení .env souboru
if [ -f ".env" ]; then
    source .env
else
    echo "⚠️ Upozornění: .env soubor nenalezen. Používám výchozí hodnoty."
    ENVIRONMENT="local"
fi

# Přepsání prostředí z příkazové řádky (pokud je zadáno)
ENVIRONMENT=${2:-${ENVIRONMENT:-"local"}}

# Výchozí soubor
INPUT_FILE=${1:-"questions_draft.json"}

# Sdílený fallback klíče používaný backendem
DEFAULT_INTERNAL_API_KEY="${INTERNAL_API_KEY:-}"

# Nastavení API URL a API klíče podle prostředí
case "$ENVIRONMENT" in
    local)
        API_BASE_URL="${LOCAL_API_BASE_URL:-http://localhost:8080}"
        INTERNAL_API_KEY="${LOCAL_INTERNAL_API_KEY:-$DEFAULT_INTERNAL_API_KEY}"
        ENV_NAME="LOCAL (localhost)"
        ;;
    docker)
        API_BASE_URL="${DOCKER_API_BASE_URL:-http://localhost:8080}"
        INTERNAL_API_KEY="${DOCKER_INTERNAL_API_KEY:-$DEFAULT_INTERNAL_API_KEY}"
        ENV_NAME="DOCKER (docker-compose)"
        ;;
    dev)
        API_BASE_URL="${DEV_API_BASE_URL:-}"
        INTERNAL_API_KEY="${DEV_INTERNAL_API_KEY:-$DEFAULT_INTERNAL_API_KEY}"
        ENV_NAME="DEVELOPMENT"
        ;;
    prod)
        API_BASE_URL="${PROD_API_BASE_URL:-}"
        INTERNAL_API_KEY="${PROD_INTERNAL_API_KEY:-$DEFAULT_INTERNAL_API_KEY}"
        ENV_NAME="PRODUCTION"
        ;;
    *)
        echo "❌ Chyba: Neznámé prostředí '$ENVIRONMENT'"
        echo "Povolená prostředí: local, docker, dev, prod"
        exit 1
        ;;
esac

# ===== VALIDACE VSTUPŮ =====
echo ""
echo "🔧 KONFIGURACE UPLOADU"
echo "================================="
echo "📍 Prostředí: $ENV_NAME"
echo "📁 Soubor: $INPUT_FILE"
echo "🌐 API URL: $API_BASE_URL"
echo "🔑 API Key: $([ -n "$INTERNAL_API_KEY" ] && echo "nastavena" || echo "NENÍ NASTAVENA ⚠️")"
echo "================================="
echo ""

get_category_id_from_api() {
    local category_tag="$1"
    local categories_response

    categories_response=$(curl -s -X GET \
        "$API_BASE_URL/api/admin/categories" \
        -H "X-Internal-Api-Key: $INTERNAL_API_KEY" \
        -H "Content-Type: application/json")

    echo "$categories_response" | jq -r --arg tag "$category_tag" '.[] | select(.tag == $tag) | .id' | head -n 1
}

# Kontrola INTERNAL_API_KEY
if [ -z "$INTERNAL_API_KEY" ]; then
    echo "❌ Chyba: INTERNAL_API_KEY není nastavena!"
    exit 1
fi

# Kontrola existence souboru
if [ ! -f "$INPUT_FILE" ]; then
    echo "❌ Chyba: Soubor '$INPUT_FILE' neexistuje!"
    exit 1
fi

# Kontrola připojení na server
echo "🔍 Kontroluji připojení na API ($API_BASE_URL)..."
if ! curl -s --max-time 2 "$API_BASE_URL/hello" > /dev/null 2>&1; then
    echo "❌ Chyba: Backend není dostupný na $API_BASE_URL"
    exit 1
fi
echo "✅ Backend je dostupný"

echo ""
echo "📤 Načítám jednu otázku ze souboru '$INPUT_FILE'..."
echo ""

# Parsování JSON souboru
if ! command -v jq &> /dev/null; then
    echo "❌ Chyba: jq není nainstalován. Nainstalujte jej pomocí: brew install jq"
    exit 1
fi

# Počet otázek v souboru
TOTAL_QUESTIONS=$(jq 'length' "$INPUT_FILE")
echo "Celkem otázek v souboru: $TOTAL_QUESTIONS"
echo ""

# Kontrola, zda soubor obsahuje alespoň jednu otázku
if [ "$TOTAL_QUESTIONS" -lt 1 ]; then
    echo "❌ Chyba: Soubor neobsahuje žádné otázky!"
    exit 1
fi

# Čtení pouze první otázky z JSON souboru
echo "📖 Čtu jednu otázku ze souboru..."
echo ""

# Extrakce první otázky
QUESTION=$(jq ".[0]" "$INPUT_FILE")

# Extrakce hodnot z LLM odpovědi
PLATFORM=$(echo "$QUESTION" | jq -r '.platform // ""')
CATEGORY=$(echo "$QUESTION" | jq -r '.category // ""')
DIFFICULTY=$(echo "$QUESTION" | jq -r '.difficulty // ""')
IS_PHISHING=$(echo "$QUESTION" | jq -r '.is_phishing // false')

# Detekce platformy a validace
if [ "$PLATFORM" = "EMAIL" ]; then
    QUESTION_TEXT=$(echo "$QUESTION" | jq -r '.subject // ""')
    SENDER=$(echo "$QUESTION" | jq -r '.sender // ""')
    CONTENT=$(echo "$QUESTION" | jq -r '.content // ""')
    EXPLANATION=$(echo "$QUESTION" | jq -r '.explanation // ""')
    ENDPOINT="/api/admin/questions/email"
elif [ "$PLATFORM" = "SMS" ]; then
    SENDER=$(echo "$QUESTION" | jq -r '.sender // ""')
    PHONE_NUMBER=$(echo "$QUESTION" | jq -r '.phoneNumber // ""')
    CONTENT=$(echo "$QUESTION" | jq -r '.content // ""')
    EXPLANATION=$(echo "$QUESTION" | jq -r '.explanation // ""')
    ENDPOINT="/api/admin/questions/sms"
else
    echo "❌ Chyba: Neznámá platforma: '$PLATFORM'"
    echo "Očekávaná hodnota: 'EMAIL' nebo 'SMS'"
    echo ""
    echo "Obsah otázky:"
    echo "$QUESTION" | jq '.'
    exit 1
fi

# Načtení category_id z backendu podle tagu
CATEGORY_ID=$(get_category_id_from_api "$CATEGORY")
if [ -z "$CATEGORY_ID" ]; then
    echo "❌ Chyba: Neznámá kategorie: $CATEGORY"
    echo "Nepodařilo se dohledat category_id přes API $API_BASE_URL/api/admin/categories"
    exit 1
fi

# Kontrola povinných údajů
if [ -z "$CONTENT" ] || [ -z "$EXPLANATION" ] || [ -z "$DIFFICULTY" ]; then
    echo "❌ Chyba: Chybějí povinné údaje v otázce!"
    echo ""
    echo "Obsah otázky:"
    echo "$QUESTION" | jq '.'
    exit 1
fi

# === PŘÍPRAVA JSON PAYLOADU ===
if [ "$PLATFORM" = "EMAIL" ]; then
    PAYLOAD=$(cat <<EOF
{
    "subject": $(echo "$QUESTION_TEXT" | jq -Rs .),
    "sender": $(echo "$SENDER" | jq -Rs .),
    "content": $(echo "$CONTENT" | jq -Rs .),
    "explanation": $(echo "$EXPLANATION" | jq -Rs .),
    "category_id": $CATEGORY_ID,
    "difficulty": "$DIFFICULTY",
    "is_phishing": $IS_PHISHING
}
EOF
)
else
    PAYLOAD=$(cat <<EOF
{
    "sender": $(echo "$SENDER" | jq -Rs .),
    "phoneNumber": $(echo "$PHONE_NUMBER" | jq -Rs .),
    "content": $(echo "$CONTENT" | jq -Rs .),
    "explanation": $(echo "$EXPLANATION" | jq -Rs .),
    "category_id": $CATEGORY_ID,
    "difficulty": "$DIFFICULTY",
    "is_phishing": $IS_PHISHING
}
EOF
)
fi

# === ZOBRAZENÍ JSON PAYLOADU ===
echo "================================="
echo "📋 JSON PAYLOAD PRO DATABÁZI:"
echo "================================="
echo ""
echo "Platforma: $PLATFORM"
echo "Endpoint: $ENDPOINT"
echo ""
if command -v jq &> /dev/null; then
    echo "$PAYLOAD" | jq '.'
else
    echo "$PAYLOAD"
fi
echo ""
echo "================================="
echo ""

# === POTVRZENÍ UŽIVATELEM ===
read -p "Je vše v pořádku? Chcete pokračovat v uložení? (y/n): " confirm
if [ "$confirm" != "y" ] && [ "$confirm" != "Y" ]; then
    echo "❌ Operace zrušena."
    exit 0
fi

echo ""
echo "🚀 Nahrávám otázku do databáze..."
echo ""

# Upload přes API
RESPONSE=$(curl -s -X POST \
    "$API_BASE_URL$ENDPOINT" \
    -H "Content-Type: application/json" \
    -H "X-Internal-Api-Key: $INTERNAL_API_KEY" \
    -d "$PAYLOAD" \
    -w "\n%{http_code}")

# Oddělení HTTP kódu od odpovědi
HTTP_CODE=$(echo "$RESPONSE" | tail -n 1)
RESPONSE_BODY=$(echo "$RESPONSE" | sed '$d')

echo ""
echo "================================="
if [ "$HTTP_CODE" = "201" ] || [ "$HTTP_CODE" = "200" ]; then
    echo "✅ Otázka úspěšně nahrána!"
    echo "HTTP $HTTP_CODE"
    echo "================================="
    echo ""
    echo "📄 Odpověď serveru:"
    echo "$RESPONSE_BODY" | jq '.' 2>/dev/null || echo "$RESPONSE_BODY"
    exit 0
else
    echo "❌ Chyba při nahrávání!"
    echo "HTTP $HTTP_CODE"
    echo "================================="
    echo ""
    echo "📄 Odpověď serveru:"
    echo "$RESPONSE_BODY" | jq '.' 2>/dev/null || echo "$RESPONSE_BODY"
    exit 1
fi


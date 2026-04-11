#!/bin/bash

# Interaktivní script pro generování jedné otázky přes LLM API
# Skript se ptá na parametry a pak zavolá API

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
OUTPUT_FILE=${1:-"questions_draft.json"}

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

echo "🔧 KONFIGURACE GENEROVÁNÍ"
echo "================================="
echo "📍 Prostředí: $ENV_NAME"
echo "📁 Výstupní soubor: $OUTPUT_FILE"
echo "🌐 API URL: $API_BASE_URL"
echo "🔑 API Key: $([ -n "$INTERNAL_API_KEY" ] && echo "nastavena" || echo "NENÍ NASTAVENA ⚠️")"
echo "================================="
echo ""

# Kontrola INTERNAL_API_KEY
if [ -z "$INTERNAL_API_KEY" ]; then
    echo "❌ Chyba: INTERNAL_API_KEY není nastavena pro prostředí '$ENVIRONMENT'!"
    echo "Zkontrolujte .env soubor."
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

# === INTERAKTIVNÍ VÝBĚR PARAMETRŮ ===

# 1. Výběr platformy
echo "📱 Vyberte PLATFORMU:"
echo "1) EMAIL"
echo "2) SMS"
read -p "Zvolte volbu (1 nebo 2): " platform_choice

case $platform_choice in
    1) PLATFORM="EMAIL" ;;
    2) PLATFORM="SMS" ;;
    *) echo "❌ Neplatná volba!"; exit 1 ;;
esac

echo "✓ Zvolena platforma: $PLATFORM"
echo ""

# 2. Výběr kategorie
echo "📂 Vyberte KATEGORII:"
echo "1) LEGIT - Legitimní zpráva"
echo "2) FAKE_URL - Falešná URL"
echo "3) URGENT - Urgentní/nátlak"
echo "4) FAKE_DOC - Falešný dokument"
echo "5) CRED_THEFT - Krádež přihlašovacích údajů"
echo "6) SPEAR_PHISH - Cílený phishing"
echo "7) LOTTERY - Soutěž/výhra"
read -p "Zvolte volbu (1-7): " category_choice

case $category_choice in
    1) CATEGORY="LEGIT" ;;
    2) CATEGORY="FAKE_URL" ;;
    3) CATEGORY="URGENT" ;;
    4) CATEGORY="FAKE_DOC" ;;
    5) CATEGORY="CRED_THEFT" ;;
    6) CATEGORY="SPEAR_PHISH" ;;
    7) CATEGORY="LOTTERY" ;;
    *) echo "❌ Neplatná volba!"; exit 1 ;;
esac

echo "✓ Zvolena kategorie: $CATEGORY"
echo ""

# 3. Výběr obtížnosti
echo "📊 Vyberte OBTÍŽNOST:"
echo "1) EASY - Snadná"
echo "2) MEDIUM - Střední"
echo "3) HARD - Obtížná"
read -p "Zvolte volbu (1-3): " difficulty_choice

case $difficulty_choice in
    1) DIFFICULTY="EASY" ;;
    2) DIFFICULTY="MEDIUM" ;;
    3) DIFFICULTY="HARD" ;;
    *) echo "❌ Neplatná volba!"; exit 1 ;;
esac

echo "✓ Zvolena obtížnost: $DIFFICULTY"
echo ""

# 4. Výběr jazyka
echo "🌍 Vyberte JAZYK:"
echo "1) Čeština (cs)"
echo "2) Angličtina (en)"
read -p "Zvolte volbu (1 nebo 2): " language_choice

case $language_choice in
    1) LANGUAGE="cs" ;;
    2) LANGUAGE="en" ;;
    *) echo "❌ Neplatná volba!"; exit 1 ;;
esac

echo "✓ Zvolen jazyk: $LANGUAGE"
echo ""

# === SHRNUTÍ A POTVRZENÍ ===
echo "================================="
echo "📋 SHRNUTÍ VYBRANÝCH PARAMETRŮ:"
echo "================================="
echo "Platforma:   $PLATFORM"
echo "Kategorie:   $CATEGORY"
echo "Obtížnost:   $DIFFICULTY"
echo "Jazyk:       $LANGUAGE"
echo "================================="
echo ""

read -p "Chcete pokračovat v generování? (y/n): " confirm
if [ "$confirm" != "y" ] && [ "$confirm" != "Y" ]; then
    echo "❌ Operace zrušena."
    exit 0
fi

# === VOLÁNÍ API ===
echo ""
echo "🚀 Generuji otázku..."

RESPONSE=$(curl -s -X GET \
    "$API_BASE_URL/api/admin/llm/generate-question?platform=$PLATFORM&category=$CATEGORY&difficulty=$DIFFICULTY&language=$LANGUAGE" \
    -H "X-Internal-Api-Key: $INTERNAL_API_KEY" \
    -H "Content-Type: application/json" \
    -w "\n%{http_code}")

HTTP_CODE=$(echo "$RESPONSE" | tail -n 1)
RESPONSE_BODY=$(echo "$RESPONSE" | sed '$d')

# Kontrola chyby
if [ "$HTTP_CODE" -lt 200 ] || [ "$HTTP_CODE" -ge 300 ]; then
    echo "❌ Chyba při generování (HTTP $HTTP_CODE):"
    echo "$RESPONSE_BODY" | jq '.' 2>/dev/null || echo "$RESPONSE_BODY"
    exit 1
fi

if echo "$RESPONSE_BODY" | jq -e 'has("error")' >/dev/null 2>&1; then
    echo "❌ Chyba při generování:"
    echo "$RESPONSE_BODY" | jq '.' 2>/dev/null || echo "$RESPONSE_BODY"
    exit 1
fi

# Přidání informace o platformě do JSON
RESPONSE_WITH_PLATFORM=$(echo "$RESPONSE_BODY" | jq --arg platform "$PLATFORM" '. + {platform: $platform}')

# Uložení do souboru
echo "[$RESPONSE_WITH_PLATFORM]" > "$OUTPUT_FILE"

echo ""
echo "✅ Úspěšně vygenerováno!"
echo "📄 Otázka uložena do: $OUTPUT_FILE"
echo ""
echo "📝 Náhled obsahu:"
echo "================================="
if command -v jq &> /dev/null; then
    jq '.' "$OUTPUT_FILE" 2>/dev/null | head -30
else
    cat "$OUTPUT_FILE" | head -30
fi
echo "================================="



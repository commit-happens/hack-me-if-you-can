# Testovací data a soubory

Tato složka obsahuje testovací data a soubory potřebné pro vývoj a testování aplikace.

## Struktura

### `/mockData`

Obsahuje soubory s mock daty používanými pro testování:

- `emailsMockData.json` - Vzorová data emailových zpráv pro testování phishingových scénářů
- `phishingTypesMockData.json` - Definice různých typů phishingových útoků

## Použití

Mock data jsou používána především pro:

- Vývoj a testování UI komponent
- Demonstraci funkcionality bez nutnosti připojení k backend serveru
- Unit testy
- Integrační testy

## Aktualizace dat

Při přidávání nebo úpravě mock dat prosím dodržujte:

1. Zachovejte stávající strukturu JSON souborů
2. Dokumentujte všechny nové přidané položky
3. Ověřte, že data odpovídají reálným scénářům

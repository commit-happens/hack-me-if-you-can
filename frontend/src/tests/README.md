# Testovací data a soubory

Tato složka obsahuje data a soubory potřebné pro vývoj a testování aplikace.

## Struktura

### `/data`

Obsahuje soubory s daty používanými pro testování:

- `emails.json` - Data emailových zpráv pro testování phishingových scénářů.
- `phishingTypes.json` - Definice různých typů phishingových útoků.

## Použití

Data jsou používána především pro:

- Vývoj a testování UI komponent.
- Demonstraci funkcionality bez nutnosti připojení k backend serveru.
- Unit testy.
- Integrační testy.

## Aktualizace dat

Při přidávání nebo úpravě dat prosím dodržujte následující pravidla:

1. Zachovejte stávající strukturu JSON souborů.
2. Dokumentujte všechny nové přidané položky.
3. Ověřte, že data odpovídají reálným scénářům.

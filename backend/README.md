# 🤖 LLM Content Generator (Subtask Completed)

Tento modul je součástí platformy **"Hack me if you can"** a slouží k automatickému generování nekonečného množství phishingových i legitimních tréninkových materiálů pro edukaci uživatelů.

## 🚀 Přehled funkcionality
- **Multi-platformní podpora:** Generování obsahu specificky pro **EMAIL** a **SMS**.
- **Chytré generování:** Využití modelu Gemini 2.5 Flash s technikou *Chain of Thought* pro vysokou věrohodnost a variabilitu.
- **Striktní formát:** Výstupem je vždy validní JSON připravený pro uložení do databáze.
- **Kategorizace:** Podpora 7 základních kategorií (LEGIT, FAKE_URL, URGENT, FAKE_DOC, CRED_THEFT, SPEAR_PHISH, LOTTERY) a 3 úrovní obtížnosti (Easy, Medium, Hard).

## 🛠 Použité technologie
- **Model:** `Gemini 2.5 Flash` (přes Google AI Studio)
- **Framework:** Spring Boot 3.x
- **Knihovna:** `spring-ai-starter-model-google-genai` (Spring AI)

## 🔑 Nastavení Google AI Studia
Pro správné fungování je nutné vygenerovat API klíč v rámci projektu **"Hack me if you can"**:

1. Přihlaste se do [Google AI Studio](https://aistudio.google.com/).
2. V levém menu zvolte **"Get API key"**.
3. Klikněte na **"Create API key in new project"** (nebo vyberte existující projekt "Hack me if you can").
4. Zkopírujte vygenerovaný klíč.

## ⚙️ Konfigurace aplikace
Aplikace očekává klíč v systémové proměnné nebo `.env` souboru.

1. Vytvořte v adresáři `backend` soubor `.env` (nebo upravte `application.properties`):
   ```env
   GOOGLE_API_KEY=vás_zkopírovaný_klíč
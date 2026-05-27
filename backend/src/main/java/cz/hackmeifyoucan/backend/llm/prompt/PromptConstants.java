package cz.hackmeifyoucan.backend.llm.prompt;

/**
 * Sdílené konstanty pro LLM prompt templates.
 * Obsahuje definice kategorií, problémů a společné části promptů.
 */
public class PromptConstants {

    // ============== KATEGORIE (SDÍLENÉ) ==============
    public static final String CATEGORIES_DEFINITION = """
            ### DOSTUPNÉ KATEGORIE A JEJICH CHARAKTERISTIKA:
            1. LEGIT: Autentické, bezpečné zprávy. Odesílatel odpovídá oficiální službě. Žádný nátlak.
            2. FAKE_URL: Útoky využívající podvržené odkazy, vizuální podobnost adres, což vypadá jako legitimní, ale směřují jinam.
            3. URGENT: Manipulativní techniky, které zneužívají autoritu, strach z postihu nebo časový nátlak k vynucení neuvážené akce.
            4. FAKE_DOC: Podvodné zprávy obsahující přílohy nebo odkazy na dokumenty, které mají za cíl infikovat zařízení virem nebo vylákat údaje.
            5. CRED_THEFT: Podvodné stránky, které vypadají jako věrná kopie přihlašovacích obrazovek známých služeb (Google, Microsoft, banky), s cílem ukrást hesla.
            6. SPEAR_PHISH: Vysoce cílený útok, který využívá konkrétní informace o vás (jméno, pozice, kolegové), aby působil maximálně důvěryhodně.
            7. LOTTERY: Zprávy slibující lákavé výhry (iPhony, peněžní obnosy, dárkové poukazy), které se snaží vylákat platební údaje nebo osobní data.
            """;

    // ============== PROBLÉMY (TAGY) - SDÍLENÉ ==============
    public static final String COMMON_PROBLEMS = """
            - `time-pressure`: Uměle vytvářený tlak časovým limitem (urgování, hrozba zablokování nebo sankce).
            - `generic-greeting`: Neosobní přístup (např. "Vážený zákazníku") místo personalizovaného jména u služeb, kde by jméno mělo být.
            - `grammar-errors`: Špatná gramatika, strojový překlad, chybějící diakritika nebo nepřirozený slovosled.
            """;

    // ============== PROBLÉMY (TAGY) - EMAIL SPECIFICKÉ ==============
    public static final String EMAIL_SPECIFIC_PROBLEMS = """
            - `fake-html`: Podvržené odkazy, nesrovnalost mezi textem odkazu a reálným cílem, falešná tlačítka.
            - `domain-spoof`: Podezřelá nebo podvržená doména odesílatele, která napodobuje známou instituci.
            - `suspicious-attachment`: Výzva ke stažení neočekávané nebo nebezpečné přílohy.
            """;

    // ============== PROBLÉMY (TAGY) - SMS SPECIFICKÉ ==============
    public static final String SMS_SPECIFIC_PROBLEMS = """
            - `fake-url`: Podezřelý odkaz, neoficiální doména napodobující instituci, nebo použití zkracovače (bit.ly, tinyurl apod.).
            - `sender-spoof`: Podezřelé telefonní číslo odesílatele (např. zahraniční předvolba pro českou službu) nebo podvržené textové ID odesílatele.
            """;

    // ============== METODA GENEROVÁNÍ - SPOLEČNÝ ZÁKLAD ==============
    public static final String METHODOLOGY_INTRO = """
            ### METODA GENEROVÁNÍ (Chain of Thought):
            1. SCÉNÁŘ: Vyber uvěřitelnou situaci pro kategorii {category} a obtížnost {difficulty}""";
    // Zbytek se doplní platform-specificky

    public static final String METHODOLOGY_COMMON = """
            2. MANIPULACE: Definuj varovné znaky (security_hints), které do textu vložíš.
            3. HIGHLIGHTING: V textu označ konkrétní slova reprezentující problém formátem `{{text|id-problemu}}`.
            4. FINÁLNÍ JSON: Zformátuj výstup dle schématu níže. Každý tag použitý v textu musí mít své vysvětlení v poli `problems`.
            """;

    // ============== OBECNÁ PRAVIDLA ==============
    public static final String GENERAL_RULES = """
            - Jazyk výstupu: {language}.
            - Pokud je kategorie LEGIT, "is_phishing" = false. Pro ostatní = true.
            - FORMÁT: Odpovídej VŽDY a POUZE ve striktním formátu JSON. Neuváděj žádné úvody ani vysvětlivky mimo JSON.
            """;

    // ============== INSTRUKCE PRE PROBLÉMY ==============
    public static final String PROBLEMS_INSTRUCTION = """
            ### DOSTUPNÉ PROBLÉMY (TAGY):
            Při tvorbě podvodné zprávy identifikuj konkrétní bezpečnostní chyby a označ je v textu pomocí tagů. Pokud je kategorie LEGIT, pole `problems` bude prázdné a v textu tagy nepoužiješ.
            """;

    private PromptConstants() {
        // Utility class, non-instantiable
    }
}


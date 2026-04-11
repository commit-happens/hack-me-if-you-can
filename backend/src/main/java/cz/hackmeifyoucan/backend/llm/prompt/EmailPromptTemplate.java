package cz.hackmeifyoucan.backend.llm.prompt;

import cz.hackmeifyoucan.backend.enums.PlatformType;
import org.springframework.stereotype.Component;

@Component
public class EmailPromptTemplate implements PromptTemplate {

    private static final String TEMPLATE = """
            Jsi expertní tvůrce obsahu pro kyberbezpečnostní platformu zaměřenou na EMAIL.
            Tvým úkolem je generovat tréninkové e-maily na základě přesně definovaných kategorií a bezpečnostních pravidel.

            ### DOSTUPNÉ KATEGORIE A JEJICH CHARAKTERISTIKA:
            1. LEGIT: Zprávy, které jsou autentické, pocházejí od důvěryhodných odesílatelů a neobsahují žádné škodlivé prvky.
            2. FAKE_URL: Útoky využívající vizuální podobnost adres nebo podvržené odkazy, které vypadají jako legitimní, ale směřují jinam.
            3. URGENT: Manipulativní techniky, které zneužívají autoritu, strach z postihu nebo časový nátlak k vynucení neuvážené akce.
            4. FAKE_DOC: Podvodné e-maily obsahující přílohy nebo odkazy na dokumenty, které mají za cíl infikovat zařízení virem nebo vylákat platební údaje.
            5. CRED_THEFT: Podvodné stránky, které vypadají jako věrná kopie přihlašovacích obrazovek známých služeb (Google, Microsoft, banky), s cílem ukrást vaše heslo a 2FA kódy.
            6. SPEAR_PHISH: Vysoce cílený útok (Spear Phishing), který využívá konkrétní informace o vás (jméno, pozice, kolegové, nedávné aktivity), aby působil maximálně důvěryhodně.
            7. LOTTERY: Zprávy slibující lákavé výhry (iPhony, peněžní obnosy, dárkové poukazy), které se snaží vylákat platební údaje nebo osobní data pod záminkou "doručení výhry".

            ### METODA GENEROVÁNÍ (Chain of Thought):
            1. SCÉNÁŘ: Vyber uvěřitelnou situaci pro kategorii {category} a obtížnost {difficulty}.
            2. MANIPULACE: Definuj varovné znaky (security_hints), které do textu vložíš (např. podezřelý odesílatel nebo nátlak).
            3. FINÁLNÍ JSON: Zformátuj výstup dle schématu níže.

            ### PRAVIDLA A OMEZENÍ:
            - Jazyk výstupu: {language}.
            - Pokud je kategorie LEGIT, "is_phishing" = false. Pro všechny ostatní = true.
            - OMEZENÍ DÉLKY: Pole "content" a "explanation" nesmí překročit 1500 znaků (limit DB).
            - FORMÁT: Odpovídej VŽDY a POUZE ve striktním formátu JSON. Neuváděj žádné úvody ani vysvětlivky mimo JSON.

            ### STRUKTURA JSON:
            {
              "subject": "Předmět e-mailu",
              "sender": "Jméno odesílatele <email@domena.cz>",
              "content": "Text e-mailu s použitím \\n pro nové řádky",
              "explanation": "Detailní vysvětlení pro studenta, proč je zpráva nebezpečná (nebo bezpečná) s odkazem na typické znaky kategorie",
              "category": "{category}",
              "difficulty": "{difficulty}",
              "is_phishing": boolean
            }
            """;

    @Override
    public PlatformType platform() {
        return PlatformType.EMAIL;
    }

    @Override
    public String render(String category, String difficulty, String language) {
        return TEMPLATE
                .replace("{category}", category)
                .replace("{difficulty}", difficulty)
                .replace("{language}", language);
    }
}


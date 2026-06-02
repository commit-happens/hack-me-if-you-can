package cz.hackmeifyoucan.backend.llm.prompt;

import cz.hackmeifyoucan.backend.enums.PlatformType;
import org.springframework.stereotype.Component;

@Component
public class EmailPromptTemplate implements PromptTemplate {

    private static final String TEMPLATE = """
            Jsi expertní tvůrce obsahu pro kyberbezpečnostní platformu zaměřenou na simulaci phishingu.
            Tvým úkolem je generovat tréninkové e-maily na základě přesně definovaných kategorií, bezpečnostních pravidel a detekovaných problémů.

            ### DOSTUPNÉ KATEGORIE A JEJICH CHARAKTERISTIKA:
            1. LEGIT: Autentické, bezpečné zprávy. Odesílatel odpovídá oficiální službě. Žádný nátlak.
            2. FAKE_URL: Útoky využívající podvržené odkazy, vizuální podobnost adres, což vypadá jako legitimní, ale směřují jinam.
            3. URGENT: Manipulativní techniky, které zneužívají autoritu, strach z postihu nebo časový nátlak k vynucení neuvážené akce.
            4. FAKE_DOC: Podvodné zprávy obsahující přílohy nebo odkazy na dokumenty, které mají za cíl infikovat zařízení virem nebo vylákat údaje.
            5. CRED_THEFT: Podvodné stránky, které vypadají jako věrná kopie přihlašovacích obrazovek známých služeb (Google, Microsoft, banky), s cílem ukrást hesla.
            6. SPEAR_PHISH: Vysoce cílený útok, který využívá konkrétní informace o vás (jméno, pozice, kolegové), aby působil maximálně důvěryhodně.
            7. LOTTERY: Zprávy slibující lákavé výhry (iPhony, peněžní obnosy, dárkové poukazy), které se snaží vylákat platební údaje nebo osobní data.

            ### DOSTUPNÉ PROBLÉMY (TAGY PRO POLE MIGRACE):
            Při tvorbě podvodné zprávy identifikuj konkrétní bezpečnostní chyby a označ je v polích "sender", "subject" nebo "content" pomocí tagů. Pokud je kategorie LEGIT, v textu tagy nepoužiješ.
            - `time-pressure`: Uměle vytvářený tlak časovým limitem (urgování, hrozba zablokování nebo sankce).
            - `generic-greeting`: Neosobní přístup (např. "Vážený zákazníku") místo personalizovaného jména u služeb, kde by jméno mělo být.
            - `grammar-errors`: Špatná gramatika, strojový překlad, chybějící diakritika nebo nepřirozený slovosled.
            - `fake-html`: Podvržené odkazy, nesrovnalost mezi textem odkazu a reálným cílem, falešná tlačítka.
            - `domain-spoof`: Podezřelá nebo podvržená doména odesílatele, která napodobuje známou instituci.
            - `suspicious-attachment`: Výzva ke stažení neočekávané nebo nebezpečné přílohy.

            ### METODA GENEROVÁNÍ (Chain of Thought):
            1. SCÉNÁŘ: Vyber uvěřitelnou situaci pro kategorii {category} a obtížnost {difficulty}.
            2. MANIPULACE: Definuj varovné znaky (security_hints), které do e-mailu vložíš (např. podezřelý odesílatel, předmět nebo nátlak v textu).
            3. HIGHLIGHTING INDIKÁTORŮ: Označ konkrétní slova reprezentující problém formátem `{{text|id-problemu}}`. Tento formát můžeš použít v polích `sender`, `subject` a `content`.
            4. VYSVĚTLENÍ: V poli `explanation` popiš situaci pedagogicky. Klíčové pojmy zvýrazni pouze pomocí HTML tagu <strong></strong>.
            5. FINÁLNÍ JSON: Zformátuj výstup dle schématu níže.

            ### STRUKTURA A PRAVIDLA POLÍ JSON:
            - **is_phishing**: Pokud je kategorie LEGIT, "is_phishing" = false. Pro ostatní = true.
            - **POVOLENÁ POLE PRO HIGHLIGHT {{...}}**: Pole `metadata.sender`, `metadata.subject` a `content` mohou obsahovat označení indikátorů ve formátu `{{text|id-problemu}}` (např. `{{info@ceskaposta-baliky.online|domain-spoof}}`). Tato pole nesmí obsahovat HTML tagy <strong>.
            - **explanation**: Detailní vysvětlení pro studenta, proč je zpráva nebezpečná (nebo bezpečná).
              * STRIKTNÍ ZÁKAZ: V tomto poli nikdy nepoužívej formátování {{text|id-problemu}}.
              * Formátování: Pro zvýraznění klíčových znaků (např. odesílatel, časový tlak) použij výhradně HTML tagy <strong> a </strong>.
            - **Omezení délky**: Pole "content" a "explanation" nesmí překročit 1500 znaků.
            - **Formát odpovědi**: Odpovídej VŽDY a POUZE ve striktním formátu JSON. Neuváděj žádné úvody ani vysvětlivky mimo JSON.

            ### SCHÉMA VÝSTUPU:
            {
              "id": 1,
              "platform": "email",
              "metadata": {
                "sender": "Jméno odesílatele a emailová adresa. Může obsahovat {{tag|id-problemu}}",
                "subject": "Předmět e-mailu. Může obsahovat {{tag|id-problemu}}"
              },
              "content": "String (Text e-mailu s označením {{indikátorů|id-problemu}})",
              "explanation": "String (Vysvětlení s použitím <strong>ztučnění</strong>. ŽÁDNÉ složené závorky!)",
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
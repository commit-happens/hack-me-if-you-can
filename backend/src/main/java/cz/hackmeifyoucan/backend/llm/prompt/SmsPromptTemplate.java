package cz.hackmeifyoucan.backend.llm.prompt;

import cz.hackmeifyoucan.backend.enums.PlatformType;
import org.springframework.stereotype.Component;

@Component
public class SmsPromptTemplate implements PromptTemplate {

    private static final String TEMPLATE = """
        Jsi expertní tvůrce obsahu pro kyberbezpečnostní platformu zaměřenou na SMS (Smishing).
         Tvým úkolem je generovat tréninkové SMS zprávy na základě přesně definovaných kategorií, bezpečnostních pravidel a detekovaných problémů.
        
         ### DOSTUPNÉ KATEGORIE A JEJICH CHARAKTERISTIKA:
         1. LEGIT: Autentické, bezpečné zprávy. Odesílatel odpovídá oficiální službě. Žádný nátlak.
         2. FAKE_URL: Útoky využívající podvržené odkazy, vizuální podobnost adres, což vypadá jako legitimní, ale směřují jinam.
         3. URGENT: Manipulativní techniky, které zneužívají autoritu, strach z postihu nebo časový nátlak k vynucení neuvážené akce.
         4. FAKE_DOC: Podvodné zprávy obsahující přílohy nebo odkazy na dokumenty, které mají za cíl infikovat zařízení virem nebo vylákat údaje.
         5. CRED_THEFT: Podvodné stránky, které vypadají jako věrná kopie přihlašovacích obrazovek známých služeb (Google, Microsoft, banky), s cílem ukrást hesla.
         6. SPEAR_PHISH: Vysoce cílený útok, který využívá konkrétní informace o vás (jméno, pozice, kolegové), aby působil maximálně důvěryhodně.
         7. LOTTERY: Zprávy slibující lákavé výhry (iPhony, peněžní obnosy, dárkové poukazy), které se snaží vylákat platební údaje nebo osobní data.
        
         ### DOSTUPNÉ PROBLÉMY (TAGY PRO POLE "content"):
         Při tvorbě podvodné zprávy identifikuj konkrétní bezpečnostní chyby a označ je VÝHRADNĚ v textu SMS (pole `content`) pomocí tagů. Pokud je kategorie LEGIT, v textu tagy nepoužiješ.
         - `time-pressure`: Uměle vytvářený tlak časovým limitem (urgování, hrozba zablokování nebo sankce).
         - `generic-greeting`: Neosobní přístup (např. "Vážený zákazníku") místo personalizovaného jména u služeb, kde by jméno mělo být.
         - `grammar-errors`: Špatná gramatika, strojový překlad, chybějící diakritika nebo nepřirozený slovosled.
         - `fake-url`: Podezřelý odkaz, neoficiální doména napodobující instituci, nebo použití zkracovače (bit.ly, tinyurl apod.).
         - `sender-spoof`: Podezřelé telefonní číslo odesílatele (např. zahraniční předvolba pro českou službu) nebo podvržené textové ID odesílatele.
        
         ### METODA GENEROVÁNÍ (Chain of Thought):
         1. SCÉNÁŘ: Vyber uvěřitelnou situaci pro kategorii {category} a obtížnost {difficulty} vhodnou pro zobrazení na mobilním telefonu.
         2. MANIPULACE: Definuj varovné znaky (security_hints), jako je neobvyklé číslo odesílatele, předmět nebo podezřelý link v textu.
         3. HIGHLIGHTING OBSAHU: Pouze v textu SMS (pole `content`) označ konkrétní slova reprezentující problém formátem `{{text|id-problemu}}`.
         4. VYSVĚTLENÍ: V poli `explanation` popiš situaci pedagogicky jako čistý text.
         5. FINÁLNÍ JSON: Zformátuj výstup dle schématu níže.
        
         ### STRUKTURA A PRAVIDLA POLÍ JSON:
        
         - **is_phishing**: Pokud je kategorie LEGIT, "is_phishing" = false. Pro ostatní = true.
         - **content**: Text SMS zprávy. Konkrétní smishingové indikátory v textu striktně označ formátem {{problémový text|id-problemu}}, např. {{bit.ly/exekuce-cz|fake-url}}. Toto pole nesmí obsahovat HTML tagy ani jiné formátování.
           * OMEZENÍ DÉLKY PRO CONTENT: Samotný text SMS po očištění od tagů musí být krátký a úderný (max 250 znaků).
         - **STRIKTNÍ ZÁKAZ FORMÁTOVÁNÍ (PLAINTEXT POLA)**:\s
           * Pole `metadata.sender`, `metadata.subject` and `explanation` musí být čistý text (plain text).
           * V těchto polích je STRIKTN RESTRINKCE používat složené závorky `{{...}}`, HTML tagy jako `<strong>` nebo jakékoliv jiné zvýrazňování textu.
           * OMEZENÍ DÉLKY PRO EXPLANATION: Maximálně 1000 znaků.
         - **Formát odpovědi**: Odpovídej VŽDY a POUZE ve striktním formátu JSON. Neuváděj žádné úvody ani vysvětlivky mimo JSON.
        
         ### SCHÉMA VÝSTUPU:
         {
           "id": 1,
           "platform": "sms",
           "metadata": {
             "sender": "Telefonní číslo odesílatele nebo textové ID (Striktně plain text, bez jakýchkoliv tagů a závorek!)",
             "subject": "Krátké označení/kontext odesílatele (Striktně plain text, bez jakýchkoliv tagů a závorek!)"
           },
           "content": "String (Text SMS s označením {{indikátorů|id-problemu}})",
           "explanation": "String (Pedagogické vysvětlení. Striktně plain text, ŽÁDNÉ složené závorky, ŽÁDNÉ HTML tagy!)",
           "category": "{category}",
           "difficulty": "{difficulty}",
           "is_phishing": boolean
         }
            """;

    @Override
    public PlatformType platform() {
        return PlatformType.SMS;
    }

    @Override
    public String render(String category, String difficulty, String language) {
        return TEMPLATE
                .replace("{category}", category)
                .replace("{difficulty}", difficulty)
                .replace("{language}", language);
    }
}
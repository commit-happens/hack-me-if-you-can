package cz.hackmeifyoucan.backend.llm.prompt;

import cz.hackmeifyoucan.backend.enums.PlatformType;
import org.springframework.stereotype.Component;

@Component
public class SmsPromptTemplate implements PromptTemplate {

    private static final String TEMPLATE = """
            Jsi expertní tvůrce obsahu pro kyberbezpečnostní platformu zaměřenou na SMS (Smishing).
            Tvým úkolem je generovat tréninkové SMS zprávy na základě přesně definovaných kategorií a bezpečnostních pravidel.

            ### DOSTUPNÉ KATEGORIE A JEJICH CHARAKTERISTIKA:
            1. LEGIT: Autentické, bezpečné SMS. Odesílatel odpovídá oficiální službě (např. potvrzení objednávky, 2FA kód). Žádný nátlak.
            2. FAKE_URL: SMS obsahující podezřelé odkazy, zkracovače (bit.ly) nebo překlepy v doménách (např. b-anka.cz).
            3. URGENT: Vyvolání paniky (zablokovaný účet, nezaplacená pokuta, exekuce), vyžadující okamžitou akci.
            4. FAKE_DOC: Odkazy na stažení falešných dokumentů, e-receptů nebo faktur v mobilním formátu.
            5. CRED_THEFT: Podvodné SMS směřující na věrné kopie mobilních přihlašovacích stránek bank nebo sociálních sítí.
            6. SPEAR_PHISH: Personalizovaná SMS (často oslovuje jménem), působí jako zpráva od kurýra s balíčkem, který právě čekáte.
            7. LOTTERY: Sliby výher v soutěžích operátorů nebo obchodních řetězců (např. "Vyhráli jste iPhone").

            ### METODA GENEROVÁNÍ (Chain of Thought):
            1. SCÉNÁŘ: Vyber uvěřitelnou situaci pro kategorii {category} a obtížnost {difficulty} vhodnou pro mobilní telefon.
            2. MANIPULACE: Definuj varovné znaky (security_hints), jako je neobvyklé číslo odesílatele nebo podezřelý link.
            3. FINÁLNÍ JSON: Zformátuj výstup dle schématu níže.

            ### PRAVIDLA A OMEZENÍ:
            - Jazyk výstupu: {language}.
            - Pokud je kategorie LEGIT, "is_phishing" = false. Pro ostatní = true.
            - OMEZENÍ DÉLKY: Samotná SMS (content) musí být krátká a úderná (max 250 znaků). Explanation může být delší (max 1000 znaků).
            - FORMÁT: Odpovídej VŽDY a POUZE ve striktním formátu JSON. Neuváděj žádné úvody ani vysvětlivky mimo JSON.

            ### STRUKTURA JSON:
            {
            "sender": "Jméno odesílatele nebo krátký název (např. InfoSMS, Posta)",
            "phoneNumber": "Telefonní číslo odesílatele (např. +420 123 456 789 nebo krátké číslo 90011)",
            "content": "Text SMS zprávy",
            "explanation": "Detailní vysvětlení pro studenta, proč je zpráva nebezpečná (nebo bezpečná) s odkazem na typické znaky SMS phishingu",
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


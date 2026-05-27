package cz.hackmeifyoucan.backend.llm.prompt;

import cz.hackmeifyoucan.backend.enums.PlatformType;
import org.springframework.stereotype.Component;

@Component
public class SmsPromptTemplate implements PromptTemplate {

    private static final String TEMPLATE = """
            Jsi expertní tvůrce obsahu pro kyberbezpečnostní platformu zaměřenou na SMS (Smishing).
            Tvým úkolem je generovat tréninkové SMS zprávy na základě přesně definovaných kategorií, bezpečnostních pravidel a detekovaných problémů.
            
            """ + PromptConstants.CATEGORIES_DEFINITION + """
            
            """ + PromptConstants.PROBLEMS_INSTRUCTION + PromptConstants.COMMON_PROBLEMS + PromptConstants.SMS_SPECIFIC_PROBLEMS + """
            
            ### METODA GENEROVÁNÍ (Chain of Thought):
            1. SCÉNÁŘ: Vyber uvěřitelnou situaci pro kategorii {category} a obtížnost {difficulty} vhodnou pro mobilní telefon.
            2. MANIPULACE: Definuj varovné znaky (security_hints), jako je neobvyklé číslo odesílatele nebo podezřelý link.
            3. HIGHLIGHTING: V textu SMS označ konkrétní slova reprezentující problém formátem `{{text|id-problemu}}`.
            4. FINÁLNÍ JSON: Zformátuj výstup dle schématu níže. Každý tag použitý v textu musí mít své vysvětlení v poli `problems`.
            
            ### PRAVIDLA A OMEZENÍ:
            """ + PromptConstants.GENERAL_RULES + """
            - OMEZENÍ DÉLKY: Samotná SMS (content) po očištění od tagů musí být krátká a úderná (max 250 znaků). Explanation může být delší (max 1000 znaků).
            
            ### STRUKTURA JSON:
            [
              {
                "id": 1,
                "platform": "sms",
                "metadata": {
                  "sender": "Telefonní číslo odesílatele (např. +420 123 456 789) nebo textové ID (např. Posta)",
                  "subject": "Krátké označení/kontext odesílatele (např. InfoSMS, Banka)"
                },
                "content": "Text SMS zprávy. Konkrétní smishingové indikátory v textu striktně označ formátem {{problémový text|id-problemu}}, např. Exekucni prikaz stahujte na {{bit.ly/exekuce-cz|fake-url}}.",
                "explanation": "Detailní vysvětlení pro studenta, proč je zpráva nebezpečná (nebo bezpečná) s odkazem na typické znaky SMS phishingu.",
                "category": "{category}",
                "difficulty": "{difficulty}",
                "is_phishing": boolean
              }
            ]
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
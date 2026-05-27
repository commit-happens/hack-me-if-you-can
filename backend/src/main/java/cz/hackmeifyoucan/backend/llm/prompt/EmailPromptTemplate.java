package cz.hackmeifyoucan.backend.llm.prompt;

import cz.hackmeifyoucan.backend.enums.PlatformType;
import org.springframework.stereotype.Component;

@Component
public class EmailPromptTemplate implements PromptTemplate {

    private static final String TEMPLATE = """
            Jsi expertní tvůrce obsahu pro kyberbezpečnostní platformu zaměřenou na simulaci phishingu.
            Tvým úkolem je generovat tréninkové e-maily na základě přesně definovaných kategorií, bezpečnostních pravidel a detekovaných problémů.
            
            """ + PromptConstants.CATEGORIES_DEFINITION + """
            
            """ + PromptConstants.PROBLEMS_INSTRUCTION + PromptConstants.COMMON_PROBLEMS + PromptConstants.EMAIL_SPECIFIC_PROBLEMS + """
            
            ### METODA GENEROVÁNÍ (Chain of Thought):
            1. SCÉNÁŘ: Vyber uvěřitelnou situaci pro kategorii {category} a obtížnost {difficulty}.
            2. MANIPULACE: Definuj varovné znaky (security_hints), které do textu vložíš (např. podezřelý odesílatel nebo nátlak).
            3. HIGHLIGHTING: V textu e-mailu označ konkrétní slova reprezentující problém formátem `{{text|id-problemu}}`.
            4. FINÁLNÍ JSON: Zformátuj výstup dle schématu níže. Každý tag použitý v textu musí mít své vysvětlení v poli `problems`.
            
            ### PRAVIDLA A OMEZENÍ:
            """ + PromptConstants.GENERAL_RULES + """
            - OMEZENÍ DÉLKY: Pole "content" a "explanation" nesmí překročit 1500 znaků (limit DB).
            
            ### STRUKTURA JSON:
            [
              {
                "id": 1,
                "platform": "email",
                "metadata": {
                  "sender": "Jméno odesílatele <email@domena.cz>",
                  "subject": "Předmět e-mailu"
                },
                "content": "Text e-mailu s použitím \\\\n pro nové řádky. Konkrétní phishingové indikátory v textu striktně označ formátem {{problémový text|id-problemu}}, např. {{Vážený zákazníku|generic-greeting}}.",
                "explanation": "Detailní vysvětlení pro studenta, proč je zpráva nebezpečná (nebo bezpečná) s odkazem na typické znaky kategorie.",
                "category": "{category}",
                "difficulty": "{difficulty}",
                "is_phishing": boolean
              }
            ]
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
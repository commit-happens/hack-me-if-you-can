import type { ProblemResponse } from "../../../../services/generated/model";

export interface ParsedProblemMarker {
  type: "problem-marker";
  text: string;
  id: string;
  description: string;
}

/**
 * Parsuje obsah e-mailu a nahrazuje problematická místa komponentou ProblemMarker.
 *
 * Syntaxe markeru: {{...{{text|id}}...}}
 * Příklad: "Klikněte {{{{zde|fake-link}}}}"
 *
 * @param content - Obsah e-mailu s markdown odkazy a problema markery
 * @param problems - Pole problémů s ID a popisem
 * @returns Pole mix stringů a objektů pro ProblemMarker
 */
export function parseEmailContent(
  content: string,
  problems: ProblemResponse[] = [],
): (string | ParsedProblemMarker)[] {
  // Podporované syntaxe:
  // 1) Jednoduchá: {{text|id}}
  // 2) Vnořená:    {{...{{text|id}}...}}
  const problemMarkerRegex =
    /\{\{(?:[^{}]*\{\{([^|{}]+)\|([^{}]+)\}\}[^{}]*|([^|{}]+)\|([^{}]+))\}\}/g;

  // Vytvoř mapu pro rychlé vyhledávání problémů podle ID
  const problemMap = new Map(problems.map((p) => [p.id, p]));

  const result: (string | ParsedProblemMarker)[] = [];
  let lastIndex = 0;

  // Proveď matching všech markerů
  let match;
  while ((match = problemMarkerRegex.exec(content)) !== null) {
    const fullMatch = match[0];
    const text = match[1] ?? match[3];
    const problemId = match[2] ?? match[4];
    const matchStart = match.index;

    // Přidej text před matchem
    if (matchStart > lastIndex) {
      result.push(content.substring(lastIndex, matchStart));
    }

    // Najdi problém v mapě
    const problem = problemMap.get(problemId);

    if (problem) {
      result.push({
        type: "problem-marker",
        text,
        id: problemId,
        description: problem.description,
      } as ParsedProblemMarker);
    } else {
      // Pokud problém neexistuje, přidej jen text bez markeru
      result.push(text);
    }

    lastIndex = matchStart + fullMatch.length;
  }

  // Přidej zbývající text
  if (lastIndex < content.length) {
    result.push(content.substring(lastIndex));
  }

  // Pokud není žádný match, vrať původní obsah
  return result.length > 0 ? result : [content];
}

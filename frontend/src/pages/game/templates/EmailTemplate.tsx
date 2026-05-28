import React from "react";
import { Card } from "react-bootstrap";
import useTranslation from "../../../hooks/useTranslation";
import type { ProblemResponse } from "../../../services/generated/model/problemResponse";
import ProblemMarker from "../components/problemMarker";
import {
  parseEmailContent,
  type ParsedProblemMarker,
} from "../components/problemMarker/parseContent";
import PseudoLink from "../components/pseudoLink";

export interface EmailTemplateProps {
  sender: string;
  subject: string;
  content: string;
  problems?: ProblemResponse[];
}

/**
 * Jednoduchá šablona e-mailu.
 * Zobrazuje pole: Odesílatel, Předmět a Obsah.
 *
 * Obsah je parsován pro:
 * 1. Problematická místa (syntaxe: {{...{{text|id}}...}})
 * 2. Markdown odkazy (syntaxe: [text](url))
 */
const EmailTemplate: React.FC<EmailTemplateProps> = ({
  sender,
  subject,
  content,
  problems = [],
}) => {
  const texts = useTranslation("template");

  return (
    <Card className="mb-2">
      <Card.Header>
        <strong>{texts.sender}</strong>: {sender}
      </Card.Header>
      <Card.Header>
        <strong>{texts.subject}</strong>: {subject}
      </Card.Header>
      <Card.Body>
        <Card.Text className="p-sm-0 p-md-3 p-lg-4 p-xl-5">
          {combineParsers(content, problems)}
        </Card.Text>
      </Card.Body>
    </Card>
  );
};

/**
 * Kombinuje parsování problém markerů a markdown odkazů.
 * Pořadí: nejdřív problém markery, pak PseudoLinky v zbývajícím textu.
 */
function combineParsers(content: string, problems: ProblemResponse[]): React.ReactNode[] {
  // Krok 1: Parsuj problem markery
  const withProblems = parseEmailContent(content, problems);

  const hasExplicitMarkers = withProblems.some((element) => typeof element !== "string");

  // Fallback: pokud obsah neobsahuje explicitní markery, ale máme problems,
  // označ problematické části automaticky podle markdown odkazů.
  if (!hasExplicitMarkers && problems.length > 0) {
    return replaceMarkdownLinksWithProblemMarkers(content, problems);
  }

  // Krok 2: Parsuj markdown linky v jednotlivých fragmentech
  const result: React.ReactNode[] = [];

  withProblems.forEach((element, parentIndex) => {
    // Pokud je to string/text, parsuj v něm markdown linky
    if (typeof element === "string") {
      result.push(...replaceMarkdownLinksWithPseudoLinks(element, parentIndex));
    } else {
      const marker = element as ParsedProblemMarker;
      result.push(
        <ProblemMarker
          key={`marker-${parentIndex}`}
          text={marker.text}
          id={marker.id}
          description={marker.description}
        />,
      );
    }
  });

  return result;
}

/**
 * Fallback parser, který mapuje markdown odkazy na ProblemMarker komponenty.
 * Používá se pouze pokud v textu nejsou explicitní {{...|...}} markery.
 */
function replaceMarkdownLinksWithProblemMarkers(
  text: string,
  problems: ProblemResponse[],
): React.ReactNode[] {
  const markdownLinkRegex = /\[([^\]]+)\]\((https?:\/\/[^)\s]+)\)/g;
  const result: React.ReactNode[] = [];

  let lastIndex = 0;
  let linkIndex = 0;
  let foundLink = false;
  let match: RegExpExecArray | null;

  while ((match = markdownLinkRegex.exec(text)) !== null) {
    const [fullMatch, linkText] = match;
    const start = match.index;
    const end = start + fullMatch.length;
    foundLink = true;

    if (start > lastIndex) {
      result.push(
        <React.Fragment key={`auto-text-${start}`}>
          {text.slice(lastIndex, start)}
        </React.Fragment>,
      );
    }

    const problem = problems[Math.min(linkIndex, problems.length - 1)];

    result.push(
      <ProblemMarker
        key={`auto-marker-${start}`}
        text={linkText}
        id={problem.id}
        description={problem.description}
      />,
    );

    linkIndex += 1;
    lastIndex = end;
  }

  if (!foundLink && text.trim().length > 0 && problems.length > 0) {
    const snippetLength = Math.min(60, text.length);
    const markerText = text.slice(0, snippetLength).trimEnd();
    const tail = text.slice(snippetLength);
    const firstProblem = problems[0];

    return [
      <ProblemMarker
        key="auto-fallback-marker"
        text={markerText}
        id={firstProblem.id}
        description={firstProblem.description}
      />,
      <React.Fragment key="auto-fallback-tail">{tail}</React.Fragment>,
    ];
  }

  if (lastIndex < text.length) {
    result.push(<React.Fragment key="auto-tail">{text.slice(lastIndex)}</React.Fragment>);
  }

  return result.length > 0 ? result : [text];
}

/** Nahrazuje Markdown odkazy komponentou PseudoLink. */
function replaceMarkdownLinksWithPseudoLinks(
  text: string,
  parentKey: number,
): React.ReactNode[] {
  const markdownLinkRegex = /\[([^[]+)\](\(.*\))/g;
  const parts = text.split(markdownLinkRegex);

  let index = 0;
  const result: React.ReactNode[] = [];

  if (parts.length < 2) return [text];

  while (index < parts.length) {
    const text1 = parts[index];
    const text2 = parts[index + 1];

    if (text2 && text2.includes("http") && text2.startsWith("(") && text2.endsWith(")")) {
      index += 1;
      const urlOnly = text2.length > 1 ? text2.slice(1, -1) : text2;
      result.push(
        <PseudoLink key={`link-${parentKey}-${index}`} title={urlOnly}>
          {text1}
        </PseudoLink>,
      );
    } else {
      result.push(
        <React.Fragment key={`text-${parentKey}-${index}`}>{text1}</React.Fragment>,
      );
    }
    index += 1;
  }

  return result;
}

export default EmailTemplate;

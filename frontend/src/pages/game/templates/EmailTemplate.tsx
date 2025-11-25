import React from "react";
import { Card } from "react-bootstrap";
import useTranslation from "../../../hooks/useTranslation";
import PseudoLink from "../components/pseudoLink";

export interface EmailTemplateProps {
  sender: string;
  subject: string;
  content: string;
}

/**
 * Jednoduchá šablona e-mailu.
 * Zobrazuje pole: Odesílatel, Předmět a Obsah.
 */
const EmailTemplate: React.FC<EmailTemplateProps> = ({ sender, subject, content }) => {
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
          {replaceMarkdownLinksWithPseudoLinks(content)}
        </Card.Text>
      </Card.Body>
    </Card>
  );
};

/** Nahrazuje Markdown odkazy komponentou PseudoLink. */
function replaceMarkdownLinksWithPseudoLinks(text: string) {
  const markdownLinkRegex = /\[([^\[]+)\](\(.*\))/g;
  const parts = text.split(markdownLinkRegex);

  let index = 0;
  const result = [];

  if (parts.length < 2) return text;

  while (index < parts.length) {
    const text1 = parts[index];
    const text2 = parts[index + 1];

    if (text2 && text2.includes("http") && text2.startsWith("(") && text2.endsWith(")")) {
      index += 1; // Už víme, že se jedná o markdown odkaz složený ze dvou částí [](), proto posuneme index o jedničku dopředu.
      const urlOnly = text2.length > 1 ? text2.slice(1, -1) : text2;
      result.push(
        <PseudoLink key={`link-${index}`} title={urlOnly}>
          {text1}
        </PseudoLink>
      );
    } else {
      result.push(<React.Fragment key={`text-${index}`}>{text1}</React.Fragment>);
    }
    index += 1;
  }

  return result;
}

export default EmailTemplate;

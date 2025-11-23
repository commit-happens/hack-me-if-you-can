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
const EmailTemplate: React.FC<EmailTemplateProps> = ({
  sender,
  subject,
  content,
}) => {
  const texts = useTranslation("template");

  const replaceMarkdownLinksWithPseudoLinks = (text: string) => {
    const markdownLinkRegex = /\[([^\[]+)\](\(.*\))/g;
    const parts = text.split(markdownLinkRegex);

    let index = 0;
    const result = [];

    if (parts.length < 2) return text;

    while (index < parts.length - 1) {
      const text1 = parts[index];
      const text2 = parts[index + 1];

      if (text2.includes("(http")) {
        index += 1;
        const urlOnly = text2.length > 1 ? text2.slice(1, -1) : text2;
        result.push(
          <PseudoLink key={index} title={urlOnly}>
            {text1}
          </PseudoLink>,
        );
      } else result.push(text1);
      index += 1;
    }

    return result;
  };

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

export default EmailTemplate;

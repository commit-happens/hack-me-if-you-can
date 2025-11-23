import React from "react";
import { Card } from "react-bootstrap";
import useTranslation from "../../../hooks/useTranslation";

export interface EmailTemplateProps {
  sender: string;
  subject: string;
  content: string | React.ReactNode;
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
  return (
    <Card className="mb-2">
      <Card.Header>
        <strong>{texts.sender}</strong>: {sender}
      </Card.Header>
      <Card.Header>
        <strong>{texts.subject}</strong>: {subject}
      </Card.Header>
      <Card.Body>
        <Card.Text className="p-sm-0 p-md-3 p-lg-4 p-xl-5">{content}</Card.Text>
      </Card.Body>
    </Card>
  );
};

export default EmailTemplate;

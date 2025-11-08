import React from "react";
import { Card } from "react-bootstrap";
import useTranslation from "../../../hooks/useTranslation";

export interface EmailTemplateProps {
  sender: string;
  subject: string;
  content: string | React.ReactNode;
  className?: string;
}

/**
 * Jednoduchá šablona e-mailu.
 * Zobrazuje pole: Odesílatel, Předmět a Obsah.
 */
const EmailTemplate: React.FC<EmailTemplateProps> = ({ sender, subject, content, className }) => {
  const texts = useTranslation("template");
  return (
    <>
      <Card style={{ width: "50vw" }} className="mb-2">
        <Card.Header>
          <strong>{texts.sender}</strong>: {sender}
        </Card.Header>
        <Card.Header>
          <strong>{texts.subject}</strong>: {subject}
        </Card.Header>
        <Card.Body>
          <Card.Text>{content}</Card.Text>
        </Card.Body>
      </Card>
    </>
  );
};

export default EmailTemplate;

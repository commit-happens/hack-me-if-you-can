import React from 'react';

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
const EmailTemplate: React.FC<EmailTemplateProps> = ({
  sender,
  subject,
  content,
  className,
}) => {
  return (
    <article
      className={className}
      style={{
        border: '1px solid #e0e0e0',
        borderRadius: 8,
        padding: 16,
        width: 720,
        fontFamily:
          "system-ui, -apple-system, Segoe UI, Roboto, 'Helvetica Neue', Arial",
        background: '#fff',
        color: '#111',
        textAlign: 'left',
        lineHeight: 1.5,
        margin: '0 auto',
      }}
    >
      <div style={{ marginBottom: 12 }}>
        <div style={{ fontWeight: 600, marginBottom: 4 }}>Odesílatel</div>
        <div>{sender}</div>
      </div>
      <div style={{ marginBottom: 12 }}>
        <div style={{ fontWeight: 600, marginBottom: 4 }}>Předmět</div>
        <div>{subject}</div>
      </div>
      <div
        style={{
          width: '100%',
          minHeight: 180,
          whiteSpace: 'pre-wrap',
          background: '#fafafa',
          padding: 20,
          borderRadius: 6,
          border: '1px solid #f0f0f0',
          fontFamily: 'inherit',
          fontSize: 14,
          lineHeight: 1.4,
          boxSizing: 'border-box',
        }}
      >
        {content}
      </div>
    </article>
  );
};

export default EmailTemplate;

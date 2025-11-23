import React from "react";

type PseudoLinkProps = {
  children: React.ReactNode;
  title?: string; // tooltip
  tabIndex?: number;
};

/** Komponenta, která vypadá jako odkaz, ale ve skutečnosti jím není. */
const PseudoLink: React.FC<PseudoLinkProps> = ({ children, title, tabIndex = 0 }) => {
  return (
    <span
      role="link"
      tabIndex={tabIndex}
      title={title}
      className={`text-primary text-decoration-underline`}
      style={{ cursor: "pointer" }}
    >
      {children}
    </span>
  );
};

export default PseudoLink;

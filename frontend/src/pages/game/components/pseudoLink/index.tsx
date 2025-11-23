import React from "react";

type Props = {
  children: React.ReactNode;
  title?: string; // tooltip
  onClick?: (e?: React.MouseEvent<HTMLSpanElement>) => void;
  tabIndex?: number;
};

const PseudoLink: React.FC<Props> = ({ children, title, tabIndex = 0 }) => {
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

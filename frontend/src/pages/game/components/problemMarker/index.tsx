import React, { useRef, useState } from "react";
import { Popover, PopoverBody, PopoverHeader } from "react-bootstrap";
import { OverlayTrigger } from "react-bootstrap";
import "./styles.css";

export interface ProblemMarkerProps {
  text: string;
  id?: string;
  description?: string;
  header?: string;
}

/**
 * Komponenta pro zobrazení problematického místa v e-mailu.
 * Při najetí myší se zobrazí Popover s vysvětlením problému.
 */
const ProblemMarker: React.FC<ProblemMarkerProps> = ({
  text,
  id,
  description,
  header,
}) => {
  const targetRef = useRef<HTMLSpanElement>(null);

  const [show, setShow] = useState(false);

  const popover = (
    <Popover id={`popover-${id}`} className="problem-popover">
      <PopoverHeader className="problem-popover-header">{`💡${header}`}</PopoverHeader>
      <PopoverBody className="problem-popover-body">{description}</PopoverBody>
    </Popover>
  );

  return (
    <OverlayTrigger
      placement="top"
      overlay={popover}
      trigger={["hover", "focus"]}
      show={show}
      onToggle={setShow}
    >
      <span
        ref={targetRef}
        className="problem-marker"
        tabIndex={0}
        role="tooltip"
        aria-label={`Problém: ${description}`}
      >
        {text}
      </span>
    </OverlayTrigger>
  );
};

export default ProblemMarker;

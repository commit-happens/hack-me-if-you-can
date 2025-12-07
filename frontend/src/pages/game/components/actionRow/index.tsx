import { faWarning, faCheck } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Button, Col, Container, ProgressBar, Row } from "react-bootstrap";
import { Answer } from "../../useGame";
import type { Translation } from "../../../../languages/csCZ";

type ActionsRowProps = {
  handleAnswer: (answer: Answer) => void;
  remainingTime: number;
  timeoutTextColor?: string;
  timeoutProgressBarVariant?: string;
  timePerQuestion: number;
  texts: Translation["game"];
};
/**
 * Komponenta pro zobrazení řádku s tlačítky pro odpověď a odpočítávadlem času.
 */
const ActionsRow = (props: ActionsRowProps) => {
  const {
    handleAnswer,
    remainingTime,
    timeoutProgressBarVariant,
    timePerQuestion,
    texts,
  } = props;

  return (
    <>
      <Row justify="center" align="center" className="g-0">
        <Col className="text-end">
          <Button
            variant="outline-secondary"
            onClick={() => handleAnswer(Answer.Phishing)}
          >
            <FontAwesomeIcon
              icon={faWarning}
              aria-hidden="true"
              className="text-danger"
            />
            {texts.answers.phishing}
          </Button>
        </Col>
        <Col
          xs={2}
          className="d-flex align-items-center justify-content-center"
        >
          <div className="text-center">
            <span
              style={{
                background: 
                        timeoutProgressBarVariant === "warning"
                ? "#f0ad4e" // žlutá
                : timeoutProgressBarVariant === "danger"
                ? "#d9534f" // červená
                : "#0d6efd", // modrá (bootstrap primary)
                      color: "white",
                padding: "4px 12px",
                borderRadius: "6px",
                fontWeight: "600",
                display: "inline-block",
                minWidth: "60px",
                textAlign: "center",
                      }}
            >
              {remainingTime} s
            </span>
          </div>

        </Col>
        <Col className="text-start">
          <Button
            variant="outline-secondary"
            onClick={() => handleAnswer(Answer.Safe)}
          >
            <FontAwesomeIcon
              icon={faCheck}
              className="text-success"
              aria-hidden="true"
            />
            {texts.answers.safe}
          </Button>
        </Col>
      </Row>
      <Container className="mt-4 g-0">
        <ProgressBar
          now={remainingTime}
          variant={timeoutProgressBarVariant}
          max={timePerQuestion}
        />
      </Container>
    </>
  );
};

export default ActionsRow;

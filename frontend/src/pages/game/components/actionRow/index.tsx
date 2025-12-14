import { faWarning, faCheck } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  Badge,
  Button,
  Col,
  Container,
  ProgressBar,
  Row,
} from "react-bootstrap";
import { Answer } from "../../useGame";
import type { Translation } from "../../../../languages/csCZ";

type ActionsRowProps = {
  handleAnswer: (answer: Answer) => void;
  remainingTime: number;
  timeoutBgColor?: string;
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
    timeoutBgColor,
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
            <Badge className={`px-2 py-2 fw-bold ${timeoutBgColor}`}>
              {remainingTime} s
            </Badge>
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

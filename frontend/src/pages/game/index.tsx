import {
  faArrowRight,
  faCheck,
  faWarning,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  Alert,
  Button,
  Col,
  Container,
  ProgressBar,
  Row,
} from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Header from "../../components/header";
import emailsData from "../../data/emails.json";
import useTranslation, { getText } from "../../hooks/useTranslation";
import Page from "../../models/Page";
import { useAppSelector } from "../../store/hooks";
import { getPagePath } from "../../utils/routing";
import EmailTemplate from "./templates/EmailTemplate";
import { Answer as GameAnswer, useGame } from "./useGame";

function Game() {
  const navigate = useNavigate();
  const score = useAppSelector((state) => state.game.score);
  const nickname = useAppSelector((state) => state.user.nickname);
  const texts = useTranslation("game");

  const { emails } = emailsData;

  const {
    currentEmail,
    currentIndex,
    answer,
    isLastEmail,
    isCorrectAnswer,
    difficulty,
    emailsOfDifficulty,
    handleAnswer,
    handleContinue,
    remainingTime,
    timeoutTextColor,
    timePerQuestion,
    timeoutProgressBarVariant,
  } = useGame({
    allEmails: emails,
    texts,
    onFinish: () => navigate(getPagePath(Page.Results)),
  });

  const { id, sender, subject, content, explanation } = currentEmail || {};

  /**
   * Zobrazení řádk s tlačítky pro odpověď a odpočítávadlem času.
   */
  const renderActionsRow = () => {
    return (
      <>
        <Row justify="center" align="center" className="g-0">
          <Col className="text-end">
            <Button
              variant="outline-secondary"
              onClick={() => handleAnswer(GameAnswer.Phishing)}
            >
              <FontAwesomeIcon
                icon={faWarning}
                aria-hidden="true"
                className="text-danger"
              />
              {getText(texts.answers.phishing)}
            </Button>
          </Col>
          <Col
            xs={2}
            className="d-flex align-items-center justify-content-center"
          >
            <div className={`text-center px-3 ${timeoutTextColor}`}>
              {remainingTime} s
            </div>
          </Col>
          <Col className="text-start">
            <Button
              variant="outline-secondary"
              onClick={() => handleAnswer(GameAnswer.Safe)}
            >
              <FontAwesomeIcon
                icon={faCheck}
                className="text-success"
                aria-hidden="true"
              />
              {getText(texts.answers.safe)}
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

  /**
   * Zobrazení obsahu podle toho, zda uživatel odpověděl.
   */
  const renderContent = () => {
    let feedbackData = {
      title: getText(texts.feedback.correct),
      variant: "success",
    };

    if (answer) {
      if (!isCorrectAnswer())
        feedbackData = {
          title: getText(texts.feedback.incorrect),
          variant: "danger",
        };

      return (
        <div className="w-100">
          <Alert variant={feedbackData.variant}>{feedbackData.title}</Alert>
          {explanation}
          <div style={{ textAlign: "center" }} className="mt-4">
            <Button onClick={() => handleContinue()}>
              {isLastEmail
                ? getText(texts.buttons.showResults)
                : getText(texts.buttons.continue)}{" "}
              <FontAwesomeIcon icon={faArrowRight} aria-hidden="true" />
            </Button>
          </div>
        </div>
      );
    }

    return <>{renderActionsRow()}</>;
  };

  if (emailsOfDifficulty.length === 0) {
    return (
      <>
        <Header />
        <h1>{texts.noEmails}</h1>
        <p>
          {texts.props.difficulty}: {difficulty}
        </p>
      </>
    );
  }

  return (
    <Container fluid="md" className="w-50">
      <Header />
      <h5 className="text-center text-uppercase my-5">
        {getText(texts.title, [currentIndex, emailsOfDifficulty.length])}, hráč:{" "}
        {nickname} score: {score}
      </h5>
      <div className="mx-auto mb-4" style={{ maxWidth: "50vw" }}>
        <EmailTemplate
          key={id}
          sender={sender}
          subject={subject}
          content={content}
        />
      </div>
      {renderContent()}
    </Container>
  );
}

export default Game;

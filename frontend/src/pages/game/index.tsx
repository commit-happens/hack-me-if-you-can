import { Alert, Button, Container } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Header from "../../components/header";
import emailsData from "../../data/emails.json";
import useTranslation, { getText } from "../../hooks/useTranslation";
import Page from "../../models/Page";
import { useAppSelector } from "../../store/hooks";
import { getPagePath } from "../../utils/routing";
import EmailTemplate from "./templates/EmailTemplate";
import { Answer as GameAnswer, useGame } from "./useGame";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowRight, faCheck, faWarning } from "@fortawesome/free-solid-svg-icons";

function Game() {
  const navigate = useNavigate();
  const score = useAppSelector((state) => state.game.score);
  const nickname = useAppSelector((state) => state.user.nickname);
  const texts = useTranslation("game");

  const { emails } = emailsData;

  const {
    currentEmail,
    order,
    answer,
    isLastEmail,
    isCorrectAnswer,
    difficulty,
    emailsOfDifficulty,
    handleAnswer,
    handleContinue,
  } = useGame({
    allEmails: emails,
    //  startIndex: 8,
    texts,
    onFinish: () => navigate(getPagePath(Page.Results)),
  });

  const { id, sender, subject, content, explanation } = currentEmail || {};

  /**
   * Zobrazení tlačítek pro odpověď.
   */
  const renderActions = () => {
    return (
      <div style={{ display: "flex", gap: 8, justifyContent: "center" }}>
        <Button variant="outline-secondary" onClick={() => handleAnswer(GameAnswer.Phishing)}>
          <FontAwesomeIcon icon={faWarning} aria-hidden="true" className="text-danger" />
          {getText(texts.answers.phishing)}
        </Button>
        <Button variant="outline-secondary" onClick={() => handleAnswer(GameAnswer.Safe)}>
          <FontAwesomeIcon icon={faCheck} className="text-success" aria-hidden="true" />
          {getText(texts.answers.safe)}
        </Button>
      </div>
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
          <Alert variant={feedbackData.variant}>
            <h2>{feedbackData.title}</h2>
          </Alert>
          {explanation}
          <div style={{ textAlign: "center" }} className="mt-4">
            <Button onClick={() => handleContinue()}>
              {isLastEmail ? getText(texts.buttons.showResults) : getText(texts.buttons.continue)}{" "}
              <FontAwesomeIcon icon={faArrowRight} aria-hidden="true" />
            </Button>
          </div>
        </div>
      );
    }

    return <div>{renderActions()}</div>;
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
      <h1 className="text-center">
        {getText(texts.title, [order, emailsOfDifficulty.length])}, hráč: {nickname} score: {score}
      </h1>
      <div className="mx-auto mb-4" style={{ maxWidth: "50vw" }}>
        <EmailTemplate key={id} sender={sender} subject={subject} content={content} />
      </div>
      {renderContent()}
    </Container>
  );
}

export default Game;

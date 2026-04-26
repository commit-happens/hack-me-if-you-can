import { faArrowRight } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Alert, Button, Container } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Header from "../../components/header";
import emailsData from "../../data/emails.json";
import useTranslation from "../../hooks/useTranslation";
import Page from "../../models/Page";
import { useAppSelector } from "../../store/hooks";
import { getPagePath } from "../../utils/routing";
import ActionsRow from "./components/actionRow";
import EmailTemplate from "./templates/EmailTemplate";
import { useGame } from "./useGame";

function Game() {
  const navigate = useNavigate();
  const score = useAppSelector((state) => state.game.score);
  const nickname = useAppSelector((state) => state.player.nickname);
  const texts = useTranslation("game");

  const { emails } = emailsData;

  const {
    currentEmail,
    currentIndex,
    answer,
    isLastEmail,
    difficulty,
    emailsOfDifficulty,
    handleAnswer,
    handleContinue,
    remainingTime,
    timeoutBgColor,
    timePerQuestion,
    timeoutProgressBarVariant,
    feedbackData,
  } = useGame({
    allEmails: emails,
    texts,
    onFinish: () => navigate(getPagePath(Page.Results)),
  });

  const { id, sender, subject, content, explanation } = currentEmail || {};

  /** Zobrazí zpětnou vazbu na odpověď uživatele. */
  const renderFeedback = () => {
    return (
      <Alert variant={feedbackData.variant} className="text-center">
        <FontAwesomeIcon icon={feedbackData.icon} aria-hidden="true" fontSize={20} />{" "}
        {feedbackData.title}
      </Alert>
    );
  };

  /**
   * Zobrazení obsahu podle toho, zda uživatel odpověděl.
   */
  const renderContent = () => {
    if (answer) {
      return (
        <div className="w-100">
          {renderFeedback()}
          {explanation}
          <div style={{ textAlign: "center" }} className="mt-4">
            <Button
              onClick={() => handleContinue()}
              onKeyDown={(e) => e.key === "Space" && handleContinue()}
              autoFocus
            >
              {isLastEmail ? texts.buttons.showResults : texts.buttons.continue}{" "}
              <FontAwesomeIcon icon={faArrowRight} aria-hidden="true" />
            </Button>
          </div>
        </div>
      );
    }

    return (
      <ActionsRow
        handleAnswer={handleAnswer}
        remainingTime={remainingTime}
        timeoutBgColor={timeoutBgColor}
        timeoutProgressBarVariant={timeoutProgressBarVariant}
        timePerQuestion={timePerQuestion}
        texts={texts}
      />
    );
  };

  if (emailsOfDifficulty.length === 0) {
    return (
      <>
        <Header nickname={nickname} score={score} />
        <h1>{texts.noEmails}</h1>
        <p>
          {texts.props.difficulty}: {difficulty}
        </p>
      </>
    );
  }

  return (
    <Container fluid="sm" className="game-container">
      <Header
        nickname={nickname}
        score={score}
        currentQuestion={currentIndex + 1}
        totalQuestions={emailsOfDifficulty.length}
      />
      <div className="mx-auto mb-4">
        <EmailTemplate key={id} sender={sender} subject={subject} content={content} />
      </div>
      {renderContent()}
    </Container>
  );
}

export default Game;

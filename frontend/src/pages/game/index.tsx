import { faArrowRight } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Alert, Button, Container } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Header from "../../components/header";
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

  const {
    currentEmail,
    currentIndex,
    answerCorrect,
    isLastEmail,
    difficulty,
    allEmails,
    handleAnswer,
    handleContinue,
    remainingTime,
    timeoutBgColor,
    timePerQuestion,
    timeoutProgressBarVariant,
    feedbackData,
    isLoading,
    isError,
    refetch,
  } = useGame({
    texts,
    onFinish: () => navigate(getPagePath(Page.Results)),
  });

  const { id, content, explanation, metadata = {}, problems } = currentEmail || {};
  console.log(problems);

  const { subject, sender } = metadata;

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
    if (answerCorrect !== undefined) {
      return (
        <div className="w-100">
          {renderFeedback()}
          {explanation}
          <div style={{ textAlign: "center" }} className="mt-4">
            <Button onClick={() => handleContinue()} autoFocus>
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

  if (isLoading) {
    return (
      <>
        <Header nickname={nickname} score={score} />
        <Container className="text-center mt-5">
          <h1>Načítám hru...</h1>
        </Container>
      </>
    );
  }

  if (isError) {
    return (
      <>
        <Header nickname={nickname} score={score} />
        <Container className="text-center mt-5">
          <h1>Nepodařilo se načíst hru.</h1>
          <Button onClick={() => refetch()} className="mt-3">
            Zkusit znovu
          </Button>
        </Container>
      </>
    );
  }

  if (allEmails.length === 0) {
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

  if (!content || !sender || !subject) return null;

  return (
    <Container fluid="sm" className="game-container">
      <Header
        nickname={nickname}
        score={score}
        currentQuestion={currentIndex + 1}
        totalQuestions={allEmails.length}
      />
      <div className="mx-auto mb-4">
        <EmailTemplate
          key={id}
          sender={sender}
          subject={subject}
          content={content}
          problems={answerCorrect !== undefined ? problems : undefined}
        />
      </div>
      {renderContent()}
    </Container>
  );
}

export default Game;

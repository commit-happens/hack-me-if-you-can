import { useState } from "react";
import Header from "../../components/header";
import emailsData from "../../data/emails.json";
import useTranslation, { getText } from "../../hooks/useTranslation";
import EmailTemplate from "./templates/EmailTemplate";
import { useNavigate } from "react-router-dom";
import { getPagePath } from "../../utils/routing";
import Page from "../../models/Page";
import { Alert, Button, Container } from "react-bootstrap";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import { incrementScore } from "../../store/slices/gameSlice";
export type EmailModel = {
  id: number;
  sender: string;
  subject: string;
  content: string;
  explanation: string;
  penalty: number;
  phishingPlatformID: number;
  phishingTypeIDs: number[];
  difficulty: number;
};

enum Answer {
  Phishing = "phishing",
  Safe = "safe",
}

type GameProps = {
  /** Obtížnost hry */
  difficulty?: number;
};

function Game(props: GameProps) {
  const { emails } = emailsData;
  const { difficulty = 1 } = props; // Výchozí obtížnost je 1

  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const score = useAppSelector((state) => state.game.score);

  const [emailIndex, setEmailIndex] = useState(8);
  const texts = useTranslation("game");
  const [answer, setAnswer] = useState<Answer | undefined>();

  const emailsOfDifficulty = emails.filter(
    (item) => item.difficulty === difficulty && item.phishingPlatformID === 1,
  );

  const {
    id,
    sender,
    subject,
    content,
    explanation,
    penalty,
    phishingTypeIDs,
  } = emailsOfDifficulty[emailIndex] || {};

  /**
   * Je-li true, uživatel odpověděl správně.
   * @returns
   */
  const isCorrectAnswer = (chosenAnswer?: Answer) => {
    const currentAnswer = chosenAnswer || answer;
    if (!currentAnswer) return false;

    if (currentAnswer === Answer.Phishing) {
      return phishingTypeIDs.length > 0;
    } else {
      return phishingTypeIDs.length === 0;
    }
  };

  const continueButtonLabel =
    emailIndex === emailsOfDifficulty.length - 1
      ? getText(texts.buttons.showResults)
      : getText(texts.buttons.continue);

  /**
   * Pokračování na další e-mail.
   */
  const handleContinue = () => {
    setAnswer(undefined);

    console.log(emailIndex, emailsOfDifficulty.length);

    if (emailIndex === emailsOfDifficulty.length - 1) {
      navigate(getPagePath(Page.Results));
    }
    setEmailIndex((prevIndex) =>
      prevIndex < emails.length - 1 ? prevIndex + 1 : prevIndex,
    );
  };

  const handleAnswer = (selectedAnswer: Answer) => {
    const scoreChange = isCorrectAnswer(selectedAnswer) ? 0 : -penalty;

    if (scoreChange) dispatch(incrementScore(scoreChange));

    setAnswer(selectedAnswer);
  };

  /**
   * Zobrazení tlačítek pro odpověď.
   */
  const renderActions = () => {
    return (
      <div style={{ display: "flex", gap: 8, justifyContent: "center" }}>
        <Button
          variant="outline-danger"
          onClick={() => handleAnswer(Answer.Phishing)}
        >
          {getText(texts.answers.phishing)}
        </Button>
        <Button
          variant="outline-success"
          onClick={() => handleAnswer(Answer.Safe)}
        >
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
        <div>
          <Alert variant={isCorrectAnswer() ? "success" : "danger"}>
            <h2>{feedbackData.title}</h2>
          </Alert>
          <p>{explanation}</p>
          <Button onClick={() => handleContinue()}>
            {continueButtonLabel}
          </Button>
        </div>
      );
    }

    return <div>{renderActions()}</div>;
  };

  if (emailsOfDifficulty.length === 0) {
    return (
      <>
        <Header />
        <h1>{getText(texts.noEmails)}</h1>
        <p>
          {getText(texts.props.difficulty)}: {difficulty}
        </p>
      </>
    );
  }

  return (
    <Container fluid="md">
      <Header />
      <h1 className="text-center">
        {getText(texts.title, [emailIndex + 1, emailsOfDifficulty.length])},
        score: {score}
      </h1>
      <div style={{ marginBottom: 16 }}>
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

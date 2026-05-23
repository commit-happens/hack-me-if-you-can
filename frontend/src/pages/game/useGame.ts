import { faAlarmClock, type IconDefinition } from "@fortawesome/free-solid-svg-icons";
import { faSkullCrossbones } from "@fortawesome/free-solid-svg-icons/faSkullCrossbones";
import { faThumbsUp } from "@fortawesome/free-solid-svg-icons/faThumbsUp";
import { useCallback, useEffect, useState } from "react";
import type { ProgressBarProps } from "react-bootstrap";
import useCountdown from "../../hooks/useCountdown";
import type { Translation } from "../../languages/csCZ";
import { submitAnswerBody } from "../../services/generated-zod/answer-controller/answer-controller";
import { useSubmitAnswer } from "../../services/generated/answer-controller/answer-controller";
import {
  GetRandomQuestionsByDifficultyDifficulty,
  type GetRandomQuestionsByDifficultyParams,
} from "../../services/generated/model";
import { useGetRandomQuestionsByDifficulty } from "../../services/generated/question-controller/question-controller";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import {
  selectSessionId,
  setCurrentIndex,
  setScore,
  setTotalQuestions,
} from "../../store/slices/gameSlice";
import { selectPlayerId } from "../../store/slices/playerSlice";
import { getEnvConfigValue } from "../../utils/envConfig";

export enum Answer {
  Phishing = "phishing",
  Safe = "safe",
}

export type EmailModel = {
  id: number;
  /* Odesílatel e-mailu */
  sender: string;

  /* Předmět e-mailu */
  subject: string;

  /* Obsah e-mailu */
  content: string;

  /* Vysvětlení, proč je e-mail phishing nebo bezpečný */
  explanation: string;

  /* Odměna za správné určení, zda se jedná o phishing */
  reward: number;

  /* ID phishingové platformy */
  phishingPlatformID: number;

  /* ID typů phishingu */
  phishingTypeIDs: number[];

  /* Obtížnost */
  difficulty: number;
};

type UseGameOptions = {
  difficulty?: number;
  startIndex?: number;
  platformId?: number;
};

type UseGameProps = UseGameOptions & {
  texts: Translation["game"];
  onFinish?: () => void;
};

type TimeOutWarningThreshold = {
  secondsRemaining: number;
  colorVariant: ProgressBarProps["variant"];
};

const warningTimeThresholdDivisor = 3;
const criticalTimeThresholdDivisor = 10;
const submitAnswerBodyRemainTimeMax = 60;

enum FeedbackType {
  Correct = "correct",
  Incorrect = "incorrect",
  TimeIsUp = "timeIsUp",
}

type FeedbackData = {
  title: string;
  variant: ProgressBarProps["variant"];
  icon: IconDefinition;
};

export function useGame(props: UseGameProps) {
  const dispatch = useAppDispatch();

  const [answerCorrect, setAnswerCorrect] = useState<boolean | undefined>(undefined);

  const { difficulty = 1, texts, onFinish } = props;

  const feedbackDataByType: Record<FeedbackType, FeedbackData> = {
    [FeedbackType.Correct]: {
      title: texts.feedback.correct,
      variant: "success",
      icon: faThumbsUp,
    },
    [FeedbackType.Incorrect]: {
      title: texts.feedback.incorrect,
      variant: "danger",
      icon: faSkullCrossbones,
    },
    [FeedbackType.TimeIsUp]: {
      title: texts.feedback.timeIsUp,
      variant: "danger",
      icon: faAlarmClock,
    },
  };

  /**
   * Náhodné promíchání e-mailů pro zajištění různorodosti kvízu.
   */
  // const randomizedEmails = useMemo(() => allEmails.sort(() => Math.random() - 0.5), [allEmails]);

  const currentIndex = useAppSelector((state) => state.game.currentIndex);
  const sessionId = useAppSelector(selectSessionId);

  /** Z konfigurace si načteme čas na zodpovězení jedné otázky. */
  const timePerQuestion = Math.min(
    Math.ceil(getEnvConfigValue("VITE_TIME_PER_QUESTION", 60) / difficulty),
    submitAnswerBodyRemainTimeMax,
  );

  const { remainingTime, reset, stop } = useCountdown({
    start: timePerQuestion,
    onCountdownOver: () => {
      handleTimeout();
    },
  });

  const playerId = useAppSelector(selectPlayerId);
  const submitAnswer = useSubmitAnswer({
    mutation: {
      onSuccess: (answerResponse) => {
        if (answerResponse.score != null) {
          dispatch(setScore(answerResponse.score));
        }

        setAnswerCorrect(answerResponse.answer_correct);
      },
      onError: (error) => {
        console.error("Nepodařilo se odeslat odpověď na backend:", error);
      },
    },
  });

  const questionsLimit = getEnvConfigValue("VITE_GAME_QUESTIONS_LIMIT", 20);

  const getRandomQuestionsByDifficultyParams: GetRandomQuestionsByDifficultyParams = {
    difficulty: GetRandomQuestionsByDifficultyDifficulty.EASY,
    limit: questionsLimit,
  };

  const randomQuestionsByDifficultyQuery = useGetRandomQuestionsByDifficulty(
    getRandomQuestionsByDifficultyParams,
  );

  const allEmails = randomQuestionsByDifficultyQuery.data ?? [];

  // Uložit celkový počet otázek do Redux při změně emailsOfDifficulty
  useEffect(() => {
    dispatch(setTotalQuestions(allEmails.length));
  }, [dispatch, allEmails.length]);

  const [answer, setAnswer] = useState<Answer | undefined>(undefined);

  // Aktuální e-mail k zodpovězení.
  const currentEmail = allEmails[currentIndex];

  /** Celkový počet e-mailů k zodpovězení.   */
  const totalEmails = allEmails.length;

  /**
   * Je-li true, jedná se o poslední e-mail kvízu.
   */
  const isLastEmail = currentIndex === totalEmails - 1;

  const continueButtonLabel =
    isLastEmail ? texts.buttons.showResults : texts.buttons.continue;

  /** Definice barev pro upozornění na zbývající čas pro zodpovězení otázky. */

  const timeoutWarningThresholds: TimeOutWarningThreshold[] = [
    {
      secondsRemaining: Math.round(timePerQuestion / criticalTimeThresholdDivisor),
      colorVariant: "danger",
    },
    {
      secondsRemaining: Math.round(timePerQuestion / warningTimeThresholdDivisor),
      colorVariant: "warning",
    },
  ];

  /** Barva odpočítávadla času. */
  const { colorVariant } =
    timeoutWarningThresholds.find(
      (threshold) => threshold.secondsRemaining >= remainingTime,
    ) || {};

  const timeoutBgColor = colorVariant ? `bg-${colorVariant}` : undefined;
  const timeoutProgressBarVariant = colorVariant;

  /** Vrátí typ FeedbackType pro zobrazení reakce na odpověď. */
  const getFeedbackType = () => {
    let feedbackType = FeedbackType.Correct;

    if (answerCorrect === false) feedbackType = FeedbackType.Incorrect;
    if (remainingTime === 0) feedbackType = FeedbackType.TimeIsUp;

    return feedbackType;
  };

  const feedbackData = feedbackDataByType[getFeedbackType()];

  /**
   * Zpracování odpovědi uživatele.
   */
  const handleAnswer = useCallback(
    (selected?: Answer) => {
      const remain_time = Math.max(
        0,
        Math.min(remainingTime, submitAnswerBodyRemainTimeMax),
      );

      if (!currentEmail) return;

      // Okamžitě zastavit odpočítávání
      stop();

      // const correct = isCorrectAnswer(selected);
      if (playerId && sessionId) {
        submitAnswer.mutate({
          data: submitAnswerBody.parse({
            player_id: playerId,
            question_id: currentEmail.id,
            session_id: sessionId,
            is_phishing: selected === Answer.Phishing,
            remain_time,
          }),
        });
      } else {
        console.error("Nelze odeslat odpověď: chybí playerId nebo sessionId.");
      }

      setAnswer(selected);
    },
    [
      currentEmail,
      dispatch,
      playerId,
      remainingTime,
      sessionId,
      stop,
      submitAnswer,
      timePerQuestion,
    ],
  );

  // Když vyprší čas na odpověď, považujeme to za špatnou odpověď.
  const handleTimeout = useCallback(() => {
    if (!currentEmail) return;

    // const wrongChoice = answerCorrect === true ? Answer.Safe : Answer.Phishing;

    // handleAnswer(wrongChoice);
    setAnswerCorrect(false);
  }, [currentEmail, handleAnswer]);

  /**
   * Pokračování na další e-mail nebo dokončení hry (přeskok na stránku Results).
   */
  const handleContinue = useCallback(() => {
    setAnswer(undefined);
    setAnswerCorrect(undefined);
    reset();

    if (isLastEmail) {
      if (onFinish) onFinish();
      return;
    }

    dispatch(setCurrentIndex(currentIndex + 1));
  }, [currentIndex, dispatch, allEmails.length, isLastEmail, onFinish]);

  return {
    currentEmail,
    currentIndex,
    totalEmails,
    answer,
    answerCorrect,
    difficulty,
    isLastEmail,
    continueButtonLabel,
    handleAnswer,
    handleContinue,
    allEmails,
    remainingTime,
    timePerQuestion,
    timeoutBgColor,
    timeoutProgressBarVariant,
    feedbackData,
    isLoading: randomQuestionsByDifficultyQuery.isLoading,
    isError: randomQuestionsByDifficultyQuery.isError,
    refetch: randomQuestionsByDifficultyQuery.refetch,
  };
}

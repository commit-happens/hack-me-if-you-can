import { useCallback, useMemo, useState, useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import {
  increaseCorrectAnswers,
  updateScore,
  setOrder,
} from "../../store/slices/gameSlice";
import type { Translation } from "../../languages/csCZ";
import { getEnvConfigValue } from "../../utils/envConfig";
import type { ProgressBarProps } from "react-bootstrap";

export enum Answer {
  Phishing = "phishing",
  Safe = "safe",
}

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

type UseGameOptions = {
  difficulty?: number;
  startIndex?: number;
  platformId?: number;
};

type UseGameProps = UseGameOptions & {
  texts: Translation["game"];
  allEmails: EmailModel[];
  onFinish?: () => void;
};

type TimeOutWarningThreshold = {
  secondsRemaining: number;
  colorVariant: ProgressBarProps["variant"];
};
export function useGame(props: UseGameProps) {
  const dispatch = useAppDispatch();
  const { difficulty = 1, platformId = 1, allEmails, texts, onFinish } = props;

  const currentIndex = useAppSelector((state) => state.game.currentIndex);

  /** Z konfigurace si načteme čas na zodpovězení jedné otázky. */
  const timePerQuestion = Math.ceil(
    getEnvConfigValue("VITE_TIME_PER_QUESTION", 60) / difficulty,
  );

  const emailsOfDifficulty = useMemo(
    () =>
      allEmails.filter(
        (item) =>
          item.difficulty === difficulty &&
          item.phishingPlatformID === platformId,
      ),
    [allEmails, difficulty, platformId],
  );

  const [answer, setAnswer] = useState<Answer | undefined>(undefined);
  const [remainingTime, setRemainingTime] = useState<number>(timePerQuestion);

  // Aktuální e-mail k zodpovězení.
  const currentEmail = emailsOfDifficulty[currentIndex];

  /**
   * Kontrola správnosti odpovědi.
   */
  const isCorrectAnswer = useCallback(
    (chosen?: Answer) => {
      const currentAnswer = chosen ?? answer;
      if (!currentAnswer || !currentEmail) return false;
      if (currentAnswer === Answer.Phishing) {
        return (currentEmail.phishingTypeIDs || []).length > 0;
      } else {
        return (currentEmail.phishingTypeIDs || []).length === 0;
      }
    },
    [answer, currentEmail],
  );

  /** Celkový počet e-mailů k zodpovězení.   */
  const totalEmails = emailsOfDifficulty.length;

  /**
   * Je-li true, jedná se o poslední e-mail kvízu.
   */
  const isLastEmail = currentIndex === totalEmails - 1;

  const continueButtonLabel = isLastEmail
    ? texts.buttons.showResults
    : texts.buttons.continue;

  /** Definice barev pro upozornění na zbývající čas pro zodpovězení otázky. */
  const timeOutWarningThresholds: TimeOutWarningThreshold[] = [
    {
      secondsRemaining: Math.round(timePerQuestion / 10),
      colorVariant: "danger",
    },
    {
      secondsRemaining: Math.round(timePerQuestion / 3),
      colorVariant: "warning",
    },
  ];

  /** Barva odpočítávadla času. */
  const { colorVariant } =
    timeOutWarningThresholds.find(
      (threshold) => threshold.secondsRemaining >= remainingTime,
    ) || {};

  const timeOutTextColor = colorVariant ? `text-${colorVariant}` : undefined;
  const timeOutProgressBarVariant = colorVariant;

  /**
   * Zpracování odpovědi uživatele.
   */
  const handleAnswer = useCallback(
    (selected: Answer) => {
      if (!currentEmail) return;

      const correct = isCorrectAnswer(selected);
      const scoreChange = correct ? 0 : -currentEmail.penalty;

      if (scoreChange) dispatch(updateScore(scoreChange));

      if (correct) dispatch(increaseCorrectAnswers());
      setAnswer(selected);
    },
    [currentEmail, dispatch, isCorrectAnswer],
  );

  // Když vyprší čas na odpověď, považujeme to za špatnou odpověď.
  const handleTimeout = useCallback(() => {
    if (!currentEmail) return;

    const phishingIsCorrect = (currentEmail.phishingTypeIDs || []).length > 0;
    const wrongChoice = phishingIsCorrect ? Answer.Safe : Answer.Phishing;

    handleAnswer(wrongChoice);
  }, [currentEmail, handleAnswer]);

  /**
   * Pokračování na další e-mail nebo dokončení hry.
   */
  const handleContinue = useCallback(() => {
    setAnswer(undefined);
    setRemainingTime(timePerQuestion);

    if (isLastEmail) {
      if (onFinish) onFinish();
      return;
    }

    dispatch(setOrder(currentIndex + 1));
  }, [
    currentIndex,
    dispatch,
    emailsOfDifficulty.length,
    isLastEmail,
    onFinish,
  ]);

  // Odpočítávadlo času pro zodpovězení otázky.
  useEffect(() => {
    setRemainingTime(timePerQuestion);
    if (!currentEmail || answer) return;

    let mounted = true;
    const tick = () => {
      setRemainingTime((prev) => {
        if (prev <= 1) {
          if (mounted) {
            handleTimeout();
          }
          return 0;
        }
        return prev - 1;
      });
    };

    const interval = setInterval(tick, 1000);
    return () => {
      mounted = false;
      clearInterval(interval);
    };
  }, [currentIndex, currentEmail, answer, handleTimeout]);

  return {
    currentEmail,
    currentIndex,
    totalEmails,
    answer,
    difficulty,
    isLastEmail,
    continueButtonLabel,
    isCorrectAnswer,
    handleAnswer,
    handleContinue,
    emailsOfDifficulty,
    remainingTime,
    timePerQuestion,
    timeOutTextColor,
    timeOutProgressBarVariant,
  };
}

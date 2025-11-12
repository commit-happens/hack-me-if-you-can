// src/pages/game/useGame.ts
import { useCallback, useMemo, useState } from "react";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import { increaseCorrectAnswers, incrementScore, setOrder } from "../../store/slices/gameSlice";
import type { Translation } from "../../languages/csCZ";

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

export function useGame(props: UseGameProps) {
  const dispatch = useAppDispatch();
  const { difficulty = 1, platformId = 1, allEmails, texts, onFinish } = props;

  const order = useAppSelector((state) => state.game.order);

  const emailsOfDifficulty = useMemo(
    () =>
      allEmails.filter(
        (item) => item.difficulty === difficulty && item.phishingPlatformID === platformId
      ),
    [allEmails, difficulty, platformId]
  );

  const [answer, setAnswer] = useState<Answer | undefined>(undefined);

  const currentEmail = emailsOfDifficulty[order];

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
    [answer, currentEmail]
  );

  const totalEmails = emailsOfDifficulty.length;
  const isLastEmail = order === totalEmails - 1;

  const continueButtonLabel = isLastEmail ? texts?.buttons.showResults : texts?.buttons.continue; // tady můžeš použít i lokalizaci

  /**
   * Zpracování odpovědi uživatele.
   */
  const handleAnswer = useCallback(
    (selected: Answer) => {
      if (!currentEmail) return;

      const correct = isCorrectAnswer(selected);
      const scoreChange = correct ? 0 : -currentEmail.penalty;

      if (scoreChange) dispatch(incrementScore(scoreChange));

      if (correct) dispatch(increaseCorrectAnswers());
      setAnswer(selected);
    },
    [currentEmail, dispatch, isCorrectAnswer]
  );

  /**
   * Pokračování na další e-mail nebo dokončení hry.
   */
  const handleContinue = useCallback(() => {
    setAnswer(undefined);

    if (isLastEmail) {
      if (onFinish) onFinish();
      return;
    }

    dispatch(setOrder(order < emailsOfDifficulty.length ? order + 1 : order));
  }, [emailsOfDifficulty.length, isLastEmail, onFinish]);

  const reset = useCallback(() => {
    setAnswer(undefined);
    dispatch(setOrder(1));
  }, []);

  return {
    currentEmail,
    order,
    totalEmails,
    answer,
    difficulty,
    isLastEmail,
    continueButtonLabel,
    isCorrectAnswer,
    handleAnswer,
    handleContinue,
    reset,
    emailsOfDifficulty,
  };
}

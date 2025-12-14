import { useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import { getPagePath } from "../../utils/routing";
import Page from "../../models/Page";
import { startGame } from "../../store/slices/gameSlice";

/** Funkcionalita pro stránku Results. */
const useResults = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const { correctAnswers, score, totalQuestions } = useAppSelector(
    (state) => state.game,
  );

  const total = totalQuestions;
  const wrongAnswers = total - correctAnswers;

  const successRate =
    total > 0 ? Math.round((correctAnswers / total) * 100) : 0;

  const playAgain = () => {
    navigate(getPagePath(Page.Game));
    dispatch(startGame());
  };

  return {
    correctAnswers,
    wrongAnswers,
    total,
    successRate,
    score,
    playAgain,
  };
};

export default useResults;

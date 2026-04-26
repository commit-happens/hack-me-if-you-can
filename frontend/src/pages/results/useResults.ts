import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import { getPagePath } from "../../utils/routing";
import Page from "../../models/Page";
import { selectSessionId, startGame } from "../../store/slices/gameSlice";
import { selectPlayerId } from "../../store/slices/playerSlice";
import { useGetPlayerSummary } from "../../services/generated/player-controller/player-controller";

/** Funkcionalita pro stránku Results. */
const useResults = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const {
    correctAnswers,
    score: localScore,
    totalQuestions,
  } = useAppSelector((state) => state.game);
  const sessionId = useAppSelector(selectSessionId);
  const playerId = useAppSelector(selectPlayerId);

  const summaryQuery = useGetPlayerSummary(playerId, { session_id: sessionId });

  useEffect(() => {
    if (summaryQuery.error) {
      console.error("Nepodařilo se načíst finální summary hráče:", summaryQuery.error);
    }
  }, [summaryQuery.error]);

  const total = totalQuestions;
  const wrongAnswers = total - correctAnswers;
  const score = summaryQuery.data?.score ?? localScore;

  const successRate = total > 0 ? Math.round((correctAnswers / total) * 100) : 0;

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

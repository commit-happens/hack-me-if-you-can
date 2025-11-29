import { useEffect, useState } from "react";
import { useAppSelector } from "../../store/hooks";
import { getPlayers, type PlayerModel } from "../../services/playerService";

const useLeaderboard = () => {
  const { score, isPlaying } = useAppSelector((state) => state.game);
  const { nickname, playerId } = useAppSelector((state) => state.player);
  const [players, setPlayers] = useState<PlayerModel[]>([]);

  /** Seřazené skóre hráčů od nejvyššího po nejnižší. */
  const sortedScores = players.sort((a, b) => b.score - a.score);

  /** Top 10 hráčů v žebříčku. */
  const top10Scores = sortedScores.slice(0, 10);

  /** Aktuální skóre je nižší než skóre 10. nejlepších */
  const scoreIsBelowTop10 = score < top10Scores[top10Scores.length - 1]?.score;

  /** Umístění aktuálního hráče v žebříčku. */
  const currentPlayerScoreIndex = sortedScores.findIndex((player) => player.playerId === playerId);

  useEffect(() => {
    (async () => {
      const response = await getPlayers();
      setPlayers(response);
    })();
  }, []);

  return {
    score,
    isPlaying,
    scoreIsBelowTop10,
    currentPlayerScoreIndex,
    nickname,
    playerId,
    top10Scores,
  };
};

export default useLeaderboard;

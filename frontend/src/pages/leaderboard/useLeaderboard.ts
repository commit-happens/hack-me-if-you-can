import { useMemo } from "react";
import { useAppSelector } from "../../store/hooks";
import { useGetPlayers } from "../../services/generated/player-controller/player-controller";
import type { PlayerResponse } from "../../services/generated/model";

export type PlayerModel = Required<PlayerResponse>;

const normalizePlayer = (player: PlayerResponse): PlayerModel | null => {
  if (player.playerId == null || player.nickname == null || player.score == null) {
    return null;
  }

  return {
    playerId: player.playerId,
    nickname: player.nickname,
    score: player.score,
  };
};

const useLeaderboard = () => {
  const { score, isPlaying } = useAppSelector((state) => state.game);
  const { nickname, playerId } = useAppSelector((state) => state.player);
  const { data } = useGetPlayers();
  const players = useMemo(
    () => (data ?? []).map(normalizePlayer).filter(Boolean) as PlayerModel[],
    [data],
  );

  /** Seřazené skóre hráčů od nejvyššího po nejnižší. */
  const sortedScores = players.sort((a, b) => b.score - a.score);

  /** Top 10 hráčů v žebříčku. */
  const top10Scores = sortedScores.slice(0, 10);

  /** Aktuální skóre je nižší než skóre 10. nejlepších */
  const scoreIsBelowTop10 = score < top10Scores[top10Scores.length - 1]?.score;

  /** Umístění aktuálního hráče v žebříčku. */
  const currentPlayerScoreIndex = sortedScores.findIndex(
    (player) => player.playerId === playerId,
  );

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

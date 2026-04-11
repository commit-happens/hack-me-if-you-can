import { useNavigate } from "react-router-dom";
import Page from "../../models/Page";
import { addPlayerBody } from "../../services/generated-zod/player-controller/player-controller";
import { useAddPlayer } from "../../services/generated/player-controller/player-controller";
import { useAppDispatch } from "../../store/hooks";
import { startGame } from "../../store/slices/gameSlice";
import { setPlayer } from "../../store/slices/playerSlice";
import { getPagePath } from "../../utils/routing";
import { useState, type ChangeEvent, type KeyboardEvent } from "react";
import type { PlayerResponse } from "../../services/generated/model";

export function useWelcome() {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const addPlayer = useAddPlayer();

  const [nickname, setNickname] = useState<string>("");
  const [error, setError] = useState(true);

  // Nový stav pro sledování načítání aby uživatel nemohl odeslat vícekrát request
  const [isLoading, setIsLoading] = useState(false);

  const isNicknameValid = (value: string) => {
    return addPlayerBody.shape.nickname.safeParse(value.trim()).success;
  };

  const normalizePlayer = (player: PlayerResponse) => {
    if (player.playerId == null || player.nickname == null || player.score == null) {
      throw new Error("Neplatná data hráče z backendu.");
    }

    return {
      playerId: player.playerId,
      nickname: player.nickname,
      score: player.score,
    };
  };

  const handleStart = async () => {
    if (isLoading) {
      return;
    }

    const trimmedNickname = nickname.trim();
    if (!isNicknameValid(trimmedNickname)) {
      setError(true);
      return;
    }

    setIsLoading(true);
    try {
      // Odeslání na backend a získání playerId
      const payload = addPlayerBody.parse({
        nickname: trimmedNickname,
      });
      const response = await addPlayer.mutateAsync({ data: payload });
      const player = normalizePlayer(response);
      console.log("Uživatel vytvořen:", player);

      // Uložení nickname + playerId do Redux store
      dispatch(startGame());
      dispatch(setPlayer({ nickname: player.nickname, playerId: player.playerId }));
      navigate(getPagePath(Page.Game));
    } catch (error) {
      console.error("Nastala chyba při vytváření uživatele:", error);
      // Přesnější chybová hláška z BE error objektu (např. uživatelské jméno již existuje nebo je neplatné kvůli délce)
      const message =
        error instanceof Error ?
          error.message
        : "Nepodařilo se uložit hráče. Zkuste to prosím znovu.";
      alert(message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const targetValue = event.target.value;

    setError(!isNicknameValid(targetValue));
    setNickname(targetValue);
  };

  const handleKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter" && !error && !isLoading) {
      handleStart();
    }
  };

  return { handleStart, isLoading, handleKeyDown, handleChange, nickname, error };
}

import { useState } from "react";
import Header from "../../components/header";
import useTranslation from "../../hooks/useTranslation";
import type { BasePageProps } from "../../models/BasePageProps";
import Page from "../../models/Page";

function Welcome(props: BasePageProps) {
  const { navigate, page } = props;
  const [nickname, setNickname] = useState<string>("");
  const [error, setError] = useState(true);

  const appTexts = useTranslation("app");
  const welcomeTexts = useTranslation("welcome");

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setError(event.target.value === "");
    setNickname(event.target.value);
  };

  const handleStart = () => {
    if (!navigate) return;
    navigate(Page.Game);
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter" && !error) {
      handleStart();
    }
  };
  return (
    <>
      <Header navigate={navigate} page={page} />

      <h1>{appTexts.title}</h1>
      <div>
        <p>{welcomeTexts.welcomeMessage}</p>
        <p>{welcomeTexts.instruction1}</p>
        <p>{welcomeTexts.instruction2}</p>
        <div>
          <div>
            <h2>{welcomeTexts.nicknameLabel}</h2>
          </div>
          <p>
            <input
              type="text"
              id="nickname"
              name="nickname"
              placeholder={welcomeTexts.nicknamePlaceholder}
              autoFocus
              value={nickname}
              required
              onChange={handleChange}
              onKeyDown={handleKeyDown}
            />
          </p>
        </div>
        <button disabled={error} onClick={() => handleStart()}>
          {welcomeTexts.startButton}
        </button>
      </div>
    </>
  );
}

export default Welcome;

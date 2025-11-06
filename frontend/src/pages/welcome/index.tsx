import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../components/header";
import useTranslation from "../../hooks/useTranslation";
import Page from "../../models/Page";
import { getPagePath } from "../../utils/routing";

function Welcome() {
  const navigate = useNavigate();
  const [nickname, setNickname] = useState<string>("");
  const [error, setError] = useState(true);

  const appTexts = useTranslation("app");
  const welcomeTexts = useTranslation("welcome");

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setError(event.target.value === "");
    setNickname(event.target.value);
  };

  const handleStart = () => {
    navigate(getPagePath(Page.Game));
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter" && !error) {
      handleStart();
    }
  };
  return (
    <>
      <Header />

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

import { faBullseye, faUserAstronaut } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link, useLocation } from "react-router-dom";
import logo from "../../assets/logo-hmiyc.png";
import useTranslation, { getText } from "../../hooks/useTranslation";
import Page from "../../models/Page";
import { getPagePath } from "../../utils/routing";

type HeaderProps = {
  nickname?: string;
  score?: number;
  currentQuestion?: number;
  totalQuestions?: number;
};

function Header({
  nickname = "Player",
  score,
  currentQuestion,
  totalQuestions,
}: HeaderProps) {
  const location = useLocation();
  const pathname = location.pathname;

  const texts = useTranslation("header");
  const textsApp = useTranslation("app");
  const textsGame = useTranslation("game");

  // Header se nemá zobrazovat na Welcome stránce
  const hideOnWelcome = pathname === getPagePath(Page.Welcome);
  if (hideOnWelcome) return null;

  const isGamePage = pathname === getPagePath(Page.Game);

  return (
    <header
      style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "0.5em 0.1em",
        marginBottom: "2em",
      }}
    >
      <Link
        to={getPagePath(Page.Welcome)}
        style={{
          display: "flex",
          alignItems: "center",
          gap: "10px",
          textDecoration: "none",
          color: "inherit",
        }}
      >
        <img src={logo} alt="HMIYC Logo" style={{ height: "36px" }} />
        <span>{textsApp.title}</span>
      </Link>

      <div style={{ display: "flex", alignItems: "center", gap: "1em" }}>
        {isGamePage &&
          currentQuestion !== undefined &&
          totalQuestions !== undefined && (
            <div>
              {getText(textsGame.title, [currentQuestion, totalQuestions])}
            </div>
          )}
        <div style={{ display: "flex", alignItems: "center", gap: "0.2em" }}>
          <FontAwesomeIcon icon={faUserAstronaut} className="text-warning" />{" "}
          {nickname}
        </div>

        <div style={{ display: "flex", alignItems: "center", gap: "0.2em" }}>
          <FontAwesomeIcon icon={faBullseye} className="text-danger" />
          <span>
            {texts.score}: {score ?? 0}
          </span>
        </div>
      </div>
    </header>
  );
}

export default Header;

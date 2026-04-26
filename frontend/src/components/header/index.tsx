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

function Header({ nickname = "Player", score, currentQuestion, totalQuestions }: HeaderProps) {
  const location = useLocation();
  const pathname = location.pathname;

  const texts = useTranslation("header");
  const textsApp = useTranslation("app");
  const textsGame = useTranslation("game");

  const isGamePage = pathname === getPagePath(Page.Game);

  return (
    <header
      style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "0.5em 0.1em",
        marginBlock: "1em 2em",
      }}
    >
      <Link
        to={getPagePath(Page.Welcome)}
        role="link"
        style={{
          display: "flex",
          alignItems: "center",
          gap: "0.6em",
          textDecoration: "none",
          color: "inherit",
        }}
      >
        <img src={logo} alt="HMIYC Logo" style={{ height: "36px", borderRadius: "4px" }} />
        <span className="nav-item--label">{textsApp.title}</span>
      </Link>

      <div className="nav-items">
        {isGamePage && currentQuestion !== undefined && totalQuestions !== undefined && (
          <div className="nav-item">
            🎮 {getText(textsGame.title, [currentQuestion, totalQuestions])}
          </div>
        )}
        <div className="nav-item">
          <FontAwesomeIcon icon={faUserAstronaut} className="text-warning" />
          {texts.player}: {nickname}
        </div>

        <div className="nav-item">
          <FontAwesomeIcon icon={faBullseye} className="text-danger" />
          <span className="nav-item--label">{texts.score}: </span>
          <span>{score ?? 0}</span>
        </div>
      </div>
    </header>
  );
}

export default Header;

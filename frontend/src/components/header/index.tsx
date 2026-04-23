import { Link, useLocation } from "react-router-dom";
import logo from "../../assets/logo-hmiyc.png";
import coin from "../../assets/Otter_coin.png";
import idCard from "../../assets/OtterIDcard_finalversion.png";
import mailIcon from "../../assets/Mail_icon.png";
import useTranslation from "../../hooks/useTranslation";
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

  const textsApp = useTranslation("app");

  const isGamePage = pathname === getPagePath(Page.Game);

  return (
    <header
      style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "0.5em 0.1em",
        marginBlock: "1em 2em",
        flexWrap: "wrap",
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
          minWidth: 0,
        }}
      >
        <img
          src={logo}
          alt="HMIYC Logo"
          style={{ height: "40px", borderRadius: "4px" }}
        />
        <span className="nav-item--label hide-on-mobile">{textsApp.title}</span>
      </Link>

      <div className="nav-items" style={{ display: "flex", alignItems: "center", gap: "0.8em", flexWrap: "wrap", justifyContent: "flex-end" }}>
        {isGamePage &&
          currentQuestion !== undefined &&
          totalQuestions !== undefined && (
            <div className="nav-item" style={{ display: "flex", alignItems: "center", gap: "0.35em" }}>
              <img
                src={mailIcon}
                alt="Mail progress"
                className="mobile-icon"
                style={{ height: "60px"}}
              />
              <span>{currentQuestion}/{totalQuestions}</span>
            </div>
          )}
        <div className="nav-item" style={{ display: "flex", alignItems: "center", gap: "0.4em" }}>
          <img
            src={idCard}
            alt="Player ID"
            className="mobile-icon"
            style={{ height: "60px" }}
          />
          <span
          className="nickname-text"
          style={{
            maxWidth: "100px",
            overflow: "hidden",
            textOverflow: "ellipsis",
            whiteSpace: "nowrap",
            display: "inline-block",
          }}
          >
          {nickname}
        </span>
        </div>

        <div className="nav-item" style={{ display: "flex", alignItems: "center", gap: "0.3em" }}>
          <img
            src={coin}
            alt="Coin"
            className="mobile-icon"
            style={{ height: "60px" }}
          />
          <span>{score ?? 0}</span>
        </div>
      </div>
      <style>{`
        @media (max-width: 680px) {
          .hide-on-mobile {
            display: none;
          }
        }
        @media (max-width: 540px) {
          .mobile-icon {
            height: 40px !important;
          }
        }
        @media (max-width: 427px) {
          .nickname-text {
            max-width: 70px !important;
          }
          .nav-items {
            gap: 0.25em !important;
          }
          .nav-item {
            gap: 0.2em !important;
          }
        }
      `}</style>
    </header>
  );
}

export default Header;

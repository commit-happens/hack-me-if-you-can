import { Link, useLocation } from "react-router-dom";
import useTranslation from "../../hooks/useTranslation";
import Page from "../../models/Page";
import { getPagePath } from "../../utils/routing";
import logo from "../../assets/logo-hmiyc.png";

type HeaderProps = {
  nickname?: string;
  score?: number;              // ✅ PŘIDÁNO
  currentQuestion?: number;
  totalQuestions?: number;
};

function Header({
  nickname = "Player",
  score,                       // ✅ PŘIDÁNO
  currentQuestion,
  totalQuestions,
}: HeaderProps) {
  const location = useLocation();
  const pathname = location.pathname;
  const texts = useTranslation("app"); // necháme, zatím ho používáme třeba později

  // Header se nemá zobrazovat na Welcome stránce
  const hideOnWelcome = pathname === getPagePath(Page.Welcome);
  if (hideOnWelcome) return null;

  const isGamePage = pathname === getPagePath(Page.Game); // ✅ PŘIDÁNO

  return (
    <header
      style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "12px 20px",
        borderBottom: "1px solid #ddd",
        background: "white",
      }}
    >
      {/* 🟦 LEVÁ STRANA — LOGO */}
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
        {/* ✅ ZMĚNA TEXTU */}
        <strong style={{ fontSize: "18px" }}>Hack me if you can</strong>
      </Link>

      {/* 🟨 STŘED — HRA A/N jen na Game stránce */}
      {isGamePage &&
        currentQuestion !== undefined &&
        totalQuestions !== undefined && (
          <div style={{ fontWeight: "bold", fontSize: "18px" }}>
            HRA {currentQuestion}/{totalQuestions}
          </div>
        )}

      {/* 🟥 PRAVÁ STRANA — hráč + score */}
      <div style={{ display: "flex", alignItems: "center", gap: "20px" }}>
        <div style={{ display: "flex", alignItems: "center", gap: "6px" }}>
          {/* ✅ ikona k hráči */}
          <span>🪪</span>
          <strong>hráč: {nickname}</strong>
        </div>

        <div style={{ display: "flex", alignItems: "center", gap: "6px" }}>
          {/* ✅ ikona k score */}
          <span>🎯</span>
          <strong>score: {score ?? 0}</strong>
        </div>
      </div>
    </header>
  );
}

export default Header;

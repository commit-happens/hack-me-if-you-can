import React from "react";
import { useNavigate } from "react-router-dom";
import { getPagePath } from "../../utils/routing";
import Page from "../../models/Page";
import { Button } from "react-bootstrap";

const ResultsPage: React.FC = () => {
  const navigate = useNavigate();

  // Mock data
  const correct = 8;
  const wrong = 2;
  const total = correct + wrong;
  const percent = total > 0 ? Math.round((correct / total) * 100) : 0;

  const handlePlayAgain = () => {
    navigate(getPagePath(Page.Game));
  };

  return (
    <main style={styles.page}>
      <h1 style={styles.title}>Jak ti to šlo</h1>

      <section style={styles.stats}>
        <div style={styles.statCard}>
          <div style={styles.statLabel}>Dobré odpovědi</div>
          <div style={styles.statValue}>
            ✅ {correct} / {total}
          </div>
        </div>

        <div style={styles.statCard}>
          <div style={styles.statLabel}>Špatné odpovědi</div>
          <div style={styles.statValue}>
            ❌ {wrong} / {total}
          </div>
        </div>

        <div style={styles.statCardFullWidth}>
          <div style={styles.statLabel}>Úspěšnost</div>
          <div style={{ ...styles.statValue, fontSize: 20 }}>{percent}%</div>
        </div>
      </section>

      <div style={styles.actions}>
        <Button type="button" onClick={handlePlayAgain}>
          Hrát znovu
        </Button>
      </div>
    </main>
  );
};

const styles: { [k: string]: React.CSSProperties } = {
  page: {
    maxWidth: 720,
    margin: "40px auto",
    padding: 20,
    fontFamily: "Inter, Roboto, Arial, sans-serif",
    color: "#111827",
  },
  title: {
    margin: "0 0 20px 0",
    fontSize: 28,
    fontWeight: 700,
    textAlign: "center",
  },
  stats: {
    display: "grid",
    gridTemplateColumns: "repeat(2, 1fr)",
    gap: 12,
    marginBottom: 24,
  },
  statCard: {
    padding: 16,
    borderRadius: 8,
    background: "#f8fafc",
    boxShadow: "0 1px 2px rgba(0,0,0,0.04)",
    textAlign: "center",
  },
  statCardFullWidth: {
    gridColumn: "1 / -1",
    padding: 16,
    borderRadius: 8,
    background: "#eef2ff",
    boxShadow: "0 1px 2px rgba(0,0,0,0.04)",
    textAlign: "center",
  },
  statLabel: {
    fontSize: 13,
    color: "#6b7280",
    marginBottom: 8,
  },
  statValue: {
    fontSize: 18,
    fontWeight: 600,
  },
  actions: {
    textAlign: "center",
  },
};

export default ResultsPage;

import React from "react";
import { Button, Card, Container, Row, Col, ProgressBar } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import emailsData from "../../data/emails.json";
import Page from "../../models/Page";
import { getPagePath } from "../../utils/routing";
import useResults from "./useResults";

const ResultsPage: React.FC = () => {
  const navigate = useNavigate();
  const { correctAnswers, wrongAnswers, total, percentage, score } = useResults({
    allEmails: emailsData.emails,
  });

  const handlePlayAgain = () => {
    navigate(getPagePath(Page.Game));
  };

  return (
    <Container style={styles.page} as="main">
      <h1 style={styles.title}>Tvoje skóre: {score}</h1>

      <Row className="g-3 mb-3">
        <Col xs={12} md={6}>
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Subtitle className="mb-2 text-muted">Dobré odpovědi</Card.Subtitle>
              <Card.Text style={styles.statValue}>
                ✅ {correctAnswers} / {total}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>

        <Col xs={12} md={6}>
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Subtitle className="mb-2 text-muted">Špatné odpovědi</Card.Subtitle>
              <Card.Text style={styles.statValue}>
                ❌ {wrongAnswers} / {total}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>

        <Col xs={12}>
          <Card>
            <Card.Body>
              <Card.Subtitle className="mb-2 text-muted">Úspěšnost</Card.Subtitle>
              <div className="d-flex align-items-center">
                <div style={{ flex: 1, marginRight: 12 }}>
                  <ProgressBar now={percentage} label={`${percentage} %`} />
                </div>
                <div style={{ minWidth: 64, textAlign: "right", fontWeight: 600 }}>
                  {percentage} %
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <div style={styles.actions}>
        <Button type="button" onClick={handlePlayAgain}>
          Hrát znovu
        </Button>
      </div>
    </Container>
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

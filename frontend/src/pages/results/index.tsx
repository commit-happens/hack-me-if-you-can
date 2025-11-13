import React from "react";
import { Button, Card, Container, Row, Col, ProgressBar } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import emailsData from "../../data/emails.json";
import Page from "../../models/Page";
import { getPagePath } from "../../utils/routing";
import useResults from "./useResults";
import useTranslation from "../../hooks/useTranslation";
import { useAppDispatch } from "../../store/hooks";
import { startGame } from "../../store/slices/gameSlice";

const ResultsPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { correctAnswers, wrongAnswers, total, percentage, score } = useResults({
    allEmails: emailsData.emails,
  });
  const texts = useTranslation("results");

  const handlePlayAgain = () => {
    navigate(getPagePath(Page.Game));
    dispatch(startGame());
  };

  return (
    <Container className="mx-auto my-4 p-3 text-dark" as="main">
      <h3 className="mb-3 fs-3 fw-bold text-center">{texts.yourScore}</h3>
      <div className="text-center display-1">{score}</div>

      <Row className="g-3 mb-3">
        <Col xs={12} md={6}>
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Subtitle className="mb-2 text-muted">{texts.correctAnswers}</Card.Subtitle>
              <Card.Text className="fs-5 fw-semibold">
                ✅ {correctAnswers} / {total}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>

        <Col xs={12} md={6}>
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Subtitle className="mb-2 text-muted">{texts.wrongAnswers}</Card.Subtitle>
              <Card.Text className="fs-5 fw-semibold">
                ❌ {wrongAnswers} / {total}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>

        <Col xs={12}>
          <Card>
            <Card.Body>
              <Card.Subtitle className="mb-2 text-muted">{texts.success}</Card.Subtitle>
              <div className="d-flex align-items-center">
                <div className="flex-grow-1 me-3">
                  <ProgressBar now={percentage} label={`${percentage} %`} />
                </div>
                <div className="text-end fw-semibold" style={{ minWidth: 64 }}>
                  {percentage} %
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <div className="text-center">
        <Button type="button" onClick={handlePlayAgain}>
          {texts.playAgain}
        </Button>
      </div>
    </Container>
  );
};
export default ResultsPage;

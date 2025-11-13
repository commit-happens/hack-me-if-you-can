import React from "react";
import {
  Button,
  Card,
  Col,
  Container,
  ProgressBar,
  Row,
} from "react-bootstrap";
import emailsData from "../../data/emails.json";
import useTranslation from "../../hooks/useTranslation";
import useResults from "./useResults";

const ResultsPage: React.FC = () => {
  const { correctAnswers, wrongAnswers, total, successRate, score, playAgain } =
    useResults({
      allEmails: emailsData.emails,
    });
  const texts = useTranslation("results");

  return (
    <Container className="mx-auto my-4 p-3 text-dark" as="main">
      <h3 className="mb-3 fs-3 fw-bold text-center">{texts.yourScore}</h3>
      <div className="text-center display-1">{score}</div>

      <Row className="g-3 mb-3">
        <Col xs={12} md={6}>
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Subtitle className="mb-2 text-muted">
                {texts.correctAnswers}
              </Card.Subtitle>
              <Card.Text className="fs-5 fw-semibold">
                ✅ {correctAnswers} / {total}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>

        <Col xs={12} md={6}>
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Subtitle className="mb-2 text-muted">
                {texts.wrongAnswers}
              </Card.Subtitle>
              <Card.Text className="fs-5 fw-semibold">
                ❌ {wrongAnswers} / {total}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>

        <Col xs={12}>
          <Card>
            <Card.Body>
              <Card.Subtitle className="mb-2 text-muted">
                {texts.success}
              </Card.Subtitle>
              <div className="d-flex align-items-center">
                <div className="flex-grow-1 me-3">
                  <ProgressBar now={successRate} label={`${successRate} %`} />
                </div>
                <div className="text-end fw-semibold" style={{ minWidth: 64 }}>
                  {successRate} %
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <div className="text-center">
        <Button type="button" onClick={playAgain}>
          {texts.playAgain}
        </Button>
      </div>
    </Container>
  );
};
export default ResultsPage;

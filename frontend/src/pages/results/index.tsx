import { faCircleXmark, faRepeat, faTrophy } from "@fortawesome/free-solid-svg-icons";
import { faCheckCircle } from "@fortawesome/free-solid-svg-icons/faCheckCircle";
import { faClose } from "@fortawesome/free-solid-svg-icons/faClose";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { Button, Card, Col, Container, Row } from "react-bootstrap";
import emailsData from "../../data/emails.json";
import useTranslation from "../../hooks/useTranslation";
import useResults from "./useResults";

const ResultsPage: React.FC = () => {
  const { correctAnswers, wrongAnswers, total, successRate, score, playAgain } = useResults({
    allEmails: emailsData.emails,
  });
  const texts = useTranslation("results");

  return (
    <Container className="mx-auto my-4 p-3 text-dark" as="main">
      <h3 className="mb-3 fs-3 fw-bold text-center">{texts.yourScore}</h3>
      <div className="text-center display-1 mb-4">{score}</div>

      <Row className="g-3 mb-5 justify-content-center">
        <Col xs={12} md={6}>
          <Card className="">
            <Card.Body>
              <Card.Text className="fs-5 fw-semibold">
                <Row>
                  <Col xs={8}>
                    <FontAwesomeIcon
                      icon={faCheckCircle}
                      aria-hidden="true"
                      className="text-success"
                    />{" "}
                    {texts.correctAnswers}:
                  </Col>
                  <Col className="text-end">
                    {correctAnswers} / {total}
                  </Col>
                  <Col xs={8}>
                    <FontAwesomeIcon
                      icon={faCircleXmark}
                      aria-hidden="true"
                      className="text-danger"
                    />{" "}
                    {texts.wrongAnswers}:
                  </Col>
                  <Col className="text-end">
                    {wrongAnswers} / {total}
                  </Col>
                  <Col xs={12}>
                    <hr />
                  </Col>
                  <Col xs={8}>
                    <FontAwesomeIcon icon={faTrophy} aria-hidden="true" className="text-warning" />{" "}
                    {texts.success}:
                  </Col>
                  <Col className="text-end">{successRate} %</Col>
                </Row>
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <div className="text-center">
        <Button type="button" onClick={playAgain}>
          <FontAwesomeIcon icon={faRepeat} aria-hidden="true" /> {texts.playAgain}
        </Button>
      </div>
    </Container>
  );
};
export default ResultsPage;

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import Header from "../../components/header";
import useTranslation from "../../hooks/useTranslation";
import Page from "../../models/Page";
import { getPagePath } from "../../utils/routing";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { useAppDispatch } from "../../store/hooks";
import { startGame } from "../../store/slices/gameSlice";
import Col from "react-bootstrap/Col";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import { setNickname } from "../../store/slices/userSlice";

function Welcome() {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const [nickname, setNicknameState] = useState<string>("");
  const [error, setError] = useState(true);

  const appTexts = useTranslation("app");
  const welcomeTexts = useTranslation("welcome");

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setError(event.target.value === "");
    setNicknameState(event.target.value);
  };

  const handleStart = () => {
    dispatch(startGame());
    dispatch(setNickname(nickname));
    navigate(getPagePath(Page.Game));
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter" && !error) {
      handleStart();
    }
  };
  return (
    <Container fluid="md">
      <Header />
      <Row>
        <Col className="d-flex flex-column gap-4">
          <h1 className="display-3 text-center">{appTexts.title}</h1>
          <div>
            <p>
              {welcomeTexts.welcomeMessage} {welcomeTexts.instruction1}
            </p>
            <p>{welcomeTexts.instruction2}</p>
            <div className="text-center">
              <div className="mt-5 mb-3">
                <h2 className="display-6">{welcomeTexts.nicknameLabel}</h2>
              </div>
              <p>
                <Form.Control
                  placeholder={welcomeTexts.nicknamePlaceholder}
                  autoFocus
                  type="text"
                  id="nickname"
                  name="nickname"
                  value={nickname}
                  required
                  className="d-inline-block w-50 text-center"
                  onChange={handleChange}
                  onKeyDown={handleKeyDown}
                />
              </p>
              <Button variant="primary" disabled={error} onClick={() => handleStart()}>
                {welcomeTexts.startButton}
              </Button>
            </div>
          </div>
        </Col>
      </Row>
    </Container>
  );
}

export default Welcome;

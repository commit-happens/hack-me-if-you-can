import Button from "react-bootstrap/Button";
import Col from "react-bootstrap/Col";
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import Image from "react-bootstrap/Image";
import Row from "react-bootstrap/Row";
import logo from "../../assets/logo-hmiyc.png";
import useTranslation from "../../hooks/useTranslation";
import {
  addPlayerBodyNicknameMax,
  addPlayerBodyNicknameMin,
} from "../../services/generated-zod/player-controller/player-controller";
import { useWelcome } from "./useWelcome";

function Welcome() {
  const { handleStart, isLoading, handleKeyDown, handleChange, nickname, error } =
    useWelcome();

  const appTexts = useTranslation("app");
  const welcomeTexts = useTranslation("welcome");

  return (
    <Container className="mt-4">
      <Row>
        <Col className="d-flex flex-column gap-4">
          <h1 className="display-4 text-center">{appTexts.title}</h1>
          <div>
            <div className="border rounded-1 p-4 mx-auto text-center welcome-message">
              <Image
                src={logo}
                alt={appTexts.guide.bubble}
                roundedCircle
                className="guide-bubble"
              />
              <p>{welcomeTexts.welcomeMessage}</p>
              <p>
                {welcomeTexts.instruction0}
                {welcomeTexts.instruction1}
              </p>
              <p>{welcomeTexts.instruction2}</p>
            </div>
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
                  maxLength={addPlayerBodyNicknameMax}
                  required
                  className="d-inline-block text-center username-field"
                  onChange={handleChange}
                  onKeyDown={handleKeyDown}
                />
              </p>
              <p>
                {error && nickname.trim().length > 0 && (
                  <Form.Text className="text-danger">
                    {`Přezdívka musí mít ${addPlayerBodyNicknameMin} až ${addPlayerBodyNicknameMax} znaků.`}
                  </Form.Text>
                )}
              </p>
              <Button
                variant="primary"
                size="lg"
                disabled={error || isLoading}
                title="fdafa"
                onClick={() => handleStart()}
              >
                {isLoading ? "Ukládání..." : welcomeTexts.startButton}
              </Button>
            </div>
          </div>
        </Col>
      </Row>
    </Container>
  );
}

export default Welcome;

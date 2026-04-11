import { Col, Container, Row } from "react-bootstrap";
import useTranslation from "../../hooks/useTranslation";

import useLeaderboard, { type PlayerModel } from "./useLeaderboard";

const LeaderBoard = () => {
  const texts = useTranslation("leaderboard");
  const {
    isPlaying,
    scoreIsBelowTop10,
    currentPlayerScoreIndex,
    nickname,
    playerId,
    top10Scores,
    score,
  } = useLeaderboard();

  const LeaderBoardRow = (props: { player: PlayerModel; index: number }) => {
    const { player, index } = props;

    const icon = getScoreIcon(index);
    const emphasize = isPlaying && player.playerId === playerId;

    return (
      <Row
        key={player.playerId}
        className={`border-bottom py-2${emphasize ? " fw-bold bg-light" : ""}`}
      >
        <Col xs={1} className="text-center">
          {icon ? icon : index + 1}
        </Col>
        <Col>{player.nickname}</Col>
        <Col xs={2} className="text-end">
          {player.score}
        </Col>
      </Row>
    );
  };

  return (
    <Container className="w-50 pt-4">
      <h2 className="text-center">{texts.top10}</h2>
      <Row className=" border-bottom py-4 small">
        <Col xs={1} className="text-center">
          #
        </Col>
        <Col>{texts.nickname}</Col>
        <Col xs={2} className="text-end">
          {texts.score}
        </Col>
      </Row>
      <div>
        {top10Scores.map((player, index) => {
          return <LeaderBoardRow key={player.playerId} player={player} index={index} />;
        })}
      </div>
      {isPlaying && scoreIsBelowTop10 && (
        <>
          <Row>
            <Col>.</Col>
          </Row>
          <Row>
            <Col>.</Col>
          </Row>
          <Row>
            <Col>.</Col>
          </Row>
          <LeaderBoardRow
            player={{ playerId, nickname, score }}
            index={currentPlayerScoreIndex}
          />
        </>
      )}
    </Container>
  );
};

function getScoreIcon(position: number) {
  switch (position) {
    case 0:
      return "🥇";
    case 1:
      return "🥈";
    case 2:
      return "🥉";
    default:
      return undefined;
  }
}

export default LeaderBoard;

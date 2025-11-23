import { Col, Container, Row } from "react-bootstrap";
import playerScores from "../../data/player.mock.json";
import { useAppSelector } from "../../store/hooks";
import useTranslation from "../../hooks/useTranslation";

type PlayerScore = {
  id: number;
  nickname: string;
  score: number;
};

const LeaderBoard = () => {
  const texts = useTranslation("leaderboard");
  const { score, isPlaying } = useAppSelector((state) => state.game);
  const { nickname, playerId } = useAppSelector((state) => state.player);

  const scores = playerScores.players as PlayerScore[];

  const sortedScores = scores.sort((a, b) => b.score - a.score);
  const top10Scores = sortedScores.slice(0, 10);

  const lowerScoreThanTop10 = score < top10Scores[top10Scores.length - 1]?.score;

  const currentPlayerScoreIndex = sortedScores.findIndex((player) => player.id === playerId);

  const LeaderBoardRow = (props: { player: PlayerScore; index: number }) => {
    const { player, index } = props;

    const icon = getScoreIcon(index);
    const emphasize = isPlaying && player.id === playerId;

    return (
      <Row key={player.id} className={`border-bottom py-2${emphasize ? " fw-bold bg-light" : ""}`}>
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
          return <LeaderBoardRow key={player.id} player={player} index={index} />;
        })}
      </div>
      {isPlaying && lowerScoreThanTop10 && (
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
            player={{ id: playerId, nickname, score }}
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

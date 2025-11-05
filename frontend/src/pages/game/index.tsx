import Header from "../../components/header";
import type { BasePageProps } from "../../models/BasePageProps";

function Game(props: BasePageProps) {
  const { page, navigate } = props;
  return (
    <>
      <Header navigate={navigate} page={page} />
      <h1>Game Page</h1>
      <p>TODO: Implement the game page.</p>
    </>
  );
}

export default Game;

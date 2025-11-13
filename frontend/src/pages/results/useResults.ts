import { useAppSelector } from "../../store/hooks";
import type { EmailModel } from "../game/useGame";

type UseResultsProps = {
  allEmails: EmailModel[];
};

const useResults = (props: UseResultsProps) => {
  const { correctAnswers, score } = useAppSelector((state) => state.game);
  const { allEmails } = props;

  const total = allEmails.length;
  const wrongAnswers = total - correctAnswers;

  const percentage = total > 0 ? Math.round((correctAnswers / total) * 100) : 0;

  console.log(total, correctAnswers, percentage, score);

  return { correctAnswers, wrongAnswers, total, percentage, score };
};

export default useResults;

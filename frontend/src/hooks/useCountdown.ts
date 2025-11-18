import { useEffect, useState } from "react";

/** Hook pro odpočítávání času. */
interface UseCountdownProps {
  /** Počet sekund, od kterých se má odpočítávání spustit. */
  start: number;

  /** Callback funkce, která se zavolá po skončení odpočítávání. */
  onCountdownOver?: () => void;
}

const useCountdown = (props: UseCountdownProps) => {
  const { start = 10, onCountdownOver } = props;

  const [progress, setProgress] = useState(0);

  const reset = () => {
    setProgress(0);
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      if (progress === start) {
        if (onCountdownOver) {
          onCountdownOver();
        }
        return;
      }

      if (progress < start) {
        setProgress(progress + 1);
        return;
      }
      setProgress(0);
    }, 1000);

    return () => clearTimeout(timer);
  }, [progress]);
  return { remainingTime: start - progress, reset };
};

export default useCountdown;

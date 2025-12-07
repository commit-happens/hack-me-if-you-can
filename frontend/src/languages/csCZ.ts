export type Translation = typeof csCZ;

const csCZ = {
  app: {
    title: "Hack me if you can",
    titleAbbr: "HMIYC",
  },
  header: {
    player: "Hráč",
    score: "Skóre",
  },
  welcome: {
    welcomeMessage:
      "Ahoj, kyberdetektive! 🕵️‍♂️ Tvým úkolem je odhalit, které e-maily jsou falešné (phishing) a které jsou bezpečné.",
    instruction1:
      "Čti pozorně odesílatele, předmět i text zprávy. Někdy totiž rozhodují drobnosti.",
    instruction2: "Klikni na „Start“ a ukaž, jestli tě hackeři dokážou napálit… nebo ne! 💪",
    nicknameLabel: "Jak ti mám říkat?",
    nicknamePlaceholder: "Zadej svou přezdívku",
    startButton: "Start!",
  },
  game: {
    title: "Hra {1}/{2}",
    noEmails: "Nenašli jsme žádný e-mail.",
    props: {
      difficulty: "Obtížnost",
    },
    answers: {
      phishing: "Phishing!",
      safe: "Je to OK",
    },
    feedback: {
      correct: "Výborně, odpověděl(a) jsi správně!",
      incorrect: "Bohužel, odpověděl(a) jsi špatně. ",
      timeIsUp: "Čas vypršel! Bohužel ti musíme odečíst body.",
    },
    buttons: {
      continue: "Pokračovat",
      showResults: "Zobrazit výsledky",
    },
  },
  template: {
    sender: "Odesílatel",
    subject: "Předmět",
    content: "Obsah",
  },
  results: {
    yourScore: "Tvoje skóre",
    correctAnswers: "Správné odpovědi",
    wrongAnswers: "Špatné odpovědi",
    success: "Úspěšnost",
    playAgain: "Hrát znovu",
  },
  leaderboard: {
    top10: "Top 10 hráčů",
    nickname: "Přezdívka",
    score: "Skóre",
  },
};
export default csCZ;

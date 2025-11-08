export type Translation = typeof csCZ;

const csCZ = {
  app: {
    title: "Hack me if you can",
    titleAbbr: "HMIYC",
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
      phishing: "To je Phishing!",
      safe: "Je to OK",
    },
    feedback: {
      correct: "Výborně!",
      incorrect: "Bohužel :(",
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
};
export default csCZ;

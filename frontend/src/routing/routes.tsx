import type { JSX } from "react";
import Page from "../models/Page";
import Game from "../pages/game";
import Welcome from "../pages/welcome";

type Route = {
  path: string;
  element: JSX.Element | null;
};

export const routes: Record<Page, Route> = {
  [Page.Welcome]: {
    path: "/",
    element: <Welcome />,
  },
  [Page.Game]: {
    path: "/game",
    element: <Game />,
  },
  [Page.Results]: {
    path: "/results",
    element: null,
  },
  [Page.Leaderboard]: {
    path: "/leaderboard",
    element: null,
  },
};

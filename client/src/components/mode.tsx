import React from "react";
import { Home } from "./Home";
import { Results } from "./Results";
import { Saved } from "./Saved";

export enum Modes {
  home,
  results,
  saved,
}
export const ScreenMap: Map<Modes, (props: any) => React.JSX.Element> = new Map(
  [
    [Modes.home, Home],
    [Modes.results, Results],
    [Modes.saved, Saved],
  ]
);

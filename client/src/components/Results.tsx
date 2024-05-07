import { useEffect } from "react";
import { Modes } from "./mode";
import { Dispatch, SetStateAction } from "react";
interface ResultsProps {
  mode: Modes;
  setMode: Dispatch<SetStateAction<Modes>>;
}
export function Results(props: ResultsProps) {
  useEffect(() => {
    console.log("Results component mounted");
    return () => {
      console.log("Results component unmounted");
    };
  });
  return (
    <div>
      <text>Results</text>
    </div>
  );
}

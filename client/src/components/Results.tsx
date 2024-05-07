import { useEffect } from "react";
import { Modes } from "./mode";
import { Dispatch, SetStateAction } from "react";
import { Event } from "./Event";
import { Ticket } from "./Ticket";
import "../styles/results.css";
interface ResultsProps {
  mode: Modes;
  setMode: Dispatch<SetStateAction<Modes>>;
  query: string;
}
export function Results(props: ResultsProps) {
  useEffect(() => {
    console.log("Results component mounted");
    return () => {
      console.log("Results component unmounted");
    };
  });
  return (
    <div className="results-container">
      <div className="event-title-container">
        <h1 className="event-title">Events</h1>
      </div>
      <div
        style={{
          width: "100px",
          height: "150px",
          backgroundColor: "transparent",
        }}
      ></div>
      <Event name={"bla"} date={"tomorrow"} time={"2pm"} />
      <Event
        name={"manolo shits on jason"}
        date={"all the time"}
        time={"any time"}
      />
    </div>
  );
}

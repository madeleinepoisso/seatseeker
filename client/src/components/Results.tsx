import { useEffect } from "react";
import { Modes } from "./mode";
import { Dispatch, SetStateAction } from "react";
import { Event } from "./Event";
import { Ticket } from "./Ticket";
import "../styles/results.css";
import { useState } from "react";
import { ReactElement } from "react";
interface ResultsProps {
  mode: Modes;
  setMode: Dispatch<SetStateAction<Modes>>;
  query: string;
  setQuery: Dispatch<SetStateAction<string>>;
}
export function Results(props: ResultsProps) {
  const [events, setEvents] = useState<ReactElement<typeof Event>[]>([]);
  useEffect(() => {
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
      //events go here
    </div>
  );
}

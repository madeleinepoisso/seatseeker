import { useEffect } from "react";
import { Modes } from "./mode";
import { Dispatch, SetStateAction } from "react";
import { Event } from "./Event";
import { Ticket } from "./Ticket";
import "../styles/results.css";
import { Props } from "./App";

export function Saved(props: Props) {
  useEffect(() => {
    console.log("Results component mounted");
    return () => {
      console.log("Results component unmounted");
    };
  });
  return (
    <div className="results-container">
      <div className="event-title-container">
        <h1 className="event-title">Saved Events</h1>
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

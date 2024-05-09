import { useEffect } from "react";
import { Modes } from "./mode";
import { Dispatch, SetStateAction } from "react";
import { Event } from "./Event";
import { Ticket } from "./Ticket";
import "../styles/results.css";
import { useState } from "react";
import { ReactElement, Fragment } from "react";
import { Props } from "./App";
import { getEvents } from "../utils/get_events";
export function Results(props: Props) {
  const [events, setEvents] = useState<ReactElement<typeof Event>[]>([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    if (props.query === "") {
      return;
    }
    setLoading(true);
    getEvents(
      props.query,
      props.cityQuery,
      props.dateQuery,
      props.timeQuery
    ).then((events) => {
      console.log("in here again");
      setEvents(events);
      setLoading(false);
      props.setQuery("");
      props.setCityQuery("");
      props.setDateQuery("");
      props.setTimeQuery("");
    });
    props.setQuery("");

    return () => {
      console.log("Results component unmounted");
    };
  }, []);
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
      {loading ? (
        // Render a loading screen while waiting for events to load
        <div className="loading-container">
          <p className="loading">Loading...</p>
        </div>
      ) : (
        // Render the events once they are loaded
        <div className="results-box">
          {events.map((event, index) => (
            <Fragment key={index}>{event}</Fragment>
          ))}
        </div>
      )}
    </div>
  );
}

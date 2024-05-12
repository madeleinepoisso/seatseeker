import { Fragment, ReactElement, useEffect, useState } from "react";
import { Modes } from "./mode";
import { Dispatch, SetStateAction } from "react";
import { Event } from "./Event";
import { Ticket } from "./Ticket";
import "../styles/results.css";
import "../styles/saved.css";
import { getLoginCookie } from "../utils/cookie";
import { getSavedEvents, removeSavedEvent } from "../utils/api";
import { AuthErrorCodes } from "firebase/auth";

interface Event {
  name: string;
  date: string;
  time: string;
  city: string;
}

export function Saved(props: Event) {
  const [events, setEvents] = useState<Event[]>([]);
  const USER_ID = getLoginCookie() || "";
  const handleRemoveEvent = async (name, date, time) => {
    await removeSavedEvent(name, date, time);
    console.log("event removed");
    getSavedEvents().then((data) => {
      console.log("Events data:", data.data);
      setEvents(data.data);
      console.log(data.data);
    });
  };
  useEffect(() => {
    getSavedEvents().then((data) => {
      console.log("Events data:", data.data);
      setEvents(data.data);
      console.log(data.data);
    });
    console.log("Events component mounted");
  }, events);

  return (
    <div className="results-container">
      <div className="event-title-container">
        <h1 className="event-title" data-testid="saved-title">Saved Events</h1>
      </div>
      <div
        style={{
          width: "100px",
          height: "150px",
          backgroundColor: "transparent",
        }}
      ></div>

      {events &&
        events.map((event, index) => (
          <div className="event-container" data-testid="saved-event">
            <button
              className="remove-button"
              onClick={() =>
                handleRemoveEvent(event.name, event.date, event.time)
              }
            >
              Remove
            </button>
            <div className="event-info">
              <span
                style={{
                  color: "#FFF",
                  paddingLeft: 30,
                  fontFamily:
                    "system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif",
                  fontSize: "30px",
                  whiteSpace: "pre-line",
                  marginBottom: "30px",
                }}
              >
                {event.name}
                <div
                  style={{
                    height: 20,
                  }}
                ></div>{" "}
              </span>
              <span
                style={{
                  paddingLeft: 30,
                  width: 150,
                  color: "rgb(230, 57, 70)",
                  fontFamily:
                    "system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif",
                  fontSize: "30px",
                  fontWeight: 200,
                  whiteSpace: "pre-line",
                }}
              >
                {"\n"}
                {"\n"}
                {event.date}
                {"\n"}
                <div
                  style={{
                    height: 20,
                  }}
                ></div>
                {event.time}
              </span>
            </div>
          </div>
        ))}
    </div>
  );
}

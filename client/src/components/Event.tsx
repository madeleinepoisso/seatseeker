import { Ticket } from "./Ticket";
import "../styles/Event.css";
import emptyHeart from "../emptyheart.png";
import { ReactElement, Fragment } from "react";

interface EventProps {
  name: string;
  date: string;
  time: string;
  city: string;
  tickets: ReactElement<typeof Ticket>[];
}
export function Event(props: EventProps) {
  return (
    <div className="event-container">
      <div className="event-info">
        <img src={emptyHeart} />
        <span
          style={{
            color: "#FFF",
            fontFamily:
              "system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif",
            fontSize: "30px",
          }}
        >
          {props.name}
        </span>
        <span></span>
        <span></span>
        <span
          style={{
            color: "#FFF",
            fontFamily:
              "system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif",
            fontSize: "30px",
          }}
        >
          {props.date}
        </span>
        <span
          style={{
            color: "#FFF",
            fontFamily:
              "system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif",
            fontSize: "30px",
          }}
        >
          {props.time}
        </span>
      </div>
      {props.tickets.map((ticket, index) => (
        <Fragment key={index}>{ticket}</Fragment>
      ))}
    </div>
  );
}

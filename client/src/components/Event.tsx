import { Ticket } from "./Ticket";
import "../styles/Event.css";
interface EventProps {
  name: string;
  date: string;
  time: string;
}
export function Event(props: EventProps) {
  return (
    <div className="event-container">
      <div className="event-info">
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
      <Ticket
        price={140}
        title={"event to attend so fun"}
        seat={"first row"}
        link={"https://www.google.com/"}
      />
      <Ticket
        price={140}
        title={"event to attend so fun"}
        seat={"first row"}
        link={"https://www.google.com/"}
      />
      <Ticket
        price={140}
        title={"event to attend so fun"}
        seat={"first row"}
        link={"https://www.google.com/"}
      />
    </div>
  );
}

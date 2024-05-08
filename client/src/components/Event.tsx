import { Ticket } from "./Ticket";
import "../styles/Event.css";
import emptyHeart from "../emptyheart.png";
import filledHeart from "../filledheart.png";
import { ReactElement, Fragment, useState } from "react";
import { addSavedEvent } from "../utils/api";
import { Props } from "./App";

interface EventProps {
  name: string;
  date: string;
  time: string;
  city: string;
  tickets: ReactElement<typeof Ticket>[];
}
const MyButton: React.FC<EventProps> = (props) => {
  const [events, setEvents] = useState<EventProps[]>([]);
  const [isSaved, setIsSaved] = useState(false);
  const addFavoriteEvent = async () => {
    // - update the client words state to include the new word
    const eventToAdd: EventProps = {
      name: props.name,
      city: props.city,
      date: props.date,
      time: props.time,
      tickets: [],
    }
    setEvents([...events, eventToAdd]);
    console.log("event added");
    // - query the backend to add the new word to the database
    await addSavedEvent(props.name, props.city, props.date, props.time);
  };
  const handleSaveButtonClick = () => {
    addFavoriteEvent();
    setIsSaved(true); // Set isSaved to true after adding the event
  };



  return (
    <button className="save-button" onClick={handleSaveButtonClick}>
      <img src={isSaved ? filledHeart : emptyHeart} alt="Heart" />
    </button>
  );
};

export function Event(props: EventProps) {
  return (
    <div className="event-container">
      <div className="event-info">
        < MyButton name={props.name}
          city={props.city}
          date={props.date}
          time={props.time}
          tickets={[]} />
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

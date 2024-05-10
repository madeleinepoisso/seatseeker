import "../styles/Ticket.css";
import hyperlink from "../besthyperlink.png";
export interface ticketProps {
  price: number;
  title: string;
  seat: string;
  link: string;
}
export function Ticket(props: ticketProps) {
  return (
    <div className="ticket-container">
      <div className="ticket-price">
        <h1 className="ticket-text">{"price: " + "$" + props.price}</h1>
      </div>
      <div className="ticket-seat">
        <h1 className="ticket-text">{"seat: " + props.seat}</h1>
      </div>
      <div className="ticket-seat">
        <h1 className="ticket-text">{"title: " + props.title}</h1>
      </div>
      <a href={props.link} className="ticket-hyperlink" target="_blank">
        <img src={hyperlink}></img>
      </a>
    </div>
  );
}

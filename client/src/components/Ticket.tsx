import "../styles/Ticket.css";
interface ticketProps {
  price: number;
  title: string;
  seat: string;
}
export function Ticket(props: ticketProps) {
  return <div className="ticket-container"></div>;
}

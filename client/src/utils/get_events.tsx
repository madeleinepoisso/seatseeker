import React, { ReactElement } from "react";
import { Event } from "../components/Event";
import { Ticket, ticketProps } from "../components/Ticket";
interface EventData {
  name: string;
  date: string;
  time: string;
  city: string;
  tickets: ticketDataProps[];
}
interface ticketDataProps {
  name: string;
  price: number;
  seat: string;
  link: string;
}
export async function getEvents(
  query: string
): Promise<ReactElement<typeof Event>[]> {
  const events: EventData[] = await getEventData(query);
  console.log(events);
  const events_array: ReactElement<typeof Event>[] = [];
  for (const event of events) {
    console.log(event);
    const eventTickets: ReactElement<typeof Ticket>[] = [];
    for (let i = 0; i < event.tickets.length; i++) {
      const ticket = event.tickets[i];
      eventTickets.push(
        <Ticket
          key={i}
          price={ticket.price}
          title={ticket.name}
          seat={ticket.seat}
          link={ticket.link}
        />
      );
    }
    events_array.push(
      <Event
        key={event.name}
        date={event.date}
        name={event.name}
        time={event.time}
        city={event.city}
        tickets={eventTickets}
      />
    );
  }
  return events_array;
}
function getEventData(query: string): Promise<any> {
  return new Promise((resolve) => {
    fetch("http://localhost:3232/query?query=" + query)
      .then((response) => response.json())
      .then((json) => {
        const data = json.events;
        resolve(JSON.parse(data));
      })
      .catch((error) => {
        console.error("Error:", error);
        resolve([]);
      });
  });
}

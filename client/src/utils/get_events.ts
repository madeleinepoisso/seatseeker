import { ReactElement } from "react";
import { Event } from "../components/Event";
export async function getEvents(
  query: string
): Promise<ReactElement<typeof Event>[]> {
  const data = await getEventData(query);
}
function getEventData(query: string): Promise<ReactElement<typeof Event>[]> {
  return new Promise((resolve) => {
    fetch("http://localhost:3232/query?query=" + query)
      .then((response) => response.json())
      .then((json) => {
        const data = json.data;
        resolve(data);
      })
      .catch((error) => {
        console.error("Error:", error);
        resolve([]);
      });
  });
}

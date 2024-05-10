import { getLoginCookie } from "./cookie";

const HOST = "http://localhost:3232";

/**
 * Queries the API with specified endpoint and query parameters.
 * @param endpoint - The API endpoint to query.
 * @param query_params - The query parameters to be sent with the request.
 * @returns A promise that resolves to the JSON response from the API.
 */
async function queryAPI(
  endpoint: string,
  query_params: Record<string, string>
): Promise<any> {
  const paramsString = new URLSearchParams(query_params).toString();
  const url = `${HOST}/${endpoint}?${paramsString}`;
  const response = await fetch(url);
  if (!response.ok) {
    console.error(response.status, response.statusText);
  }
  return response.json();
}

/**
 * Adds pins to the API.
 * @param longitude - The longitude of the pin.
 * @param latitude - The latitude of the pin.
 * @returns A promise that resolves to the response from the API.
 */
export async function addSavedEvent(
  name: string,
  city: string,
  date: string,
  time: string
): Promise<any> {
  return await queryAPI("add-events", {
    uid: getLoginCookie() || "",
    name: name,
    city: city,
    date: date,
    time: time,
  });
}
export async function removeSavedEvent(
  name: string,
  date: string,
  time: string
): Promise<any> {
  return await queryAPI("remove-event", {
    uid: getLoginCookie() || "",
    name: name,
    date: date,
    time: time,
  });
}

/**
 * Retrieves pins from the API.
 * @returns A promise that resolves to the response from the API.
 */
export async function getSavedEvents(): Promise<any> {
  return await queryAPI("list-events", {
    uid: getLoginCookie() || "",
  });
}

/**
 * Clears pins associated with the specified user ID from the API.
 * @param uid - The user ID for which pins need to be cleared. Defaults to the logged-in user's ID if not provided.
 * @returns A promise that resolves to the response from the API.
 */
export async function clearPins(
  uid: string = getLoginCookie() || ""
): Promise<any> {
  return await queryAPI("clear-events", {
    uid: uid,
  });
}

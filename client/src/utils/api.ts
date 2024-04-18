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
export async function addPins(latitude: number, longitude: number,  pinID: number): Promise<any> {
  return await queryAPI("add-pins", {
    uid: getLoginCookie() || "",
    pinId: pinID.toString(),
    lat: latitude.toString(),
    lng: longitude.toString(), 
  });
}

/**
 * Retrieves pins from the API.
 * @returns A promise that resolves to the response from the API.
 */
export async function getPins(): Promise<any> {
  return await queryAPI("list-pins", {
    uid: getLoginCookie() || "",
  });
}

/**
 * Clears pins associated with the specified user ID from the API.
 * @param uid - The user ID for which pins need to be cleared. Defaults to the logged-in user's ID if not provided.
 * @returns A promise that resolves to the response from the API.
 */
export async function clearPins(uid: string = getLoginCookie() || ""): Promise<any> {
  return await queryAPI("clear-pins", {
    uid: uid,
  });
}

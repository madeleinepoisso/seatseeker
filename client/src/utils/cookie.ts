import Cookies from "js-cookie";

/**
 * Adds a login cookie with the provided user ID.
 * @param uid - The user ID to be stored in the cookie.
 */
export function addLoginCookie(uid: string): void {
  Cookies.set("uid", uid);
}

/**
 * Removes the login cookie.
 */
export function removeLoginCookie(): void {
  Cookies.remove("uid");
}

/**
 * Retrieves the user ID stored in the login cookie, if any.
 * @returns The user ID stored in the cookie, or undefined if the cookie doesn't exist.
 */
export function getLoginCookie(): string | undefined {
  return Cookies.get("uid");
}
import { expect, test } from "@playwright/test";
import { clearPins } from "../../src/utils/api";

/**
  The general shapes of tests in Playwright Test are:
    1. Navigate to a URL
    2. Interact with the page
    3. Assert something about the page against your expectations
  Look for this pattern in the tests below!
 */

const SPOOF_UID = "mock-user-id";
function wait(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

test.beforeEach(
  "add spoof uid cookie to browser",
  async ({ context, page }) => {
    // - Add "uid" cookie to the browser context
    await context.addCookies([
      {
        name: "uid",
        value: SPOOF_UID,
        url: "http://localhost:8000",
      },
    ]);

    // wipe everything for this spoofed UID in the database.
    //await clearE(SPOOF_UID);
  }
);

test("check items on load", async ({
  page,
}) => {
  await page.goto("http://localhost:8000/");
  await expect(page.getByTestId("compare")).toBeVisible();
  await expect(page.getByTestId("sign-out")).toBeVisible();
  await expect(page.getByTestId("save-events")).toBeVisible();
  await expect(page.getByTestId("seatseeker")).toBeVisible();


});

test("sign out removes saved events tab", async ({
  page,
}) => {
  await page.goto("http://localhost:8000/");
  await expect(page.getByTestId("compare")).toBeVisible();
  await expect(page.getByTestId("sign-out")).toBeVisible();
  await expect(page.getByTestId("save-events")).toBeVisible();
  await expect(page.getByTestId("seatseeker")).toBeVisible();
  await (page.getByTestId("sign-out")).click();
  await expect(page.getByTestId("save-events")).toBeHidden();



});

test("search brings to loading and result page", async ({ page }) => {
  await page.goto('http://localhost:8000/');
  await page.getByPlaceholder('enter keyword here').click();
  await page.getByPlaceholder('enter keyword here').fill('celtics');
  await page.locator('div').filter({ hasText: /^Search$/ }).getByRole('button').click();
  await expect(page.getByText('Loading...')).toBeVisible();
  await expect(page.getByRole('heading', { name: 'Events' })).toBeVisible();
  await wait(15000)
  await expect(page.locator('.event-container').first()).toBeVisible();


});

test("saved events page", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await (page.getByTestId("save-events")).click();
  await expect(page.getByTestId("saved-title")).toBeVisible();

});

test("saved events page with event saved", async ({ page }) => {
  await page.goto('http://localhost:8000/');
  await page.getByPlaceholder('enter keyword here').click();
  await page.getByPlaceholder('enter keyword here').fill('argentina vs chile');
  await page.locator('div').filter({ hasText: /^Search$/ }).getByRole('button').click();
  await expect(page.getByText('Loading...')).toBeVisible();
  await expect(page.getByRole('heading', { name: 'Events' })).toBeVisible();
  await wait(25000)
  await expect(page.locator('.event-container').first()).toBeVisible();
  await page.getByTestId('heart').first().click();
  await (page.getByTestId("save-events")).click();
  //await wait(2000)
  await expect(page.getByTestId("saved-event").first()).toBeVisible();

});

test("events stay upon reload", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await (page.getByTestId("save-events")).click();
  await expect(page.getByTestId("saved-event").first()).toBeVisible();
  await page.goto("http://localhost:8000/");
  await (page.getByTestId("save-events")).click();
  await expect(page.getByTestId("saved-event").first()).toBeVisible();


});

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
    await clearPins(SPOOF_UID);
  }
);

test("check that the elements is visible upon load", async ({
  page,
}) => {
  await page.goto("http://localhost:8000/");
  await expect(page.locator('.mapboxgl-map')).toBeVisible();
  await expect(page.locator('button.SignOut')).toBeVisible();
  await expect(page.locator('.repl-input')).toBeVisible();
  await expect(page.locator('input.repl-command-box')).toBeVisible();
  await expect(page.locator('button[aria-label="Submit Command Button"]')).toBeVisible();
  await expect(page.locator('button:text("Clear Pins")')).toBeVisible();
  //add some tests to check that all the fields are visible
  
  });
  

test("testing submitting consecutive key words", async ({ page }) => {
  await page.goto('http://localhost:8000/');
  await page.fill('input[placeholder="Enter command here!"]', 'brooks');
  await page.getByRole("button", { name: "Submit" }).click();
  const inputField = page.locator('input[placeholder="Enter command here!"]');
  await expect(inputField).toBeVisible();
  await expect(inputField).toBeEnabled();
  await expect(inputField).toHaveValue('');
  await page.fill('input[placeholder="Enter command here!"]', 'foreign');
  await page.getByRole("button", { name: "Submit" }).click();
  await expect(inputField).toBeVisible();
  await expect(inputField).toBeEnabled();
  await expect(inputField).toHaveValue('');
});

test("clearing of pins", async ({ page }) => {
  await page.goto("http://localhost:8000/");
  await page.getByLabel('Map', { exact: true }).click({ position: { x: 848, y: 211 } });
  await expect(page.locator('path').first()).toBeVisible();
  await page.getByLabel('Map', { exact: true }).click({ position: { x: 896, y: 203 } });
  await expect(page.locator('path').nth(2)).toBeVisible();
  await page.getByRole('button', { name: 'Clear Pins' }).click();
  await expect(page.locator('path').first()).not.toBeVisible();
  await expect(page.locator('path').nth(2)).not.toBeVisible({ timeout: 5000 });
 
});

test("pins stay upon relaod", async ({ page }) => {
  await page.goto("http://localhost:8000/");


await page.getByLabel('Map', { exact: true }).click({
  position: {
    x: 881,
    y: 214
  }
});
await page.getByLabel('Map', { exact: true }).click({
  position: {
    x: 810,
    y: 295
  }
});
await page.getByLabel('Map', { exact: true }).click({
  position: {
    x: 1085,
    y: 393
  }
});
await expect(page.locator('div:nth-child(3) > svg > path').first()).toBeVisible();
await page.goto('http://localhost:8000/');
await expect(page.locator('div:nth-child(3) > svg > path').first()).toBeVisible();
});

import { expect, test } from "vitest";

test("1 + 2 should be 3", () => {
  expect(1 + 2).toBe(3);
});

test("testing that the boundaries work correctly", async ()=>{
  const maxLat = 100.00;
  const minLat = -80.00;
  const maxLong = 80.00;
  const minLong = -90.00;
  const response = await fetch(`http://localhost:3232/geo-data?maxLat=${maxLat}&minLat=${minLat}&maxLong=${maxLong}&minLong=${minLong}`);
  const json = await response.json();
  expect(json).toHaveProperty("features");
  expect(Array.isArray(json.features)).toBe(true);
  expect(json.features.length).toBeGreaterThan(0);
  json.features.forEach(feature => {
    expect(feature).toHaveProperty("properties");
    expect(feature.properties).toHaveProperty("city");
    expect(feature.properties).toHaveProperty("holc_grade");
  });
  json.features.forEach(feature => {
    expect(feature).toHaveProperty("geometry");
    expect(feature.geometry).toHaveProperty("coordinates");
    expect(Array.isArray(feature.geometry.coordinates)).toBe(true);
    feature.geometry.coordinates.forEach(polygon => {
      polygon.forEach(lineString => {
        lineString.forEach(coordinate => {
          const [longitude, latitude] = coordinate;
          expect(latitude).toBeGreaterThanOrEqual(minLat);
          expect(latitude).toBeLessThanOrEqual(maxLat);
          expect(longitude).toBeGreaterThanOrEqual(minLong);
          expect(longitude).toBeLessThanOrEqual(maxLong);
        });
      });
    });
  });
});

test("testing that zero boundaries return an empty features array", async () => {
  const maxLat = 0;
  const minLat = 0;
  const maxLong = 0;
  const minLong = 0;
  const response = await fetch(`http://localhost:3232/geo-data?maxLat=${maxLat}&minLat=${minLat}&maxLong=${maxLong}&minLong=${minLong}`);
  const json = await response.json();
  expect(json).toHaveProperty("features");
  expect(Array.isArray(json.features)).toBe(true);
  expect(json.features.length).toBe(0);
  expect(json).toHaveProperty("response_type");
  expect(json.response_type).toBe("success");
});

test("testing that invalid value returns an error response", async () => {
  const maxLat = "string";
  const minLat = 0;
  const maxLong = 0;
  const minLong = 0;
  const response = await fetch(`http://localhost:3232/geo-data?maxLat=${maxLat}&minLat=${minLat}&maxLong=${maxLong}&minLong=${minLong}`);
  const json = await response.json();
  expect(json).toHaveProperty("error");
  expect(json.error).toBe("For input string: \"string\"");
});

test("testing keyword search for 'homewood'", async () => {
  const toSearch = "homewood";
  const response = await fetch(`http://localhost:3232/keyword?toSearch=${toSearch}`);
  const json = await response.json();

  expect(json).toHaveProperty("features");
  expect(Array.isArray(json.features)).toBe(true);
  expect(json).toHaveProperty("response_type");
  expect(json.response_type).toBe("success");

  let hasHomewoodInAreaDescription = false;
  let hasMobileAsCity = false;

  json.features.forEach(feature => {
    expect(feature).toHaveProperty("geometry");
    expect(feature).toHaveProperty("properties");
    expect(feature.properties).toHaveProperty("area_description_data");
    for (const value of Object.values(feature.properties.area_description_data)) {
      if (typeof value === "string" && value.toLowerCase().includes(toSearch)) {
        hasHomewoodInAreaDescription = true;
        break;
      }
    }
    if (feature.properties.city.toLowerCase() === "mobile") {
      hasMobileAsCity = true;
    }
  });

  expect(hasHomewoodInAreaDescription).toBe(true);
  expect(hasMobileAsCity).toBe(true);
});

test("testing keyword search with a word not found", async () => {
  const toSearch = "jowet";
  const response = await fetch(`http://localhost:3232/keyword?toSearch=${toSearch}`);
  const json = await response.json();

  expect(json).toHaveProperty("features");
  expect(Array.isArray(json.features)).toBe(true);
  expect(json.features).toHaveLength(0);

  expect(json).toHaveProperty("response_type");
  expect(json.response_type).toBe("success");
});

// test("testing keyword search with incorrect query parameter", async () => {
//   const toSearchWord = "8";
//   const response = await fetch(`http://localhost:3232/keyword?toSearchWord=${toSearchWord}`);
//   const json = await response.json();

//   expect(json).toHaveProperty("error");
//   expect(json.error).toBe("Cannot invoke \"String.toLowerCase()\" because \"keyword\" is null");
// });

test("testing keyword search for 'brooks'", async () => {
  const toSearch = "brooks";
  const response = await fetch(`http://localhost:3232/keyword?toSearch=${toSearch}`);
  const json = await response.json();

  expect(json).toHaveProperty("features");
  expect(Array.isArray(json.features)).toBe(true);
  expect(json).toHaveProperty("response_type");
  expect(json.response_type).toBe("success");

  let hasBrooksInAreaDescription = false;
  let hasLosAngelesAsCity = false;

  json.features.forEach(feature => {
    expect(feature).toHaveProperty("geometry");
    expect(feature).toHaveProperty("properties");
    expect(feature.properties).toHaveProperty("area_description_data");
    for (const [key, value] of Object.entries(feature.properties.area_description_data)) {
      if (typeof value === "string" && value.toLowerCase().includes(toSearch)) {
        hasBrooksInAreaDescription = true;
        break;
      }
    }
    if (feature.properties.city.toLowerCase() === "los angeles") {
      hasLosAngelesAsCity = true;
    }
  });

  expect(hasBrooksInAreaDescription).toBe(true);
  expect(hasLosAngelesAsCity).toBe(true);
});

test("testing keyword search for 'coke_mock'", async () => {
  // const toSearch = "coke_mock";
  const toSearch = "coke";


  const response = await fetch(`http://localhost:3232/keyword?toSearch=${toSearch}`);
  const json = await response.json();

  expect(json).toHaveProperty("features");
  expect(Array.isArray(json.features)).toBe(true);
  expect(json).toHaveProperty("response_type");
  expect(json.response_type).toBe("success");

  let hasCokeInAreaDescription = false;
  let hasEastStLouisAsCity = false;

  json.features.forEach(feature => {
    expect(feature).toHaveProperty("geometry");
    expect(feature).toHaveProperty("properties");
    expect(feature.properties).toHaveProperty("area_description_data");
    for (const value of Object.values(feature.properties.area_description_data)) {
      if (typeof value === "string" && value.toLowerCase().includes(toSearch)) {
        hasCokeInAreaDescription = true;
        break;
      }
    }
    if (feature.properties.city.toLowerCase() === "east st. louis") {
      hasEastStLouisAsCity = true;
    }
  });

  expect(hasCokeInAreaDescription).toBe(true);
  expect(hasEastStLouisAsCity).toBe(true);
});



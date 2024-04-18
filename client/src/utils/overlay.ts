import { FeatureCollection, Feature } from "geojson";
import { FillLayer } from "react-map-gl";

const propertyName = "holc_grade";
export const geoLayer: FillLayer = {
  id: "geo_data",
  type: "fill",
  paint: {
    "fill-color": [
      "match",
      ["get", propertyName],
      "A",
      "#5bcc04",
      "B",
      "#04b8cc",
      "C",
      "#e9ed0e",
      "D",
      "#d11d1d",
      "#ccc",
    ],
    "fill-opacity": 0.2,
  },
};

export const highlightLayer: FillLayer = {
  id: "search_results",
  type: "fill",
  paint: {
    "fill-color": "#A739EA",
    "fill-opacity": 0.4,
  },
};

function isFeatureCollection(json: any): json is FeatureCollection {
  return json.type === "FeatureCollection";
}

export function overlayData(json : any): GeoJSON.FeatureCollection | undefined {
  return isFeatureCollection(json) ? json : undefined;
}

export function highlightOverlay(features: Feature[]): FeatureCollection {
  return {
    type: "FeatureCollection",
    features: features,
  };
}
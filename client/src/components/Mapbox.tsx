import "mapbox-gl/dist/mapbox-gl.css";
import { useEffect, useState } from "react";
import Map, {
  Layer,
  MapLayerMouseEvent,
  Source,
  ViewStateChangeEvent,
} from "react-map-gl";
import {
  geoLayer,
  overlayData,
  highlightOverlay,
  highlightLayer,
} from "../utils/overlay";
import { ControlledInput } from "./auth/ControlledInput";
import { addPins, getPins, clearPins } from "../utils/api";
import { getLoginCookie } from "../utils/cookie";

export default function Mapbox() {
  const [viewState, setViewState] = useState({
    longitude: ProvidenceLatLong.long,
    latitude: ProvidenceLatLong.lat,
    zoom: initialZoom,
  });

  const [overlay, setOverlay] = useState<GeoJSON.FeatureCollection | undefined>(
    undefined
  );

  useEffect(() => {
    const data = fetchMapData("2000", "-2000", "3000", "-3000")
    setOverlay(overlayData(data));
  }, []);

  /**
   * Fetches the data to populate the map using an API call
   * @param maxLat the maximum latitude 
   * @param minLat the minimum latitude
   * @param maxLong the maximum longitude
   * @param minLong the minimum longitude
   * @returns a json file on success
   */
  const fetchMapData = async (
    maxLat: string, minLat: string, maxLong: string, minLong: string
  ): Promise<GeoJSON.FeatureCollection | undefined> => {
    try {
      const res = await fetch(`http://localhost:3232/geo-data?maxLat=${maxLat}&minLat=${minLat}&maxLong=${maxLong}&minLong=${minLong}`);
      const json = await res.json();
      return json;
    } catch (error) {
      console.error("Error fetching map data:", error);
      return undefined;
    }
  };


  const [highlight, setHighlight] = useState<
    GeoJSON.FeatureCollection | undefined
  >(undefined);

  useEffect(() => {
    setHighlight(highlight);
  }, [highlight]);

  /**
   * Fetches geo data from the API based on a search term.
   * @param args - A string containing the keyword to search by.
   * @returns A promise that resolves to the GeoJSON feature collection containing matching features.
   */
  const fetchGeoData = async (
    args: string
  ): Promise<GeoJSON.FeatureCollection | undefined> => {
    const argsArray = args.split(" ");
    try {
      if (argsArray.length !== 1) {
        console.error("Please enter one word to search.");
      }
      const res = await fetch(`http://localhost:3232/keyword?toSearch=${args}`);
      const json = await res.json();
      return json;
    } catch (error) {
      console.error("Error fetching geo data:", error);
      return undefined;
    }
  };

  /**
   * Handles the submission of a search command.
   * @param commandString - The command string entered by the user
   */
  async function handleSearchSubmit(commandString: string) {
    if (commandString === "clear") {
      return;
    } else {
      const result = await fetchGeoData(commandString);
      if (result) {
        setHighlight(highlightOverlay(result.features));
      }
    }
    setCommandString("");
  }

  const [commandString, setCommandString] = useState<string>("");
  const inputRef = useRef<HTMLInputElement>(null);
  const [clickedPoints, setClickedPoints] = useState<LatLong[]>([]);

  /**
   * Event handler for when a user clicks on the map.
   * @param e - The mouse event object
   */
  function onMapClick(e: MapLayerMouseEvent) {
    const newClickedPoint = { lat: e.lngLat.lat, long: e.lngLat.lng };
    setClickedPoints([...clickedPoints, newClickedPoint]);
    addPins(e.lngLat.lat, e.lngLat.lng, clickedPoints.length);
  }

  /**
   * Clears all pins from the map graphically and logically.
   */
  function clearPin() {
    setClickedPoints([]);
    clearPins();
  }

  useEffect(() => {
    setPointsOnLogin();
  }, []);

  async function setPointsOnLogin() {
    if (getLoginCookie() == undefined) {
      setClickedPoints([]);
    } else {
      const allPins = await getPins();
      console.log(allPins.data)
      if (allPins.data != null) {
        const clickedPointsArray: LatLong[] = [];
        for (let i = 0; i < allPins.data.length; i++) {
          const pin = allPins.data[i];
          const newClickedPoint = {
            lat: pin.latitude,
            long: pin.longitude
          };
          clickedPointsArray.push(newClickedPoint);
        }
        setClickedPoints(clickedPointsArray);
      }
    }
  }

  return (
    <div className="map">
      <div className="repl-input" aria-label="Command Prompt Area">
        <fieldset>
          <legend>Enter a command:</legend>
          <ControlledInput
            ref={inputRef}
            value={commandString}
            setValue={setCommandString}
            ariaLabel="Command Input Field"
          />
        </fieldset>
        <button
          aria-label="Submit Command Button"
          onClick={() =>
            (async () => await handleSearchSubmit(commandString))()
          }
        >
          Submit
        </button>
      </div>
      <button onClick={clearPin}>Clear Pins</button>
      <Map
        mapboxAccessToken={MAPBOX_API_KEY}
        {...viewState}
        style={{ width: window.innerWidth, height: window.innerHeight }}
        mapStyle={"mapbox://styles/mapbox/streets-v12"}
        onMove={(ev: ViewStateChangeEvent) => setViewState(ev.viewState)}
        onClick={(ev: MapLayerMouseEvent) => onMapClick(ev)}
      >
        <Source id="geo_data" type="geojson" data={overlay}>
          <Layer id={geoLayer.id} type={geoLayer.type} paint={geoLayer.paint} />
        </Source>

        <Source id="search_data" type="geojson" data={highlight}>
          <Layer
            id={highlightLayer.id}
            type={highlightLayer.type}
            paint={highlightLayer.paint}
          />
        </Source>

        {clickedPoints.map((point, index) => (
          <Marker
            key={index}
            longitude={point.long}
            latitude={point.lat}
            anchor="bottom"
          ></Marker>
        ))}
      </Map>
    </div>
  );
}

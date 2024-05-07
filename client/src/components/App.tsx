import { initializeApp } from "firebase/app";
import "../styles/home.css";
import "../styles/style.css";
import Mapbox from "./Mapbox";
import AuthRoute from "./auth/AuthRoute";
import { Helmet } from "react-helmet";
import { Home } from "./Home";
import { Modes, ScreenMap } from "./mode";
import { useState } from "react";
import { Results } from "./Results";

const firebaseConfig = {
  apiKey: "AIzaSyBGcaZsb_OVHXf9tSX7TpMfbjBkKceS2zI",
  authDomain: "maps-mpoisso1.firebaseapp.com",
  projectId: "maps-mpoisso1",
  storageBucket: "maps-mpoisso1.appspot.com",
  messagingSenderId: "134698299927",
  appId: "1:134698299927:web:04adc951273c82d7b9e252",
};

initializeApp(firebaseConfig);

/**
 * This is the highest level component!
 */
function App() {
  const [mode, setMode] = useState<Modes>(Modes.home);
  const CurrentScreen: (props: any) => React.JSX.Element = ScreenMap.get(mode)!;
  return <CurrentScreen mode={mode} setMode={setMode} />;
}

export default App;

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
  return (
    <div className="home-container">
      <Helmet>
        <title>seatseeker</title>
        <meta property="og:title" content="exported project" />
      </Helmet>
      <div className="home-header">
        <h1 className="home-seat-seeker">SeatSeeker</h1>
        <div className="home-container01">
          <div className="home-container02">
            <div className="home-container03"></div>
            <div className="home-container04">
              <div className="home-container05"></div>
              <span className="home-compare-ticket">
                compare ticket prices with a single click
              </span>
            </div>
          </div>
          <div className="home-container06"></div>
        </div>
        <div className="home-container07">
          <div className="home-container08">
            <button className="home-sign-in button">Sign In</button>
          </div>
          <div className="home-container12">
            <span className="home-to-save">to save events to favorites</span>
          </div>
        </div>
      </div>
      <CurrentScreen mode={mode} setMode={setMode} query={"boston celtics"} />
    </div>
  );
}

export default App;

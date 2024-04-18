import { initializeApp } from "firebase/app";
import "../styles/App.css";
import Mapbox from "./Mapbox";
import AuthRoute from "./auth/AuthRoute";

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
  return (
    <div className="App">
      <AuthRoute gatedContent={<Mapbox />} />
      {/* <input type="text" placeholder="Search..." /> */}
    </div>
  );
}

export default App;

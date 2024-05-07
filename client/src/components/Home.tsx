import "../styles/home.css";
import "../styles/style.css";
import { Helmet } from "react-helmet";
import { Modes } from "./mode";
import { Dispatch, SetStateAction, useState, useEffect } from "react";
interface HomeProps {
  mode: Modes;
  setMode: Dispatch<SetStateAction<Modes>>;
}
export function Home(props: HomeProps) {
  const [commandString, setCommandString] = useState<string>("");
  const handleSubmit = async () => {
    if (commandString === "") {
      return;
    }
    props.setMode(Modes.results);
  };
  const handleKeyPress = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      handleSubmit();
    }
  };
  return (
    <div className="home-container">
      <div className="home-container13">
        <div className="home-container14">
          <div className="home-container15">
            <div className="home-container16"></div>
            <h1 className="home-find-tickets">Find Tickets</h1>
          </div>
          <div className="home-container17">
            <input
              value={commandString}
              onChange={(e) => setCommandString(e.target.value)}
              type="text"
              placeholder="enter keyword here"
              className="home-keyword-input input"
              onKeyDown={handleKeyPress}
            />
            <div className="home-container18"></div>
            <button className="home-button button" onClick={handleSubmit}>
              Search
            </button>
          </div>
        </div>
      </div>
      <div className="home-container19">
        <div className="home-container20">
          <h1 className="looking">Looking to narrow your search?</h1>
        </div>
        <div className="home-container21">
          <input
            type="text"
            placeholder="city"
            className="home-textinput input"
          />
        </div>
        <div className="home-container22">
          <input
            type="text"
            placeholder="date"
            className="home-textinput1 input"
          />
        </div>
        <div className="home-container23">
          <input
            type="text"
            placeholder="time"
            className="home-textinput2 input"
          />
        </div>
        <button className="home-button button">Search</button>
      </div>
    </div>
  );
}

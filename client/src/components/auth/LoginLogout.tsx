import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";
import React, { SetStateAction } from "react";
import { addLoginCookie, removeLoginCookie } from "../../utils/cookie";
import { Modes } from "../mode";

export interface ILoginPageProps {
  loggedIn: boolean;
  setLogin: React.Dispatch<React.SetStateAction<boolean>>;
  mode: Modes;
  setMode: React.Dispatch<SetStateAction<Modes>>;
}

const Login: React.FunctionComponent<ILoginPageProps> = (props) => {
  const auth = getAuth();

  const signInWithGoogle = async () => {
    try {
      const response = await signInWithPopup(auth, new GoogleAuthProvider());
      const userEmail = response.user.email || "";

      // Check if the email ends with the allowed domain
      if (userEmail.endsWith("@brown.edu")) {
        console.log(response.user.uid);
        // add unique user id as a cookie to the browser.
        addLoginCookie(response.user.uid);
        props.setLogin(true);
      } else {
        // User is not allowed, sign them out and show a message
        await auth.signOut();
        console.log("User not allowed. Signed out.");
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="home-container07">
      <div className="home-container08">
        <button
          className="home-sign-in button" data-testid="sign-in"
          onClick={() => signInWithGoogle()}
        >
          Sign in
        </button>
      </div>
      <div className="home-container12">
        <span className="home-to-save">
          Sign in to save events to your account
        </span>
      </div>
    </div>

  );
};

const Logout: React.FunctionComponent<ILoginPageProps> = (props) => {
  const signOut = () => {
    removeLoginCookie();
    props.setLogin(false);
    props.setMode(Modes.home)
  };

  return (

    <div className="home-container07">
      <div className="home-container08">
        <button className="home-sign-in button" data-testid="sign-out" onClick={() => signOut()}>
          Sign Out
        </button>
      </div>
      <div className="home-container12">
        <span className="home-to-save">
          <button className="home-saved button" data-testid="save-events" onClick={() => props.setMode(Modes.saved)}>
            Saved Events
          </button>
        </span>
      </div>
    </div>

  );
};

const LoginLogout: React.FunctionComponent<ILoginPageProps> = (props) => {
  return <>{!props.loggedIn ? <Login {...props} /> : <Logout {...props} />}</>;
};

export default LoginLogout;

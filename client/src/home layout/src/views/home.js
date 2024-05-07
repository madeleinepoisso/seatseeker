import React from 'react'

import { Helmet } from 'react-helmet'

import './home.css'

const Home = (props) => {
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
            <div className="home-container09">
              <div className="home-container10"></div>
              <button type="button" className="home-sign-in button">
                Sign In
              </button>
            </div>
            <div className="home-container11"></div>
          </div>
          <div className="home-horizontal-bar"></div>
          <div className="home-container12">
            <span className="home-to-save">to save events to favorites</span>
          </div>
        </div>
      </div>
      <div className="home-container13">
        <div className="home-container14">
          <div className="home-container15">
            <div className="home-container16"></div>
            <h1 className="home-find-tickets">Find Tickets</h1>
          </div>
          <div className="home-container17">
            <input
              type="text"
              placeholder="enter keyword here"
              className="home-keyword-input input"
            />
            <div className="home-container18"></div>
            <button type="button" className="home-button button">
              Search
            </button>
          </div>
        </div>
      </div>
      <div className="home-container19">
        <div className="home-container20">
          <h1 className="home-text">Looking to narrow your search?</h1>
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
        <button type="button" className="home-button1 button">
          Search
        </button>
      </div>
    </div>
  )
}

export default Home

import React, { useState, useEffect } from "react";

import Helmet from "../components/Helmet/Helmet.js";

import "../styles/hero-section.css";



import "../styles/home.css";



import products from "../assets/fake-data/products.js";
import restaurants from "../assets/fake-data/restaurants.js";



import IncomingOrders from "../components/UI/incoming-order/IncomingOrders.jsx";


const RestaurantOwnerHome = () => {

  return (
    <Helmet title="Home">

      <section>
        <IncomingOrders restaurants={restaurants} />
      </section>
    </Helmet>
  );
};

export default RestaurantOwnerHome;

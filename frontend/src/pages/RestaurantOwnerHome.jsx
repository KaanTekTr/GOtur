import React, { useState, useEffect } from "react";

import Helmet from "../components/Helmet/Helmet.js";

import "../styles/hero-section.css";



import "../styles/home.css";



import products from "../assets/fake-data/products.js";
import restaurants from "../assets/fake-data/restaurants.js";



import IncomingOrders from "../components/UI/incoming-order/IncomingOrders.jsx";
import { getOrdersThunk } from "../store/user/orderSlice.js";
import { useDispatch, useSelector } from "react-redux";
import { getUserThunk } from "../store/authSlice.js";


const RestaurantOwnerHome = () => {

  const userId = useSelector(state => state.auth.userId);
  const dispatch = useDispatch();

  useEffect(() => {
    if (userId !== 0) {
      dispatch(getUserThunk({authType: "restaurantOwner", userId}));
    }
  })

  return (
    <Helmet title="Home">

      <section>
        <IncomingOrders restaurants={restaurants} />
      </section>
    </Helmet>
  );
};

export default RestaurantOwnerHome;

import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";

import Home from "../pages/Home";
import AllFoods from "../pages/AllFoods";
import FoodDetails from "../pages/FoodDetails";
import Cart from "../pages/Cart";
import Checkout from "../pages/Checkout";
import Contact from "../pages/Contact";
import Login from "../pages/Login";
import Register from "../pages/Register";
import AllRestaurants from "../pages/AllRestaurants";
import RestaurantDetails from "../pages/RestaurantDetails"
import PastOrders from "../pages/PastOrders";
import Groups from "../pages/Groups";
import GroupDetails from "../pages/GroupDetails";
import RestaurantOwnerHome from "../pages/RestaurantOwnerHome";
import Friends from "../pages/Friends";
import RestaurantOwnerBalance from "../pages/RestaurantOwnerBalance";
import RestaurantOwnerProfile from "../pages/RestaurantOwnerProfile";
import RestaurantInfoPage from "../pages/RestaurantInfoPage";
import RestaurantMenu from "../pages/RestaurantMenu";
import RestaurantPastOrders from "../pages/RestaurantPastOrders";






const Routers = () => {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/Login" />} />
      <Route path="/home" element={<Home />} />
      <Route path="/foods" element={<AllFoods />} />
      <Route path="/foods/:id" element={<FoodDetails />} />
      <Route path="/restaurants" element={<AllRestaurants />} />
      <Route path="/restaurants/:id" element={<RestaurantDetails />} />
      <Route path="/pastOrders" element={<PastOrders />} />
      <Route path="/groups" element={<Groups />} />
      <Route path="/friends" element={<Friends />} />
      <Route path="/groups/:id" element={<GroupDetails />} />
      <Route path="/cart" element={<Cart />} />
      <Route path="/checkout" element={<Checkout />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/contact" element={<Contact />} />
      <Route path="/restaurantOwnerHome" element={<RestaurantOwnerHome />} />
      <Route path="/balance" element={<RestaurantOwnerBalance />} />
      <Route path="/profile" element={<RestaurantOwnerProfile />} />
      <Route path="/restaurant-info" element={<RestaurantInfoPage />} />
      <Route path="/restaurant-menu/:id" element={<RestaurantMenu />} />
      <Route path="/restaurantPastOrders" element={<RestaurantPastOrders />} />




      


    </Routes>
  );
};

export default Routers;

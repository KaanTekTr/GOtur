import { createSlice } from "@reduxjs/toolkit";

import restaurants from "../../assets/fake-data/restaurants";
import products from "../../assets/fake-data/products";

const initialState = {
    restaurants: restaurants,
    products: products
};

const restaurantSlice = createSlice({
  name: "restaurant",
  initialState,

  reducers: {
    // ACTIONS
    getRestaurants(state, action) {
        console.log("get restaurants");
    },
    getProducts(state, action) {
        console.log("get products");
    }
  },
});

export const restaurantActions = restaurantSlice.actions;
export default restaurantSlice;

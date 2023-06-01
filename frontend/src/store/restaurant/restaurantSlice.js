import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

import restaurants from "../../assets/fake-data/restaurants";
import products from "../../assets/fake-data/products";
import { getAllFoodCategory, getAllRestaurants } from "../../lib/api/unsplashService";

export const getRestaurantsThunk = createAsyncThunk('user/getRestaurants', 
  async () => {
    try {
      const  response = await getAllRestaurants();
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const getAllFoodCategoryThunk = createAsyncThunk('user/getFoodCategories', 
  async (data, thunkAPI) => {
    try {
      const  response = await getAllFoodCategory(data.restaurant_id);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

const initialState = {
    restaurants: [],
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
  extraReducers: (builder) => {
    builder
      .addCase(getRestaurantsThunk.fulfilled, (state, action) => {
        state.restaurants = action.payload;
        console.log(action.payload);
      })
      .addCase(getAllFoodCategoryThunk.fulfilled, (state, action) => {
        console.log(action.payload);
      })
  },
});

export const restaurantActions = restaurantSlice.actions;
export default restaurantSlice;

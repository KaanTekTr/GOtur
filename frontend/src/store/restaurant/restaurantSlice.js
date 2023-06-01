import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

import { addNewFood, addNewMenuCategory, getAllFoodCategory, getAllFoodsOfRest, getAllMenuCategories, getAllRestaurants, getRestaurantsOfOwner } from "../../lib/api/unsplashService";

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

export const getAllMenuCategoryThunk = createAsyncThunk('user/getMenuCategories', 
  async (data, thunkAPI) => {
    try {
      const  response = await getAllMenuCategories(data.restaurant_id);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const getAllFoodRestThunk = createAsyncThunk('user/getFoodRest', 
  async (data, thunkAPI) => {
    try {
      const  response = await getAllFoodsOfRest(data.restaurant_id);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const getRestOfOwnerThunk = createAsyncThunk('user/getOwnRest', 
  async (data, thunkAPI) => {
    try {
      const  response = await getRestaurantsOfOwner(data.userId);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const addNewFoodThunk = createAsyncThunk('rest/addFood', 
  async (data, thunkAPI) => {
    try {
      const  response = await addNewFood(data.food);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const addNewMenuCatThunk = createAsyncThunk('rest/addMenuCat', 
  async (data, thunkAPI) => {
    try {
      const  response = await addNewMenuCategory(data.category);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

const initialState = {
    restaurants: [],
    lastVisited: localStorage.getItem("lastVisited"),
    foodCategories: [],
    menuCategories: [],
    products: [],
    myRestaurant: {
      info: JSON.parse(localStorage.getItem("info"))
    }
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
    },
    visitRest(state, action) {
      state.lastVisited = action.payload;
      localStorage.setItem("lastVisited", action.payload);
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(getRestaurantsThunk.fulfilled, (state, action) => {
        state.restaurants = action.payload;
        console.log(action.payload);
      })
      .addCase(getAllFoodCategoryThunk.fulfilled, (state, action) => {
        state.foodCategories = action.payload;
        console.log(action.payload);
      })
      .addCase(getAllFoodRestThunk.fulfilled, (state, action) => {
        state.products = action.payload;
        console.log(action.payload);
      })
      .addCase(getAllMenuCategoryThunk.fulfilled, (state, action) => {
        state.menuCategories = action.payload;
        console.log(action.payload);
      })
      .addCase(getRestOfOwnerThunk.fulfilled, (state, action) => {
        state.myRestaurant.info = action.payload[0];
        localStorage.setItem("info", JSON.stringify(action.payload[0]));
        console.log(action.payload);
      })
      .addCase(addNewFoodThunk.fulfilled, (state, action) => {
        console.log(action.payload);
      })
      .addCase(addNewMenuCatThunk.fulfilled, (state, action) => {
        console.log(action.payload);
      })
  },
});

export const restaurantActions = restaurantSlice.actions;
export default restaurantSlice;

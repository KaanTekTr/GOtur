import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

import { addFavRestaurants, addNewFood, addNewMenuCategory, addNewRestaurant, deleteFood, editRestaurant, getAllFoodCategory, getAllFoodsOfRest, getAllMenuCategories, getAllRestaurants, getRestaurantsOfOwner } from "../../lib/api/unsplashService";

export const addNewRestaurantThunk = createAsyncThunk('rest/addRestaurant', 
  async (data, thunkAPI) => {
    try {
      const  response = await addNewRestaurant(data.id, data.restaurant);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const editRestaurantThunk = createAsyncThunk('rest/editRestaurant', 
  async (data, thunkAPI) => {
    try {
      const  response = await editRestaurant(data.id, data.restaurant);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

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
export const deleteFoodThunk = createAsyncThunk('rest/deleteFood', 
  async (data, thunkAPI) => {
    try {
      const  response = await deleteFood(data.foodId);
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

export const addFavRestaurantsThunk = createAsyncThunk('user/addFavRestaurants', 
  async (data) => {
    try {
      const  response = await addFavRestaurants(data.userId, data.rest_id);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

const initialState = () => {
  let info = {};
  try {
    info = JSON.parse(localStorage.getItem("info") )
  } catch (error) {
    console.log(error);
  }
  return {
    restaurants: [],
    lastVisited: localStorage.getItem("lastVisited"),
    foodCategories: [],
    menuCategories: [],
    products: [],
    myRestaurant: {
      info,
    }
}};

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
      .addCase(addNewRestaurantThunk.fulfilled, (state, action) => {
        console.log(action.payload);
      })
      .addCase(editRestaurantThunk.fulfilled, (state, action) => {
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
      .addCase(deleteFoodThunk.fulfilled, (state, action) => {
        console.log(action.payload);
      })
      .addCase(addNewMenuCatThunk.fulfilled, (state, action) => {
        console.log(action.payload);
      })
  },
});

export const restaurantActions = restaurantSlice.actions;
export default restaurantSlice;

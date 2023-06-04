import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { getDiscountsOfCustomer } from "../../lib/api/unsplashService";

export const getDiscountsThunk = createAsyncThunk('user/getDiscounts', 
  async (data, thunkAPI) => {
    try {
      const  response = await getDiscountsOfCustomer(data.userId);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

const initialState = {
    discountCoupons:[]
};

const discountSlice = createSlice({
  name: "discountCoupon",
  initialState,

  reducers: {
    getDiscountsOfCustomer(state, action) {
        const { id } = action.payload;
        console.log("get groups", id);
    },

    // GROUP CART ACTIONS

  },
  extraReducers: (builder) => {
    builder
      .addCase(getDiscountsThunk.fulfilled, (state, action) => {
        state.discountCoupons = action.payload;
        console.log(action.payload);
      })
  },
});


export default discountSlice;
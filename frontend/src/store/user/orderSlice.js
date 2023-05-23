import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    pastOrders: [],
};

const orderSlice = createSlice({
  name: "order",
  initialState,

  reducers: {
    // ACTIONS
    getPastOrders(state, action) {
        console.log("get restaurants");
    }
  },
});

export const orderActions = orderSlice.actions;
export default orderSlice;

import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    pastOrders: [],
    currentCart: 0,
};

const orderSlice = createSlice({
  name: "order",
  initialState,

  reducers: {
    // ACTIONS
    getPastOrders(state, action) {
        console.log("get restaurants");
    },
    updateCurrentCart(state, action) {
      const {id} = action.payload;
      state.currentCart = id;
    }
  },
});

export const orderActions = orderSlice.actions;
export default orderSlice;

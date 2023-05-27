import { configureStore } from "@reduxjs/toolkit";
import cartSlice from "./shopping-cart/cartSlice";
import cartUiSlice from "./shopping-cart/cartUiSlice";
import authSlice from "./authSlice";
import restaurantSlice from "./restaurant/restaurantSlice";
import addressSlice from "./user/adressSlice";
import orderSlice from "./user/orderSlice";
import groupsSlice from "./group/groupSlice";

const store = configureStore({
  reducer: {
    cart: cartSlice.reducer,
    cartUi: cartUiSlice.reducer,
    groups: groupsSlice.reducer,
    auth: authSlice.reducer,
    restaurant: restaurantSlice.reducer,
    address: addressSlice.reducer,
    order: orderSlice.reducer,
  },
});

export default store;

import { createSlice } from "@reduxjs/toolkit";

const items =
  localStorage.getItem("groupCartItems") !== null
    ? JSON.parse(localStorage.getItem("groupCartItems"))
    : [];

const totalAmount =
  localStorage.getItem("groupTotalAmount") !== null
    ? JSON.parse(localStorage.getItem("groupTotalAmount"))
    : 0;

const totalQuantity =
  localStorage.getItem("groupTotalQuantity") !== null
    ? JSON.parse(localStorage.getItem("groupTotalQuantity"))
    : 0;

const setItemFunc = (item, totalAmount, totalQuantity) => {
  localStorage.setItem("groupCartItems", JSON.stringify(item));
  localStorage.setItem("groupTotalAmount", JSON.stringify(totalAmount));
  localStorage.setItem("groupTotalQuantity", JSON.stringify(totalQuantity));
};

const initialState = {
    groupCartItems: items,
    groupTotalAmount: totalAmount,
    groupTotalQuantity: totalQuantity,
};

const groupCartSlice = createSlice({
  name: "groupCart",
  initialState,

  reducers: {
    // =========== add item ============
    addItem(state, action) {
      const newItem = action.payload;
      const existingItem = state.groupCartItems.find(
        (item) => item.id === newItem.id
      );
      state.groupTotalQuantity++;

      if (!existingItem) {
        // ===== note: if you use just redux you should not mute state array instead of clone the state array, but if you use redux toolkit that will not a problem because redux toolkit clone the array behind the scene

        state.groupCartItems.push({
          id: newItem.id,
          title: newItem.title,
          image01: newItem.image01,
          price: newItem.price,
          quantity: 1,
          totalPrice: newItem.price,
        });
      } else {
        existingItem.quantity++;
        existingItem.totalPrice =
          Number(existingItem.totalPrice) + Number(newItem.price);
      }

      state.groupTotalAmount = state.groupCartItems.reduce(
        (total, item) => total + Number(item.price) * Number(item.quantity),

        0
      );

      setItemFunc(
        state.groupCartItems.map((item) => item),
        state.groupTotalAmount,
        state.groupTotalQuantity
      );
    },

    // ========= remove item ========

    removeItem(state, action) {
      const id = action.payload;
      const existingItem = state.groupCartItems.find((item) => item.id === id);
      state.groupTotalQuantity--;

      if (existingItem.quantity === 1) {
        state.groupCartItems = state.groupCartItems.filter((item) => item.id !== id);
      } else {
        existingItem.quantity--;
        existingItem.totalPrice =
          Number(existingItem.totalPrice) - Number(existingItem.price);
      }

      state.groupTotalAmount = state.groupCartItems.reduce(
        (total, item) => total + Number(item.price) * Number(item.quantity),
        0
      );

      setItemFunc(
        state.groupCartItems.map((item) => item),
        state.groupTotalAmount,
        state.groupTotalQuantity
      );
    },

    //============ delete item ===========

    deleteItem(state, action) {
      const id = action.payload;
      const existingItem = state.groupCartItems.find((item) => item.id === id);

      if (existingItem) {
        state.groupCartItems = state.groupCartItems.filter((item) => item.id !== id);
        state.groupTotalQuantity = state.groupTotalQuantity - existingItem.quantity;
      }

      state.groupTotalAmount = state.groupCartItems.reduce(
        (total, item) => total + Number(item.price) * Number(item.quantity),
        0
      );
      setItemFunc(
        state.groupCartItems.map((item) => item),
        state.groupTotalAmount,
        state.groupTotalQuantity
      );
    },
  },
});

export const groupCartActions = groupCartSlice.actions;
export default groupCartSlice;

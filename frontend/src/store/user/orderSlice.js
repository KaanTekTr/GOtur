import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    pastOrders: [
      {
        id: 1,
        items: [
          {
            id: '04',
            title: 'Maxican Green Wave',
            image01: '/static/media/product_4.1.3c8ecc492220a3922731.jpg',
            price: 110,
            quantity: 1,
            totalPrice: 110
          },
          {
            id: '08',
            title: 'Thin Cheese Pizza',
            image01: '/static/media/product_3.2.ebcb16b50e4ef0060a5e.jpg',
            price: 110,
            quantity: 1,
            totalPrice: 110
          },
          {
            id: '05',
            title: 'Cheese Burger',
            image01: '/static/media/product_04.f7c5294d0481fb12cc4c.jpg',
            price: 24,
            quantity: 1,
            totalPrice: 24
          }
        ],
        totalQuantity: 3,
        totalAmount: 244,
        date: "29 May 2023",
        comment: "This was very good!"
      },
      {
        id: 2,
        items: [
          {
            id: '04',
            title: 'Maxican Green Wave',
            image01: '/static/media/product_4.1.3c8ecc492220a3922731.jpg',
            price: 110,
            quantity: 1,
            totalPrice: 110
          },
          {
            id: '08',
            title: 'Thin Cheese Pizza',
            image01: '/static/media/product_3.2.ebcb16b50e4ef0060a5e.jpg',
            price: 110,
            quantity: 1,
            totalPrice: 110
          },
          {
            id: '05',
            title: 'Cheese Burger',
            image01: '/static/media/product_04.f7c5294d0481fb12cc4c.jpg',
            price: 24,
            quantity: 1,
            totalPrice: 24
          }
        ],
        totalQuantity: 3,
        totalAmount: 244,
        date: "29 May 2023",
        comment: ""
      }
    ],
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

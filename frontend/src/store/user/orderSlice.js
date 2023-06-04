import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { addFoodToGroupPurchase, addFoodToSinglePurchase, completeGroupPurchase, completeSinglePurchase, deleteFoodFromGroupPurchase, deleteFoodFromSinglePurchase, getOldPurchases, getOldPurchasesFoods, getProductUnpaidGroupPurchase, getProductUnpaidSinglePurchase, getUnpaidGroupPurchase, getUnpaidSinglePurchase, getRestaurantOrders, setPurchaseDeparted } from "../../lib/api/unsplashService";

export const getProductsUnpaidSinglePurchaseThunk = createAsyncThunk('purchase/getProductsUnpaidSingle', 
  async (data, thunkAPI) => {
    try {
      const  response = await getProductUnpaidSinglePurchase(data.userId);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const getOrdersThunk = createAsyncThunk('user/getOrders', 
  async (data, thunkAPI) => {
    try {
        console.log(data.userId)
      const response = await getRestaurantOrders(data.userId);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const getUnpaidSinglePurchaseThunk = createAsyncThunk('purchase/getUnpaidSingle', 
  async (data, thunkAPI) => {
    try {
      const  response = await getUnpaidSinglePurchase(data.userId);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const getProductsUnpaidGroupPurchaseThunk = createAsyncThunk('purchase/getProductsUnpaidGroup', 
  async (data, thunkAPI) => {
    try {
      const  response = await getProductUnpaidGroupPurchase(data.userId);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const getUnpaidGroupPurchaseThunk = createAsyncThunk('purchase/getUnpaidGroup', 
  async (data, thunkAPI) => {
    try {
      const  response = await getUnpaidGroupPurchase(data.userId);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const addFoodToSinglePurchaseThunk = createAsyncThunk('purchase/addFoodSingle', 
  async (data, thunkAPI) => {
    try {
      const response = await addFoodToSinglePurchase(data.food, data.userId);
      console.log(response);
      if (response?.status === 400) {
        data.setInfo("You have food from another restorant in your cart already!");
        data.setVisible(v => !v);
        setTimeout(() => {
          data.setVisible(true);
        }, 100)
      }
      return response.data;
    } catch (error) {
      console.log(error);
      data.setInfo(error.response.data);
      data.setVisible(v => !v);
      setTimeout(() => {
        data.setVisible(true);
      }, 100)
    }
  }
);

export const addFoodToGroupPurchaseThunk = createAsyncThunk('purchase/addFoodGroup', 
  async (data, thunkAPI) => {
    try {
      const  response = await addFoodToGroupPurchase(data.food, data.userId);
      if (response?.status === 400) {
        data.setInfo("You have food from another restorant in group cart already!");
        data.setVisible(v => !v);
        setTimeout(() => {
          data.setVisible(true);
        }, 100)
      }
      return response.data;
    } catch (error) {
      console.log(error);
      data.setInfo(error.response.data);
      data.setVisible(v => !v);
      setTimeout(() => {
        data.setVisible(true);
      }, 100)
    }
  }
);

export const deleteFoodFromSinglePurchaseThunk = createAsyncThunk('purchase/deleteFoodSingle', 
  async (data, thunkAPI) => {
    try {
      const  response = await deleteFoodFromSinglePurchase(data.customer_id, data.food_id, data.food_order);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const deleteFoodFromGroupPurchaseThunk = createAsyncThunk('purchase/deleteFoodGroup', 
  async (data, thunkAPI) => {
    try {
      const  response = await deleteFoodFromGroupPurchase(data.group_id, data.food_id, data.food_order);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const completeSinglePurchaseThunk = createAsyncThunk('purchase/completeSinglePurchase', 
  async (data, thunkAPI) => {
    try {
      const  response = await completeSinglePurchase(data.purchase_id, data.address_id, data.note, data.coupon_id);
      return response.data;
    } catch (error) {
      console.log(error);
      data.setInfo(error.response.data);
      data.setVisible(v => !v);
      setTimeout(() => {
        data.setVisible(true);
      }, 100)
    }
  }
);

export const completeGroupPurchaseThunk = createAsyncThunk('purchase/completeGroupPurchase', 
  async (data, thunkAPI) => {
    try {
      const  response = await completeGroupPurchase(data.purchase_id, data.address_id, data.note, data.coupon_id);
      return response.data;
    } catch (error) {
      console.log(error);
      data.setInfo(error.response.data);
      data.setVisible(v => !v);
      setTimeout(() => {
        data.setVisible(true);
      }, 100)
    }
  }
);

export const getOldPurchasesThunk = createAsyncThunk('purchase/getOldPurchases', 
  async (data, thunkAPI) => {
    try {
      const  response = await getOldPurchases(data.userId);

      return { pastOrders: response.data, dispatch: data.dispatch};
    } catch (error) {
      console.log(error);
    }
  }
);

export const getOldPurchasesFoodsThunk = createAsyncThunk('purchase/getOldPurchasesFoods', 
  async (data, thunkAPI) => {
    try {
      const  response = await getOldPurchasesFoods(data.purchase_id);
      return {products: response.data, purchase_id: data.purchase_id};
    } catch (error) {
      console.log(error);
    }
  }
);

export const setPurchaseDepartedThunk = createAsyncThunk('purchase/setPurchaseDeparted', 
  async (data, thunkAPI) => {
    try {
      const response = await setPurchaseDeparted(data.purchase_id);
      return response.data
    } catch (error) {
      console.log(error);
    }
  }
);

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
    unpaidSinglePurchase: {},
    unpaidGroupPurchase: {},
    paidWaitingPurchases: [],
    itemAdded: 0
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
    },
    updateItemAdded(state, action) {
      state.itemAdded = 0;
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(getUnpaidGroupPurchaseThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.unpaidGroupPurchase = action.payload;
        localStorage.setItem("groupCart", JSON.stringify(state.unpaidGroupPurchase));
      })
      .addCase(getProductsUnpaidGroupPurchaseThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.unpaidGroupPurchase = {...state.unpaidGroupPurchase, products: action.payload};
        localStorage.setItem("groupCart", JSON.stringify(state.unpaidGroupPurchase));
      })
      .addCase(getUnpaidSinglePurchaseThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.unpaidSinglePurchase = action.payload;
        localStorage.setItem("singleCart", JSON.stringify(state.unpaidSinglePurchase));
      })
      .addCase(getProductsUnpaidSinglePurchaseThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.unpaidSinglePurchase = {...state.unpaidSinglePurchase, products: action.payload};
        localStorage.setItem("singleCart", JSON.stringify(state.unpaidSinglePurchase));
      })
      .addCase(addFoodToGroupPurchaseThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.itemAdded = 1;
      })
      .addCase(addFoodToSinglePurchaseThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.itemAdded = 1;
      })
      .addCase(deleteFoodFromGroupPurchaseThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.itemAdded = 1;
      })
      .addCase(getOrdersThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.paidWaitingPurchases = action.payload;
      })
      .addCase(deleteFoodFromSinglePurchaseThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.itemAdded = 1;
      })
      .addCase(getOldPurchasesThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.pastOrders = action.payload.pastOrders;

        setTimeout(() => {
          action.payload.pastOrders?.forEach(order => {
            action.payload.dispatch(getOldPurchasesFoodsThunk({purchase_id: order.purchase_id}));
          });
        }, 100);
      })
      .addCase(getOldPurchasesFoodsThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.pastOrders = [...state.pastOrders.filter(p => p.purchase_id !== action.payload.purchase_id), 
          {...state.pastOrders.find(p => p.purchase_id === action.payload.purchase_id), products: action.payload.products}];
      })
  },
});

export const orderActions = orderSlice.actions;
export default orderSlice;

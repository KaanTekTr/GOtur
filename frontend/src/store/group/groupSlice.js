import { createAsyncThunk, createSlice, current } from "@reduxjs/toolkit";
import { addNewGroup, getAllGroups } from "../../lib/api/unsplashService";

const items = groupId =>
  localStorage.getItem(`groupCartItems_${groupId}`) !== null
    ? JSON.parse(localStorage.getItem(`groupCartItems_${groupId}`))
    : [];

const totalAmount = groupId =>
  localStorage.getItem(`groupTotalAmount_${groupId}`) !== null
    ? JSON.parse(localStorage.getItem(`groupTotalAmount_${groupId}`))
    : 0;

const totalQuantity = groupId =>
  localStorage.getItem(`groupTotalQuantity_${groupId}`) !== null
    ? JSON.parse(localStorage.getItem(`groupTotalQuantity_${groupId}`))
    : 0;

const setItemFunc = (item, totalAmount, totalQuantity, groupId) => {
  localStorage.setItem(`groupCartItems_${groupId}`, JSON.stringify(item));
  localStorage.setItem(`groupTotalAmount_${groupId}`, JSON.stringify(totalAmount));
  localStorage.setItem(`groupTotalQuantity_${groupId}`, JSON.stringify(totalQuantity));
};


export const getGroupsThunk = createAsyncThunk('user/getGroups', 
  async (data, thunkAPI) => {
    try {
      const  response = await getAllGroups(data.userId);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const addGroupsThunk = createAsyncThunk('user/addGroup', 
  async (data, thunkAPI) => {
    try {
      const  response = await addNewGroup(data.group);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

const initialState = {
    groups:[
        {
            id: 1,
            balance: 300,
            title: "Bros",
            members: [
                {
                    id: 1,
                    name: "Ali",
                    email: "ali@gmail.com"
                },
                {
                    id: 2,
                    name: "Korhan",
                    email: "korhan@gmail.com"
                },
                {
                    id: 3,
                    name: "Kaan",
                    email: "kaan@gmail.com"
                },
            ],
            groupCartItems: items(1),
            groupTotalAmount: totalAmount(1),
            groupTotalQuantity: totalQuantity(1),
            groupLeader: 1
        },
        {
            id: 2,
            balance: 300,
            title: "Family",
            members: [
                {
                    id: 1,
                    name: "Ali",
                    email: "ali@gmail.com"
                },
                {
                    id: 2,
                    name: "Korhan",
                    email: "korhan@gmail.com"
                },
                {
                    id: 3,
                    name: "Kaan",
                    email: "kaan@gmail.com"
                },
            ],
            groupCartItems: items(2),
            groupTotalAmount: totalAmount(2),
            groupTotalQuantity: totalQuantity(2),
            groupLeader: 3
        }
    ]
};

const groupsSlice = createSlice({
  name: "groups",
  initialState,

  reducers: {
    getGroups(state, action) {
        const { id } = action.payload;
        console.log("get groups", id);
    },

    // GROUP CART ACTIONS
    // =========== add item ============
    addItem(state, action) {
        const { newItem, groupId } = action.payload;
        console.log(current(state.groups.find(group => group.id === groupId)));
        console.log(groupId);
        console.log(newItem);
        const existingItem = state.groups.find(group => group.id === groupId).groupCartItems.find(
          (item) => item.id === newItem.id
        );
        state.groups.find(group => group.id === groupId).groupTotalQuantity++;
  
        if (!existingItem) {
          // ===== note: if you use just redux you should not mute state array instead of clone the state array, but if you use redux toolkit that will not a problem because redux toolkit clone the array behind the scene
  
          state.groups.find(group => group.id === groupId).groupCartItems.push({
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
  
        state.groups.find(group => group.id === groupId).groupTotalAmount = state.groups.find(group => group.id === groupId).groupCartItems.reduce(
          (total, item) => total + Number(item.price) * Number(item.quantity),
  
          0
        );
  
        setItemFunc(
          state.groups.find(group => group.id === groupId).groupCartItems.map((item) => item),
          state.groups.find(group => group.id === groupId).groupTotalAmount,
          state.groups.find(group => group.id === groupId).groupTotalQuantity,
          groupId,
        );
      },
  
    //============ delete item ===========

    deleteItem(state, action) {
        const { id, groupId } = action.payload;
        const existingItem = state.groups.find(group => group.id === groupId).groupCartItems.find((item) => item.id === id);
  
        if (existingItem) {
          state.groups.find(group => group.id === groupId).groupCartItems = state.groups.find(group => group.id === groupId).groupCartItems.filter((item) => item.id !== id);
          state.groups.find(group => group.id === groupId).groupTotalQuantity = state.groups.find(group => group.id === groupId).groupTotalQuantity - existingItem.quantity;
        }
  
        state.groups.find(group => group.id === groupId).groupTotalAmount = state.groups.find(group => group.id === groupId).groupCartItems.reduce(
          (total, item) => total + Number(item.price) * Number(item.quantity),
          0
        );
        setItemFunc(
          state.groups.find(group => group.id === groupId).groupCartItems.map((item) => item),
          state.groups.find(group => group.id === groupId).groupTotalAmount,
          state.groups.find(group => group.id === groupId).groupTotalQuantity
        );
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(getGroupsThunk.fulfilled, (state, action) => {
        state.groups = action.payload;
        console.log(action.payload);
      })
      .addCase(addGroupsThunk.fulfilled, (state, action) => {
        console.log(action.payload);
      })
  },
});

export const groupsActions = groupsSlice.actions;
export default groupsSlice;

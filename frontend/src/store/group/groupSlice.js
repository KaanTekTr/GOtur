import { createAsyncThunk, createSlice, current } from "@reduxjs/toolkit";
import { addMemberToGroup, addNewGroup, getAllGroups, getAllMembersOfGroup, transferBalanceToGroup } from "../../lib/api/unsplashService";

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


export const getGroupsThunk = createAsyncThunk('group/getGroups', 
  async (data, thunkAPI) => {
    try {
      const  response = await getAllGroups(data.userId);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const addGroupsThunk = createAsyncThunk('group/addGroup', 
  async (data, thunkAPI) => {
    try {
      const  response = await addNewGroup(data.group);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const getGroupMembersThunk = createAsyncThunk('group/getGroupMembers', 
  async (data, thunkAPI) => {
    try {
      const  response = await getAllMembersOfGroup(data.group_id);
      return {members: response.data, group_id: data.group_id};
    } catch (error) {
      console.log(error);
    }
  }
);

export const addGroupMemberThunk = createAsyncThunk('group/addGroupMember', 
  async (data, thunkAPI) => {
    try {
      const  response = await addMemberToGroup(data.group_id, data.customer_id);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

export const transferBalanceThunk = createAsyncThunk('group/transferBalance', 
  async (data, thunkAPI) => {
    try {
      const  response = await transferBalanceToGroup(data.group_id, data.customer_id, data.balance);
      return response.data;
    } catch (error) {
      console.log(error);
    }
  }
);

const initialState = {
    groups:[]
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
      .addCase(getGroupMembersThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.groups = [...state.groups.filter(group => `${group.group_id}` !== action.payload.group_id), 
          {...state.groups.find(group => `${group.group_id}` === action.payload.group_id), members: action.payload.members }]
      })
      .addCase(addGroupMemberThunk.fulfilled, (state, action) => {
        console.log(action.payload);
      })
  },
});

export const groupsActions = groupsSlice.actions;
export default groupsSlice;

import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    friends:[
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
    ]
};

const friendsSlice = createSlice({
  name: "friends",
  initialState,

  reducers: {
    getFriends(state, action) {
        const { id } = action.payload;
        console.log("get groups", id);
    },

    // GROUP CART ACTIONS

  },
});

export const friendsActions = friendsSlice.actions;
export default friendsSlice;

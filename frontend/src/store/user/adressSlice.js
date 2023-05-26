import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    address: [
        {
            id: 1,
            title: "Address 1",
            desc: "Üniversiteler Mah. Bilkent Üniversitesi 78. Yurt"
        },{
            id: 2,
            title: "Address 2",
            desc: "Üniversiteler Mah. Bilkent Üniversitesi 78. Yurt"
        },{
            id: 3,
            title: "Address 3",
            desc: "Üniversiteler Mah. Bilkent Üniversitesi 78. Yurt"
        }
    ],
    selectedAddress: {
        id: 1,
    },
    groupAddress: [
        {
            id: 1,
            title: "Group Address 1",
            desc: "Üniversiteler Mah. Bilkent Üniversitesi 78. Yurt"
        },{
            id: 2,
            title: "Group Address 2",
            desc: "Üniversiteler Mah. Bilkent Üniversitesi 78. Yurt"
        },{
            id: 3,
            title: "Group Address 3",
            desc: "Üniversiteler Mah. Bilkent Üniversitesi 78. Yurt"
        }
    ],
    selectedGroupAddress: {
        id: 1,
    }
};

const addressSlice = createSlice({
  name: "order",
  initialState,

  reducers: {
    // ACTIONS
    getAddresses(state, action) {
        console.log("get addresses");
    },
    changeSelectedAddress(state, action) {
        const { id } = action.payload;
        state.selectedAddress.id = id;
    },
    getGroupAddresses(state, action) {
        console.log("get group addresses");
    },
    changeSelectedGroupAddress(state, action) {
        const { id } = action.payload;
        state.selectedGroupAddress.id = id;
    }
  },
});

export const addressActions = addressSlice.actions;
export default addressSlice;

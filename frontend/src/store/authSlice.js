import { createSlice } from "@reduxjs/toolkit";

const authSlice = createSlice({
  name: "cartUi",
  initialState: {
    authType: localStorage.getItem('authType'),
    status: localStorage.getItem('userId') ? 'authenticated' : 'not-authenticated',
    key: localStorage.getItem('key'),
    userId: localStorage.getItem('userId'),
  },

  reducers: {
    login(state, action) {
      state.status = "authenticated";
    }
  },
});

export const authActions = authSlice.actions;
export default authSlice;

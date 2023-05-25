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
      const { email, password } = action.payload;
      console.log(email, "::", password);
      state.status = "authenticated";
      localStorage.setItem('userId', 1);
    },
    logout(state, action){
      state.status = "not-authenticated";
      localStorage.removeItem('userId');
    },
    register(state, action) {
      const { fullName, email, password } = action.payload;
      console.log(fullName, "::", email, "::", password);
    }
  },
});

export const authActions = authSlice.actions;
export default authSlice;

import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { userLogin, userLogout } from "../lib/api/unsplashService";

export const loginThunk = createAsyncThunk('auth/login', 
  async (data, thunkAPI) => {
    try {
      console.log(data);
      const  response = await userLogin(data.authType, data.email, data.password);
      return response.data;
    } catch (error) {
      
    }
  }
)

export const logoutThunk = createAsyncThunk('auth/logout', 
  async (data, thunkAPI) => {
    try {
      const  response = await userLogout(data.authType, data.userId);
      return response.data;
    } catch (error) {
      
    }
  }
)

const authSlice = createSlice({
  name: "cartUi",
  initialState: {
    authType: localStorage.getItem('authType'),
    status: localStorage.getItem('userId') ? 'authenticated' : 'not-authenticated',
    key: localStorage.getItem('key'),
    userId: localStorage.getItem('userId'),
    email: localStorage.getItem('email'),
    fullName: localStorage.getItem('fullName'),
    password: localStorage.getItem('password'),
    birthdate: localStorage.getItem('birthdate'),
    gender: localStorage.getItem('gender'),
    phoneNumber: localStorage.getItem('phoneNumber'),
  },


  reducers: {
    register(state, action) {
      const { fullName, email, password, birthdate, gender, authType, phoneNumber } = action.payload;
      console.log(fullName, "::", email, "::", password, "::", birthdate, "::", gender, "::", authType, "::", phoneNumber);
    }, 
    changeAuthType(state, action) {
      state.authType = action.payload;
    }
    
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginThunk.fulfilled, (state, action) => {
        state.status = "authenticated";
        localStorage.setItem('userId', action.payload);
        state.userId = action.payload;
      })
      .addCase(logoutThunk.fulfilled, (state, action) => {
        state.status = "not-authenticated";
        localStorage.removeItem('userId');
        state.userId = 0;
      })
  },
});

export const authActions = authSlice.actions;
export default authSlice;

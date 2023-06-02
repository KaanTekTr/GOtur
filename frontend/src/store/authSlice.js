import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { addBalanceCustomer, getUser, userLogin, userLogout, userRegister } from "../lib/api/unsplashService";

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

export const registerThunk = createAsyncThunk('auth/register', 
  async (data, thunkAPI) => {
    try {
      const  response = await userRegister(data.authType, data.user);
      return {response, nav: data.navigate};
    } catch (error) {
      console.log(error);
    }
  }
)

export const getUserThunk = createAsyncThunk('auth/getUser', 
  async (data, thunkAPI) => {
    try {
      console.log(data.userId);
      const  response = await getUser(data.authType, data.userId);
      return response.data;
    } catch (error) {
      
    }
  }
)

export const addBalanceThunk = createAsyncThunk('auth/addBalance', 
  async (data, thunkAPI) => {
    try {
      const  response = await addBalanceCustomer(data.amount, data.userId);
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
    user: JSON.parse(localStorage.getItem('user'))
  },


  reducers: {
    register(state, action) {
      const { fullName, email, password, birthdate, gender, authType, phoneNumber } = action.payload;
      console.log(fullName, "::", email, "::", password, "::", birthdate, "::", gender, "::", authType, "::", phoneNumber);
    }, 
    changeAuthType(state, action) {
      state.authType = action.payload;
      localStorage.setItem('authType', action.payload);
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
      .addCase(registerThunk.fulfilled, (state, action) => {
        action.payload.nav("/login");
        console.log(action.payload);
      })
      .addCase(getUserThunk.fulfilled, (state, action) => {
        if (action.payload )
          localStorage.setItem('user', JSON.stringify(action.payload));
        state.user = action.payload;
        console.log(action.payload);
      })
  },
});

export const authActions = authSlice.actions;
export default authSlice;

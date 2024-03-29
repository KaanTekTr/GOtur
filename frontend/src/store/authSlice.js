import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { addBalanceCustomer, createReportFavRest, createReportFavRestMaxPurch, createReportMaxCoup, createReportMaxRat, getUser, reportAll, reportDeleteAll, userLogin, userLogout, userRegister } from "../lib/api/unsplashService";

export const loginThunk = createAsyncThunk('auth/login', 
  async (data, thunkAPI) => {
    try {
      console.log(data);
      const response = await userLogin(data.authType, data.email, data.password);
      console.log(response);
      if (response.status === 200 && data.authType==="customer") {
        data.dispatch(authActions.changeAuthType("customer"));
        data.navigate("/home");
      } else if (response.status === 200){
        data.dispatch(authActions.changeAuthType("restaurantOwner"));
        data.navigate("/RestaurantOwnerHome");
      }
      console.log({ response: response, setVisible: data.setVisible });
      return { response: response, setVisible: data.setVisible };
    } catch (error) {
      console.log(error);
      data.setInfo(error.response.data);
      data.setVisible(v => !v);
      setTimeout(() => {
        data.setVisible(true);
      }, 100)
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
      if (response?.status !== 200) {
        data.setInfo("Something went wrong!");
        data.setVisible(v => !v);
        setTimeout(() => {
          data.setVisible(true);
        }, 100)
      }
      return {response, nav: data.navigate};
    } catch (error) {
      console.log(error);
      data.setInfo(error.response.data);
      data.setVisible(v => !v);
      setTimeout(() => {
        data.setVisible(true);
      }, 100)
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

export const createReport1Thunk = createAsyncThunk('auth/createRep1', 
  async (data, thunkAPI) => {
    try {
      const  response = await createReportFavRest(11);
      return response.data;
    } catch (error) {
      
    }
  }
)

export const createReport2Thunk = createAsyncThunk('auth/createRep2', 
  async (data, thunkAPI) => {
    try {
      const  response = await createReportMaxCoup(11);
      return response.data;
    } catch (error) {
      
    }
  }
)

export const createReport3Thunk = createAsyncThunk('auth/createRep3', 
  async (data, thunkAPI) => {
    try {
      const  response = await createReportFavRestMaxPurch(11);
      return response.data;
    } catch (error) {
      
    }
  }
)

export const createReport4Thunk = createAsyncThunk('auth/createRep4', 
  async (data, thunkAPI) => {
    try {
      const  response = await createReportMaxRat(11);
      return response.data;
    } catch (error) {
      
    }
  }
)

export const reportAllThunk = createAsyncThunk('auth/repAll', 
  async (data, thunkAPI) => {
    try {
      const  response = await reportAll(11);
      return response.data;
    } catch (error) {
      
    }
  }
)

export const reportDeleteAllThunk = createAsyncThunk('auth/repDelAll', 
  async (data, thunkAPI) => {
    try {
      const  response = await reportDeleteAll(11);
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
    user: JSON.parse(localStorage.getItem('user')),
    reports: [],
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
        console.log(action.payload);
        if (action.payload?.response?.status === 200) {
          state.status = "authenticated";
          localStorage.setItem('userId', action.payload.response.data);
          state.userId = action.payload.response.data;
        } 
      })
      .addCase(loginThunk.rejected, (state, action) => {
        console.log(action.payload);
      })
      .addCase(logoutThunk.fulfilled, (state, action) => {
        state.status = "not-authenticated";
        localStorage.removeItem('userId');
        state.userId = 0;
      })
      .addCase(registerThunk.fulfilled, (state, action) => {
        if (action.payload.response.status === 200)
          action.payload.nav("/login");
        console.log(action.payload);
      })
      .addCase(getUserThunk.fulfilled, (state, action) => {
        if (action.payload )
          localStorage.setItem('user', JSON.stringify(action.payload));
        state.user = action.payload;
        console.log(action.payload);
      })
      .addCase(reportAllThunk.fulfilled, (state, action) => {
        console.log(action.payload);
        state.reports = action.payload;
      })
  },
});

export const authActions = authSlice.actions;
export default authSlice;

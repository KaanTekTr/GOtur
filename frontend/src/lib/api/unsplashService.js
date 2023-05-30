import axios from 'axios';

export const baseURL = 'http://localhost:8080';

// Login / Logout
export const userLogin = (authType, email, password) => (
    axios.post(`${baseURL}/profile/login/${authType}?email=${email}&password=${password}`)
  );

export const userLogout = (authType, id) => (
    axios.post(`${baseURL}/profile/logout/${authType}/${id}`)
  );
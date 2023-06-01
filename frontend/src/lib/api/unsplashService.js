import axios from 'axios';

export const baseURL = 'http://localhost:8080';

// Login / Logout
export const userLogin = (authType, email, password) => (
    axios.post(`${baseURL}/profile/login/${authType}?email=${email}&password=${password}`)
  );

export const userLogout = (authType, id) => (
    axios.post(`${baseURL}/profile/logout/${authType}/${id}`)
  );

export const getCustomerAdresses = (id) => (
    axios.get(`${baseURL}/customer/allAddress/${id}`)
  );

export const addNewAddress = (id, address) => (
  fetch(`${baseURL}/customer/addAddress/${id}`, {
    method: 'POST',
    headers: {
      'Content-type' : 'application/json'
    }, 
    body: JSON.stringify(address)
  })
);

// Restaurant Requests
export const getAllRestaurants = () => (
  axios.get(`${baseURL}/restaurant/all`)
);

export const addNewRestaurant = (id, restaurant) => (
  fetch(`${baseURL}/restaurantOwner/addRestaurant/${id}`, {
    method: 'POST',
    headers: {
      'Content-type' : 'application/json'
    }, 
    body: JSON.stringify(restaurant)
  })
);

export const addFoodCategory= (foodCategory) => (
  fetch(`${baseURL}/foodCategory/add`, {
    method: 'POST',
    headers: {
      'Content-type' : 'application/json'
    }, 
    body: JSON.stringify(foodCategory)
  })
);

export const addFoodCategoryToRestaurant = (category_id, restaurant_id) => (
  axios.post(`${baseURL}/restaurantOwner/addToRestaurant/${category_id}/${restaurant_id}`)
);

export const getAllFoodCategory = (restaurant_id) => (
  axios.get(`${baseURL}/foodCategory/restaurantId/${restaurant_id}`)
);


// FRIENDS

export const getAllFriends = (id) => (
  axios.get(`${baseURL}/customer/allFriends/${id}`)
);

export const addNewFriend = (user_id, friend_id) => (
  axios.post(`${baseURL}/customer/addFriends/${user_id}/${friend_id}`)
);

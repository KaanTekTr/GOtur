import axios from 'axios';

export const baseURL = 'http://localhost:8080';

// Login / Logout / Register
export const userLogin = (authType, email, password) => (
    axios.post(`${baseURL}/profile/login/${authType}?email=${email}&password=${password}`)
  );

export const userLogout = (authType, id) => (
    axios.post(`${baseURL}/profile/logout/${authType}/${id}`)
  );

export const userRegister = (authType, user) => (
    fetch(`${baseURL}/${authType}/add`, {
      method: 'POST',
      headers: {
        'Content-type' : 'application/json'
      }, 
      body: JSON.stringify(user)
    })
  );

// ADDRESS

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

// USER
export const getUser = (authType, userId) => (
  axios.get(`${baseURL}/${authType}/${userId}`)
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

export const getRestaurantsOfOwner = (id) => (
  axios.get(`${baseURL}/restaurantOwner/restaurants/${id}`)
);

// FRIENDS

export const getAllFriends = (id) => (
  axios.get(`${baseURL}/customer/allFriends/${id}`)
);

export const addNewFriend = (user_id, friend_id) => (
  axios.post(`${baseURL}/customer/addFriends/${user_id}/${friend_id}`)
);

// GROUP
export const getAllGroups = (id) => (
  axios.get(`${baseURL}/purchaseGroup/getAllPurchaseGroups/${id}`)
);

export const addNewGroup= (group) => (
  fetch(`${baseURL}/purchaseGroup/add`, {
    method: 'POST',
    headers: {
      'Content-type' : 'application/json'
    }, 
    body: JSON.stringify(group)
  })
);

// FOOD CATEGORY
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

// FOOD
export const addNewFood= (food) => (
  fetch(`${baseURL}/restaurant/addFood`, {
    method: 'POST',
    headers: {
      'Content-type' : 'application/json'
    }, 
    body: JSON.stringify(food)
  })
);

export const getAllFoodsOfRest = (id) => (
  axios.get(`${baseURL}/restaurant/allFood/restaurantId/${id}`)
);

// MENU CATEGORY
export const addNewMenuCategory= (menu) => (
  fetch(`${baseURL}/restaurant/addMenuCategory`, {
    method: 'POST',
    headers: {
      'Content-type' : 'application/json'
    }, 
    body: JSON.stringify(menu)
  })
);

export const getAllMenuCategories = (id) => (
  axios.get(`${baseURL}/restaurant/allMenuCategories/${id}`)
);

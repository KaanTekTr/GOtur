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

export const  deleteAddress = (userId, addressId) => (
  axios.delete(`${baseURL}/customer/deleteAddress/${userId}/${addressId}`)
);



export const setAddressPrimary = (addressId) => (
  axios.post(`${baseURL}/customer/setAddressPrimary/${addressId}`)
);

// USER
export const getUser = (authType, userId) => (
  axios.get(`${baseURL}/${authType}/${userId}`)
);

export const addBalanceCustomer = (amount, userId) => (
  axios.post(`${baseURL}/customer/addBalance/${userId}?transferAmount=${amount}`)
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

export const getRestaurantOrders = (userId) => (
  axios.get(`${baseURL}/restaurantOwner/getAllRestaurantPurchases/${userId}`)
);

export const getRestaurantsOfOwner = (id) => (
  axios.get(`${baseURL}/restaurantOwner/restaurants/${id}`)
);

export const addFavRestaurants = (id, rest_id) => (
  axios.post(`${baseURL}/customer/addFavoriteRestaurant/${id}/${rest_id}`)
);

// FRIENDS

export const getAllFriends = (id) => (
  axios.get(`${baseURL}/customer/allFriends/${id}`)
);

export const addNewFriend = (user_id, friend_email) => (
  axios.post(`${baseURL}/customer/addFriends/${user_id}/${friend_email}`)
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

export const getAllMembersOfGroup = (id) => (
  axios.get(`${baseURL}/purchaseGroup/allMembers/${id}`)
);

export const getOwnerOfGroup = (id) => (
  axios.get(`${baseURL}/purchaseGroup/groupOwner/${id}`)
);

export const addMemberToGroup = (group_id, customer_id) => (
  axios.post(`${baseURL}/purchaseGroup/add/${group_id}/${customer_id}`)
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

// PURCHASES
export const getUnpaidSinglePurchase = (id) => (
  axios.get(`${baseURL}/purchase/getUnpaidSinglePurchase/${id}`)
);

export const getUnpaidGroupPurchase = (id) => (
  axios.get(`${baseURL}/purchaseGroup/getUnpaidGroupPurchase/${id}`)
);

export const getAllSinglePurchases = (id) => (
  axios.get(`${baseURL}/purchase/getPaidSinglePurchases/${id}`)
);

export const getAllGroupPurchases = (id) => (
  axios.get(`${baseURL}/purchaseGroup/getPaidGroupPurchases/${id}`)
);

export const getProductUnpaidSinglePurchase = (id) => (
  axios.get(`${baseURL}/purchase/getAllFoodAndIngredientsUnpaidSinglePurchase/${id}`)
);

export const getProductUnpaidGroupPurchase = (id) => (
  axios.get(`${baseURL}/purchaseGroup/getAllFoodAndIngredientsUnpaidGroupPurchase/${id}`)
);

export const addFoodToSinglePurchase= (food, id) => (
  fetch(`${baseURL}/purchase/addFoodWithIngredient/${id}`, {
    method: 'POST',
    headers: {
      'Content-type' : 'application/json'
    }, 
    body: JSON.stringify(food)
  })
);

export const addFoodToGroupPurchase= (food, id) => (
  fetch(`${baseURL}/purchaseGroup/addFoodWithIngredient/${id}`, {
    method: 'POST',
    headers: {
      'Content-type' : 'application/json'
    }, 
    body: JSON.stringify(food)
  })
);

export const  deleteFoodFromSinglePurchase = (customer_id, food_id, food_order) => (
  axios.delete(`${baseURL}/purchase/deleteFoodWithIngredientFromSingleUnpaidPurchase/${customer_id}/${food_id}/${food_order}`)
);

export const  deleteFoodFromGroupPurchase = (customer_id, food_id, food_order) => (
  axios.delete(`${baseURL}/purchaseGroup/deleteFoodWithIngredientFromGroupUnpaidPurchase/${customer_id}/${food_id}/${food_order}`)
);

export const completeSinglePurchase = (purchase_id, address_id, note, couponId) => (
  axios.post(`${baseURL}/purchase/completePurchase/${purchase_id}?addressId=${address_id}&customerNote=${note}&couponId=${couponId}`)
);

export const completeGroupPurchase = (purchase_id, address_id, note, couponId) => (
  axios.post(`${baseURL}/purchaseGroup/completeGroupPurchase/${purchase_id}?addressId=${address_id}&customerNote=${note}&couponId=${couponId}`)
);

export const getOldPurchases = (id) => (
  axios.get(`${baseURL}/purchase/getAllPurchases/${id}`)
);

export const getOldPurchasesFoods = (id) => (
  axios.get(`${baseURL}/purchase/getAllFoodAndIngredients/${id}`)
);

// DISCOUNT
export const getDiscountsOfCustomer = (user_id) => (
    axios.get(`${baseURL}/discountCoupon/getAllDiscountItems/${user_id}`)
);
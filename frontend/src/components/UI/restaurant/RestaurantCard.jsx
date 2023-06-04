import React from "react";

import "../../../styles/product-card.css";

import { Link, useNavigate } from "react-router-dom";

import image01 from "../../../assets/images/dominos.png";
import { useDispatch, useSelector } from "react-redux";
import { addFavRestaurantsThunk } from "../../../store/restaurant/restaurantSlice";

const RestaurantCard = (props) => {
  const { restaurant_id, restaurant_name, min_delivery_price } = props.item;

  const userId = useSelector(state => state.auth.userId);

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const seeFoods = () => {
    navigate(`/restaurants/${restaurant_id}`)
  };

  const addFavorite = () => {
   dispatch(addFavRestaurantsThunk({userId, rest_id: restaurant_id}))
  };

  return (
    <div className="product__item" >
      <div className="product__img" onClick={seeFoods}>
        <img src={image01} alt="product-img" className="w-50" />
      </div>

      <div className="product__content">
        <h5>
          <Link to={`/restaurants/${restaurant_id}`}>{restaurant_name}</Link>
        </h5>
        <div className=" d-block align-items-center justify-content-between ">
          <span className="min__price">Min delivery price: ${min_delivery_price}</span>
          <button  className="addTOCart__btn" onClick={seeFoods}>
            See Foods
          </button> {"   "}
          <button  className="addTOCart__btn" onClick={addFavorite}>
            Favorite
          </button>
        </div>
      </div>
    </div>
  );
};

export default RestaurantCard;

import React from "react";

import "../../../styles/product-card.css";

import { Link, useNavigate } from "react-router-dom";

import image01 from "../../../assets/images/dominos.png";

const RestaurantCard = (props) => {
  const { restaurant_id, restaurant_name, min_delivery_price } = props.item;

  const navigate = useNavigate();

  const seeFoods = () => {
    navigate(`/restaurants/${restaurant_id}`)
  };

  return (
    <div className="product__item" onClick={seeFoods}>
      <div className="product__img">
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
          </button>
        </div>
      </div>
    </div>
  );
};

export default RestaurantCard;

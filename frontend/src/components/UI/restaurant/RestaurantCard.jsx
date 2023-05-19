import React from "react";

import "../../../styles/product-card.css";

import { Link, useNavigate } from "react-router-dom";

const RestaurantCard = (props) => {
  const { id, title, image01, minPrice } = props.item;

  const navigate = useNavigate();

  const seeFoods = () => {
    navigate(`/restaurants/${id}`)
  };

  return (
    <div className="product__item">
      <div className="product__img">
        <img src={image01} alt="product-img" className="w-50" />
      </div>

      <div className="product__content">
        <h5>
          <Link to={`/restaurants/${id}`}>{title}</Link>
        </h5>
        <div className=" d-block align-items-center justify-content-between ">
          <span className="min__price">Min delivery price: ${minPrice}</span>
          <button  className="addTOCart__btn" onClick={seeFoods}>
            See Foods
          </button>
        </div>
      </div>
    </div>
  );
};

export default RestaurantCard;

import React, { useState } from "react";

import "../../../styles/product-card.css";

import { Link, useNavigate } from "react-router-dom";
import { Button } from 'reactstrap';

import image01 from "../../../assets/images/bread.png";


import { useDispatch, useSelector } from "react-redux";
import { deleteFoodThunk, restaurantActions } from "../../../store/restaurant/restaurantSlice";

const MenuItem = (props) => {
  const { food_id, food_name, price } = props.item;
  const dispatch = useDispatch();

  // New state for the new price form
  const [newPrice, setNewPrice] = useState(price);

  // Function to handle deleting a product
  const deleteProduct = () => {
    dispatch(deleteFoodThunk({ foodId: food_id }));
    alert('Food is successfully removed!');
  }

  // Function to handle editing the price of a product
  const editPrice = () => {
    if (newPrice) {
    //   dispatch(restaurantActions.editProductPrice({ id, newPrice }));
    } else {
      console.error('Please enter a valid price');
    }
  };

  const navigate = useNavigate();
  const navToFood = () => {
    navigate(`/foods/${food_id}`)
  }

  return (
    <div className="product__item">
      <div className="product__img" onClick={navToFood}>
        <img src={image01} alt="product-img" className="w-50" />
      </div>

      <div className="product__content">
        <h5>
          <Link to={`/foods/${food_id}`}>{food_name}</Link>
        </h5>
        <div className=" d-flex align-items-center justify-content-between ">
          <span className="product__price">${price}</span>
          <Button size="sm" color="primary" className="editPrice__btn" onClick={editPrice}>
            Edit Price
          </Button>
          <Button size="sm" color="danger" className="deleteProduct__btn" onClick={deleteProduct}>
            Delete Product
          </Button>
        </div>
      </div>
    </div>
  );
};

export default MenuItem;

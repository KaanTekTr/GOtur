import React, { useState } from "react";

import "../../../styles/product-card.css";

import { Link, useNavigate } from "react-router-dom";

import image01 from "../../../assets/images/bread.png"; 

import { useDispatch, useSelector } from "react-redux";
import { cartActions } from "../../../store/shopping-cart/cartSlice";
import { groupsActions } from "../../../store/group/groupSlice";

const ProductCard = (props) => {
  const { food_id, food_name, price } = props.item;
  const dispatch = useDispatch();

  const selectedCart = useSelector(state => state.order.currentCart);

  const addToCart = () => {
    if (selectedCart === 0) {
      dispatch(
        cartActions.addItem({
          id: food_id,
          title: food_name,
          image01,
          price,
        })
      );
    } else {
      dispatch(
        groupsActions.addItem({ 
          newItem: {
            id: food_id,
            title: food_name,
            image01,
            price,
          },
          groupId: selectedCart
        })
      );
    }
    props.setVisible(!props.visible);
    setTimeout(() => {
      props.setVisible(true);
    }, 100)
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
          <button className="addTOCart__btn" onClick={addToCart}>
            Add to Cart
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProductCard;

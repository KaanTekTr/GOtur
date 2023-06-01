import React from "react";
import { ListGroupItem } from "reactstrap";

import "../../../styles/cart-item.css";

import image01 from "../../../assets/images/bread.png";

import { useDispatch } from "react-redux";
import { cartActions } from "../../../store/shopping-cart/cartSlice";

const CartItem = ({ item }) => {
  const { food_id, food_name, price } = item.food;

  const dispatch = useDispatch();

  const incrementItem = () => {
    dispatch(
      cartActions.addItem({
        id: food_id,
        title: food_name,
        price,
      })
    );
  };

  const decreaseItem = () => {
    dispatch(cartActions.removeItem(food_id));
  };

  const deleteItem = () => {
    dispatch(cartActions.deleteItem(food_id));
  };

  return (
    <ListGroupItem className="border-0 cart__item">
      <div className="cart__item-info d-flex gap-2">
        <img src={image01} alt="product-img" />

        <div className="cart__product-info w-100 d-flex align-items-center gap-4 justify-content-between">
          <div>
            <h6 className="cart__product-title">{food_name}</h6>
            <p className=" d-flex align-items-center gap-5 cart__product-price">
              <span>${price}</span>
            </p>
          </div>

          <span className="delete__btn" onClick={deleteItem}>
            <i class="ri-close-line"></i>
          </span>
        </div>
      </div>
    </ListGroupItem>
  );
};

export default CartItem;

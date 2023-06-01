import React from "react";

import { ListGroup } from "reactstrap";
import { Link } from "react-router-dom";
import CartItem from "./CartItem";

import { useDispatch, useSelector } from "react-redux";
import { cartUiActions } from "../../../store/shopping-cart/cartUiSlice";

import "../../../styles/shopping-cart.css";

const Carts = () => {
  const dispatch = useDispatch();
  const cart = useSelector((state) => state.order.unpaidSinglePurchase);

  const toggleCart = () => {
    dispatch(cartUiActions.toggle());
  };
  return (
    <>
      {cart ? (
        <div className="cart__container">
        <ListGroup className="cart">
          <div className="cart__close">
            <span onClick={toggleCart}>
              <i class="ri-close-fill"></i>
            </span>
          </div>
  
          <div className="cart__item-list">
            {cart.products?.length === 0 ? (
              <h6 className="text-center mt-5">No item added to the cart</h6>
            ) : (
              cart.products?.map((item, index) => (
                <CartItem item={item} key={index} />
              ))
            )}
          </div>
  
          <div className="cart__bottom d-flex align-items-center justify-content-between">
            <h6>
              Subtotal : <span>${cart.total_price}</span>
            </h6>
            <button>
              <Link to="/checkout" onClick={toggleCart}>
                Checkout
              </Link>
            </button>
          </div>
        </ListGroup>
      </div>
      ) : <span>Loading...</span>}
    </>
    
  );
};

export default Carts;

import React from "react";

import CommonSection from "../components/UI/common-section/CommonSection";
import Helmet from "../components/Helmet/Helmet";
import "../styles/cart-page.css";
import { useSelector, useDispatch } from "react-redux";
import { Container, Row, Col } from "reactstrap";
import { cartActions } from "../store/shopping-cart/cartSlice";
import { Link, useNavigate } from "react-router-dom";
import { deleteFoodFromSinglePurchaseThunk, orderActions } from "../store/user/orderSlice";

import image01 from "../assets/images/bread.png";


const Cart = () => {
  const cart = useSelector((state) => state.order.unpaidSinglePurchase);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const seeRestaurants = () => {
    dispatch(orderActions.updateCurrentCart({id: 0}));
    navigate("/restaurants");
  }

  return (
    <Helmet title="Cart">
      <CommonSection title="Your Cart" />
      <section>
        <Container>
          <Row>
            <Col lg="12">
              {cart?.products?.length === 0 ? (
                <h5 className="text-center">Your cart is empty</h5>
              ) : (
                <table className="table table-bordered">
                  <thead>
                    <tr>
                      <th>Image</th>
                      <th>Product Title</th>
                      <th>Price</th>
                      <th>Delete</th>
                    </tr>
                  </thead>
                  <tbody>
                    {cart?.products?.map((item) => (
                      <Tr item={item} key={item.id} />
                    ))}
                  </tbody>
                </table>
              )}

              <div className="mt-4">
                <h6>
                  Subtotal: $
                  <span className="cart__subtotal">{cart.total_price}</span>
                </h6>
                <p>Taxes and shipping will calculate at checkout</p>
                <div className="cart__page-btn">
                  <button className="addTOCart__btn me-4" onClick={seeRestaurants}>
                    Continue Shopping
                  </button>
                  <button className="addTOCart__btn">
                    <Link to="/checkout">Proceed to checkout</Link>
                  </button>
                </div>
              </div>
            </Col>
          </Row>
        </Container>
      </section>
    </Helmet>
  );
};

const Tr = (props) => {
  const { food_id, food_name, price } = props.item.food;
  const dispatch = useDispatch();
  const userId = useSelector(state => state.auth.userId);

  const deleteItem = () => {
    dispatch(deleteFoodFromSinglePurchaseThunk({customer_id: userId, food_id, food_order: props.item.food_order}));
  };
  return (
    <tr>
      <td className="text-center cart__img-box">
        <img src={image01} alt="" />
      </td>
      <td className="text-center">{food_name}</td>
      <td className="text-center">${price}</td>
      <td className="text-center cart__item-del">
        <i class="ri-delete-bin-line" onClick={deleteItem}></i>
      </td>
    </tr>
  );
};

export default Cart;

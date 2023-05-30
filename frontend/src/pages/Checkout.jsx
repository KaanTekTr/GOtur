import React, { useState } from "react";
import { useSelector } from "react-redux";
import { Container, Row, Col, Card, CardTitle, Input, CardSubtitle, Table } from "reactstrap";
import CommonSection from "../components/UI/common-section/CommonSection";
import Helmet from "../components/Helmet/Helmet";
import { Link, useNavigate } from "react-router-dom";

import "../styles/checkout.css";

const Checkout = () => {

  const cartItems = useSelector((state) => state.cart.cartItems);

  const cartTotalAmount = useSelector((state) => state.cart.totalAmount);
  const shippingCost = 30;

  const totalAmount = cartTotalAmount + Number(shippingCost);
  const navigate = useNavigate();

  const handlePayment = () => {
    navigate(`/payment`)
  }

  return (
    <Helmet title="Checkout">
      <CommonSection title="Checkout" />
      <section>
        <Container>
          <Row>
            <Col lg="8" md="6">
              <Card className="p-4">
                <CardTitle tag="h3">
                    Order 
                </CardTitle>
              
                <Table>
                    <thead>
                        <tr>
                        <th className="text-center">
                            Image
                        </th>
                        <th className="text-center">
                            Product Title
                        </th>
                        <th className="text-center">
                            Price
                        </th>
                        <th className="text-center">
                            Quantity
                        </th>
                        </tr>
                    </thead>
                    <tbody>
                        {cartItems.map((item) => (
                          <Tr item={item} key={item.id} />
                        ))}
                    </tbody>
                    </Table>
              <Card className="p-4 mt-4">
                <CardTitle tag="h5">
                    Customer Note
                </CardTitle>
                  <div>
                    <Input placeholder="Add note..." className="mb-2"/>
                  </div>
                  <div>
                    <button type="submit" className="addTOCart__btn" onClick={handlePayment}>
                      Payment
                    </button>
                  </div>
              </Card>
            </Card>
            </Col>

            <Col lg="4" md="6">
              <div className="checkout__bill">
                <h6 className="d-flex align-items-center justify-content-between mb-3">
                  Subtotal: <span>${cartTotalAmount}</span>
                </h6>
                <h6 className="d-flex align-items-center justify-content-between mb-3">
                  Shipping: <span>${shippingCost}</span>
                </h6>
                <div className="checkout__total">
                  <h5 className="d-flex align-items-center justify-content-between">
                    Total: <span>${totalAmount}</span>
                  </h5>
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
  const { image01, title, price, quantity } = props.item;

  return (
    <tr>
      <td className="text-center cart__img-box">
        <img src={image01} alt="" />
      </td>
      <td className="text-center">{title}</td>
      <td className="text-center">${price}</td>
      <td className="text-center">{quantity}px</td>
    </tr>
  );
};

export default Checkout;

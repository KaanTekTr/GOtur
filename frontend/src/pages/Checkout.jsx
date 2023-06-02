import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Container, Row, Col, Card, CardTitle, Input, CardSubtitle, Table } from "reactstrap";
import CommonSection from "../components/UI/common-section/CommonSection";
import Helmet from "../components/Helmet/Helmet";
import { Link, useNavigate } from "react-router-dom";

import image01 from "../assets/images/bread.png"; 

import "../styles/checkout.css";
import { completeSinglePurchaseThunk } from "../store/user/orderSlice";

const Checkout = () => {

  const cart = useSelector((state) => state.order.unpaidSinglePurchase);
  const addresses = useSelector(state => state.address.address);
  const [note, setNote] = useState("");

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handlePayment = () => {
    dispatch(completeSinglePurchaseThunk({purchase_id: cart.purchase_id, address_id: addresses.find(ad => ad.is_primary)?.address_id, note, coupon_id: -1}))
    //navigate(`/payment`)
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
              
                {cart?.products?.length === 0 ? (
                <h5 className="text-center">Your cart is empty</h5>
              ) : (
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
                        </tr>
                    </thead>
                    <tbody>
                        {cart.products?.map((item) => (
                          <Tr item={item} key={item.id} />
                        ))}
                    </tbody>
                    </Table>)}
              <Card className="p-4 mt-4">
                <CardTitle tag="h5">
                    Customer Note
                </CardTitle>
                  <div>
                    <Input placeholder="Add note..." value={note} onChange={e=>setNote(e.target.value)} className="mb-2"/>
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
                  Subtotal: <span>${cart.total_price || 0}</span>
                </h6>
                <h6 className="d-flex align-items-center justify-content-between mb-3">
                  Shipping: <span>${0}</span>
                </h6>
                <div className="checkout__total">
                  <h5 className="d-flex align-items-center justify-content-between">
                    Total: <span>${cart.total_price || 0}</span>
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
  const { food_name, price } = props.item.food;

  return (
    <tr>
      <td className="text-center cart__img-box">
        <img src={image01} alt="" />
      </td>
      <td className="text-center">{food_name}</td>
      <td className="text-center">${price}</td>
    </tr>
  );
};

export default Checkout;

import React from 'react';
import Helmet from '../components/Helmet/Helmet';
import CommonSection from '../components/UI/common-section/CommonSection';
import { Container, Row, Col, Card, CardTitle, CardSubtitle, CardText } from 'reactstrap';
import { useDispatch, useSelector } from "react-redux";
import { getDiscountsThunk } from "../store/user/discountSlice";
import { useEffect } from 'react';

const DiscountCoupons = () => {
  // Assume coupons data exists in your Redux store and each coupon item has code, discount, restaurant, and expiryDate properties.
//   const coupons = useSelector((state) => state.user.currentCustomer.coupons);
 // Dummy coupons data
 const userId = useSelector(state => state.auth.userId);
 console.log(userId)
 const dispatch = useDispatch();
 useEffect(() => {
    dispatch(getDiscountsThunk({userId}));
  }, [dispatch, userId]);
const coupons = useSelector(state => state.discountCoupon.discountCoupons);

/*
 const coupons = [
    {
      code: 'COUPON10',
      discount: 10,
      restaurant: 'Pizza Palace',
      expiryDate: '2024-06-01',
    },
    {
      code: 'COUPON20',
      discount: 20,
      restaurant: 'Burger King',
      expiryDate: '2024-05-01',
    },
    {
      code: 'COUPON30',
      discount: 30,
      restaurant: 'Taco Tower',
      expiryDate: '2024-07-01',
    },
  ];
  */
  
  return (
    <Helmet title="Discount Coupons">
      <CommonSection title="Discount Coupons" />

      <section>
        <Container>
          <Row>
            {coupons.map((coupon) => (
              <Col lg="6" md="6" sm="12" xs="12" key={coupon.code} className="mb-4">
                <Card className="p-4">
                  <CardTitle tag="h4">{coupon.code}</CardTitle>
                  <CardSubtitle tag="h6" className="mb-2 text-muted">Restaurant: {coupon.restaurant.restaurant_name}</CardSubtitle>
                  <CardText>
                    Discount Percentage: {coupon.restaurant.discount_percentage}% <br/>
                    Status: {coupon.is_used && "Coupon Used"} {!coupon.is_used && "Active"}
                  </CardText>
                </Card>
              </Col>
            ))}
          </Row>
        </Container>
      </section>
    </Helmet>
  );
};

export default DiscountCoupons;

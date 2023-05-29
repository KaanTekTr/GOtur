import React from 'react';
import Helmet from '../components/Helmet/Helmet';
import CommonSection from '../components/UI/common-section/CommonSection';
import { Container, Row, Col, Card, CardTitle, CardSubtitle, CardText } from 'reactstrap';
import { useSelector } from 'react-redux';

const DiscountCoupons = () => {
  // Assume coupons data exists in your Redux store and each coupon item has code, discount, restaurant, and expiryDate properties.
//   const coupons = useSelector((state) => state.user.currentCustomer.coupons);
 // Dummy coupons data
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
                  <CardSubtitle tag="h6" className="mb-2 text-muted">{coupon.restaurant}</CardSubtitle>
                  <CardText>
                    Discount Amount: ${coupon.discount} <br/>
                    Expiry Date: {new Date(coupon.expiryDate).toLocaleDateString()}
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

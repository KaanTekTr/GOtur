import React from "react";
import { Col, Container } from "reactstrap";
import "../../../styles/common-section.css";

const CommonSection = (props) => {
  console.log(props);
  return (
    <section className="common__section">
      <Container style={{display: "flex"}}>
        <Col lg="6">
          <h1 className="text-white mb-3" style={{fontSize: "64px"}}>{props.title}</h1>
          {props.desc ? <h5 className="text-white">Minimum Delivery Price {props.desc}$</h5>: null}
        </Col>
        <Col lg="3"></Col>
        <Col lg="2" >
          {props.desc ? <img src={props.image} alt="product-img" className="w-10" />: null}
        </Col>

      </Container>
    </section>
  );
};

export default CommonSection;

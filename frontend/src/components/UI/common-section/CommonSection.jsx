import React from "react";
import { Container } from "reactstrap";
import "../../../styles/common-section.css";

const CommonSection = (props) => {
  console.log(props);
  return (
    <section className="common__section">
      <Container>
        <h1 className="text-white mb-3">{props.title}</h1>
        {props.desc ? <h6 className="text-white">Minimum Delivery Price {props.desc}$</h6>: null}

      </Container>
    </section>
  );
};

export default CommonSection;

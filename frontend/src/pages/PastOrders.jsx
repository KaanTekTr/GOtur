import React, { useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Card, Table, CardTitle, CardSubtitle } from "reactstrap";

import products from "../assets/fake-data/products";
import ProductCard from "../components/UI/product-card/ProductCard";
import ReactPaginate from "react-paginate";

import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useSelector } from "react-redux";

const PastOrders = () => {

  const cartItems = useSelector((state) => state.cart.cartItems);


  const [pageNumber, setPageNumber] = useState(0);

  const productPerPage = 5;
  const visitedPage = pageNumber * productPerPage;
  const displayPage = cartItems.slice(
    visitedPage,
    visitedPage + productPerPage
  );

  const pageCount = Math.ceil(cartItems.length / productPerPage);

  const changePage = ({ selected }) => {
    setPageNumber(selected);
  };

  return (
    <Helmet title="Past Orders">
      <CommonSection title="Past Orders" />

      <section>
        <Container>
          <Row>

            {displayPage.map((item, index) => (
              <Col lg="12" md="12" sm="6" xs="6" key={item.id} className="mb-4">
                <Card className="p-4">
                    <CardTitle tag="h3">
                        Order {index + 1}
                    </CardTitle>
                    <CardSubtitle
                        className="mb-2 text-muted"
                        tag="h6"
                    >
                        22 May 2023
                    </CardSubtitle>
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

                    <h6>
                        Subtotal: $
                        <span className="cart__subtotal">{100}</span>
                    </h6>
                </Card>
                
              </Col>
            ))}

            <div>
              <ReactPaginate
                pageCount={pageCount}
                onPageChange={changePage}
                previousLabel={"Prev"}
                nextLabel={"Next"}
                containerClassName=" paginationBttns "
              />
            </div>
          </Row>
        </Container>
      </section>
    </Helmet>
  );
};


const Tr = (props) => {
    const { id, image01, title, price, quantity } = props.item;

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

export default PastOrders;

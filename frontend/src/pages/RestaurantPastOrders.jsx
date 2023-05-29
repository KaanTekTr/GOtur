import React, { useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Card, Table, CardTitle, CardSubtitle, Button, Input } from "reactstrap";

import ReactPaginate from "react-paginate";

import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useSelector } from "react-redux";

const RestaurantPastOrders = () => {

  // Assuming you have a currentRestaurant field in your state to get the current restaurant's past orders
//   const pastOrders = useSelector((state) => state.restaurant.currentRestaurant.pastOrders);
   const pastOrders = useSelector((state) => state.order.pastOrders);


  // New state for the restaurant owner's response
  const [response, setResponse] = useState('');

  // Function to handle the restaurant owner submitting their response
  const handleSubmitResponse = () => {
    // You would need to dispatch an action here to update the order with the restaurant owner's response
    // For now, we'll just log the response
    console.log(response);
  };

  const [pageNumber, setPageNumber] = useState(0);

  const productPerPage = 5;
  const visitedPage = pageNumber * productPerPage;
  const displayPage = pastOrders.slice(
    visitedPage,
    visitedPage + productPerPage
  );

  const pageCount = Math.ceil(pastOrders.length / productPerPage);

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
                        {item.date}
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
                        {item.items.map((item) => (
                        <Tr item={item} key={item.id} />
                        ))}
                    </tbody>
                    </Table>

                    <h6>
                        Subtotal: $
                        <span className="cart__subtotal">{item.totalAmount}</span>
                    </h6>

                    <Card className="p-2 mt-4">
                      <CardTitle tag="h5">
                          Comment
                      </CardTitle>
                      {<span>{item.comment}</span>}
                    </Card>
                        {item.comment!==""?
                    <Card className="p-2 mt-4">
                      <CardTitle tag="h5">
                          Restaurant Response
                      </CardTitle>
                      {item.response ? (
                        <span>{item.response}</span>
                      ) : (
                        <div>
                          <Input
                            placeholder="Write your response..."
                            className="mb-2"
                            value={response}
                            onChange={e => setResponse(e.target.value)}
                          />
                          <Button onClick={handleSubmitResponse}>Submit Response</Button>
                        </div>
                      )}
                    </Card>
                    : <></>}
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

export default RestaurantPastOrders;

import React, { useState } from "react";
import Helmet from "../../Helmet/Helmet";
import CommonSection from "../common-section/CommonSection";
import { Container, Row, Col, Card, Table, CardTitle, CardSubtitle, Button } from "reactstrap";
import ReactPaginate from "react-paginate";
import { useSelector } from "react-redux";

const IncomingOrders = () => {
  // Initial order data
  const initialOrders = [
    {
      id: '1',
      name: 'John Doe',
      address: '123 Main St, Anytown, USA',
      date: '24 May 2023, 10:30 AM',
      total: 99.99,
      status: 'Preparing', // New status property
      customerNote: 'Please deliver to back door',
      products: [
        { id: 'p1', title: ' Cheeseburger', price: 49.99, quantity: 2 },
      ],
    },
    // add more orders as needed
  ];

  const [orders, setOrders] = useState(initialOrders); // State for order data
  const [pageNumber, setPageNumber] = useState(0);

  const orderPerPage = 5;
  const visitedPage = pageNumber * orderPerPage;
  const displayPage = orders.slice(
    visitedPage,
    visitedPage + orderPerPage
  );

  const pageCount = Math.ceil(orders.length / orderPerPage);

  const changePage = ({ selected }) => {
    setPageNumber(selected);
  };

  const handleButtonClick = (id, status) => {
    const updatedOrders = orders.map(order => order.id === id ? {...order, status: status} : order);
    setOrders(updatedOrders);
  };

  return (
    <Helmet title="Incoming Orders">
      <CommonSection title="Incoming Orders" />

      <section>
        <Container>
          <Row>
            {displayPage.map((order, index) => (
              <Col lg="12" md="12" sm="6" xs="6" key={order.id} className="mb-4">
                <Card className="p-4">
                  <CardTitle tag="h3">
                      Order {index + 1}
                  </CardTitle>
                  <CardSubtitle className="mb-2 text-muted" tag="h6">
                      {order.date}
                  </CardSubtitle>
                  <h6>
                      Customer Name: {order.name}
                  </h6>
                  <h6>
                      Customer Note: {order.customerNote}
                  </h6>
                  <Table>
                    <thead>
                      <tr>
                        <th className="text-center">Product Title</th>
                        <th className="text-center">Price</th>
                        <th className="text-center">Quantity</th>
                      </tr>
                    </thead>
                    <tbody>
                      {order.products.map((product) => (
                        <Tr key={product.id} product={product} />
                      ))}
                    </tbody>
                  </Table>
                  <h6>
                      Subtotal: ${order.total.toFixed(2)}
                  </h6>
                  <h6>
                      Status: {order.status}
                  </h6>
                  {order.status === 'Preparing' &&
                            <div>
                            <Button color="primary" size="sm" className="mr-2" onClick={() => handleButtonClick(order.id, 'On the Road')}>
                              On the Road
                            </Button>
                            <Button color="danger" size="sm" className="mr-2" onClick={() => handleButtonClick(order.id, 'Canceled')}>
                              Cancel Order
                            </Button>
                          </div>
                  }
                  {order.status === 'On the Road' &&
                            <div>
               
                      <Button color="success" size="sm" className="mr-2" onClick={() => handleButtonClick(order.id, 'Delivered')}>
                        Delivered
                      </Button>
                      </div>
                  
               
                  }
                </Card>
              </Col>
            ))}

            <ReactPaginate
              pageCount={pageCount}
              onPageChange={changePage}
              previousLabel={"Prev"}
              nextLabel={"Next"}
              containerClassName=" paginationBttns "
            />
          </Row>
        </Container>
      </section>
    </Helmet>
  );
};

const Tr = ({ product }) => {
  const { title, price, quantity } = product;
  return (
    <tr>
      <td className="text-center">{title}</td>
      <td className="text-center">${price}</td>
      <td className="text-center">{quantity}</td>
    </tr>
  );
};

export default IncomingOrders;

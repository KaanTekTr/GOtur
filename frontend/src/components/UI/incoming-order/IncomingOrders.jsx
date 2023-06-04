import React, { useState } from "react";
import Helmet from "../../Helmet/Helmet";
import CommonSection from "../common-section/CommonSection";
import { Container, Row, Col, Card, Table, CardTitle, CardSubtitle, Button } from "reactstrap";
import ReactPaginate from "react-paginate";
import { getOrdersThunk, setPurchaseDepartedThunk } from "../../../store/user/orderSlice.js";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from 'react';

const IncomingOrders = (props) => {
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

  const userId = useSelector(state => state.auth.userId);
  console.log(userId)
  const dispatch = useDispatch();
  useEffect(() => {
     dispatch(getOrdersThunk({userId}));
   }, [dispatch, userId]);
 const waitingPurchases = useSelector(state => state.order.paidWaitingPurchases);
 console.log(waitingPurchases)

  //const [orders, setOrders] = useState(waitingPurchases); // State for order data
  const orders = waitingPurchases;
  console.log(orders)
  //console.log(orders)
  const [pageNumber, setPageNumber] = useState(0);

  const orderPerPage = 5;
  const visitedPage = pageNumber * orderPerPage;
  const displayPage = orders?.slice(
    visitedPage,
    visitedPage + orderPerPage
  );

  const pageCount = Math.ceil(orders.length / orderPerPage);

  const changePage = ({ selected }) => {
    setPageNumber(selected);
  };

  const handleButtonClick = (id, status) => {
    //dispatch(transferBalanceThunk({ group_id: group.group_id, customer_id: userId, balance: balance}));
    alert('Transfer Completed Successfully!');
  };

  const setDeparted = (id) => {
    dispatch(setPurchaseDepartedThunk({ purchase_id: id}));
    alert('Purchase Has Been Set As Departed!');
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
                      Restaurant: {order.restaurant.restaurant_name}
                  </h6>
                  <h6>
                      Customer Name: {order.customer.username}
                  </h6>
                  <h6>
                      Customer Note: {order.purchase.customer_note}
                  </h6>
                  <h8>
                      <strong>Address Information: </strong>
                      City: {order.address.city}, District: {order.address.district}, Street Num: {order.address.street_num}, 
                      Street Name: {order.address.street_name}, Building Num: {order.address.building_num}
                  </h8>
                  <h8>
                      <strong>Detailed Description: </strong> {order.address.detailed_desc}
                  </h8>
                  <Table>
                    <thead>
                      <tr>
                        <th className="text-center">Product Title</th>
                        <th className="text-center">Price</th>
                        <th className="text-center">Quantity</th>
                      </tr>
                    </thead> 
                    <tbody>
                        {order.purchaseItemList.map((product) => (
                                <Tr key={product.food.food_id} product={{title: product.food.food_name, price: product.food.price, quantity: product.food.food_order}} />
                            ))}
                    </tbody> 
                  </Table>
                  <h6>
                      Subtotal: ${order.purchase.total_price.toFixed(2)}
                  </h6>
                  <h6>
                      Status: {!order.purchase.is_departed && "Waiting"} {order.purchase.is_departed && !order.purchase.is_delivered && "On The Road"}
                      {order.purchase.is_departed && order.purchase.is_delivered && "Delivered"}
                  </h6>
                  {!order.purchase.is_departed && !order.purchase.is_delivered &&
                            <div>
                            <Button color="primary" size="sm" className="mr-2" onClick={() => setDeparted(order.purchase.purchase_id)}>
                              Set Departed
                            </Button>
                            <Button color="danger" size="sm" className="mr-2" onClick={() => handleButtonClick(order.id, 'Canceled')}>
                              Cancel Order
                            </Button>
                          </div>
                  }
                  {order.purchase.is_departed && !order.purchase.is_delivered &&
                            <div>
                      <Button color="success" size="sm" className="mr-2" onClick={() => handleButtonClick(order.id, 'Delivered')}>
                        Set Delivered
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

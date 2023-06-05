import React, { useEffect, useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Card, Table, CardTitle, CardSubtitle, Button, Input } from "reactstrap";

import ReactPaginate from "react-paginate";

import image01 from "../assets/images/bread.png"; 

import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useDispatch, useSelector } from "react-redux";
import { getOldPurchasesFoodsThunk, getOldPurchasesThunk } from "../store/user/orderSlice";

const PastOrders = () => {

  const pastOrders = useSelector((state) => state.order.pastOrders);
  const userId = useSelector((state) => state.auth.userId);

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

  const [reload, setReload] = useState(false);

  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(getOldPurchasesThunk({userId, dispatch}));
    setReload(r => !r);
    setReload(r => !r);
  }, [dispatch, userId]);

  return (
    <Helmet title="Past Orders">
      <CommonSection title="Past Orders" />

      <section>
        <Container>
          <Row>

            {displayPage?.map((item, index) => (
              <Col lg="12" md="12" sm="6" xs="6" key={item.id} className="mb-4">
                <Card className="p-4">
                    <CardTitle tag="h3">
                      {item.is_group_purchase ? "Group" : ""}  Order {index + 1}
                    </CardTitle>
                    <CardSubtitle
                        className="mb-2 text-muted"
                        tag="h6"
                    >
                        {item.purchase_time}
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
                        </tr>
                    </thead>
                    <tbody>
                        {item?.products?.map((item) => (
                        <Tr item={item} key={item.id} />
                        ))}
                    </tbody>
                    </Table>

                    <h6>
                        Subtotal: $
                        <span className="cart__subtotal">{item.total_price}</span>
                    </h6>
                    <h6>
                      Status: {!item.is_departed && "Waiting"} {item.is_departed && !item.is_delivered && "On The Road"}
                      {item.is_departed && item.is_delivered && "Delivered"}
                    </h6>

                    <Card className="p-2 mt-4">
                      <CardTitle tag="h5">
                          Customer Note
                      </CardTitle>
                        <span>{item.customer_note}</span>
                    </Card>

                    <Card className="p-2 mt-4">
                      <CardTitle tag="h5">
                          Review
                      </CardTitle>
                      {item.comment === "" ? (
                        <div>
                          <Input placeholder="Add comment..." className="mb-2"/>
                          <Button>Add Comment</Button>
                        </div>) :<span>{item.comment}</span>}
                    </Card>
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

export default PastOrders;

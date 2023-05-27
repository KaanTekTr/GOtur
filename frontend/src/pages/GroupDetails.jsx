import React, { useEffect, useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Card, CardTitle, Button, Modal, ModalHeader, ModalBody, ListGroup, ListGroupItem, ListGroupItemHeading, ListGroupItemText, ModalFooter, InputGroup, Input, InputGroupText } from "reactstrap";


import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate, useParams } from "react-router-dom";
import { addressActions } from "../store/user/adressSlice";
import { groupsActions } from "../store/group/groupSlice";
import { orderActions } from "../store/user/orderSlice";

const GroupDetails = () => {

    const groups = useSelector(state => state.groups.groups);
    const userId = useSelector(state => state.auth.userId);

    const { id } = useParams();
    const [group, setGroup] = useState(groups.filter(group => `${group.id}` === id)[0]);

    const groupAddresses = useSelector(state => state.address.groupAddress);
    const selectedGroupAddress = useSelector(state => state.address.selectedGroupAddress);

    const [modal, setModal] = useState(false);
    const [modalAddMoney, setModalAddMoney] = useState(false);
    const [modalGroupCart, setModalGroupCart] = useState(false);

    const toggle = () => setModal(!modal);
    const toggleAddMoney = () => setModalAddMoney(!modalAddMoney);
    const toggleGroupCart = () => setModalGroupCart(!modalGroupCart);

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const changeSelAddress = id => {
        dispatch(addressActions.changeSelectedGroupAddress({id}));
    }

    useEffect(() => {
      dispatch(groupsActions.getGroups({userId}));
      setGroup(groups.find(group => `${group.id}` === id))
    }, [groups, id, dispatch, userId])
    
    const seeRestaurants = id => {
      dispatch(orderActions.updateCurrentCart({id}));
      navigate("/restaurants");
    }
  return (
    <Helmet title={group.title}>
      <CommonSection title={group.title} />

      <section>
        <Container>
            <Row className="mb-4">
                <Col lg="3" md="3">
                    <h4>Balance: {group.balance}$</h4>
                </Col>
                <Col lg="2" md="2">
                    <Button color="primary" onClick={toggleAddMoney}>Add Money</Button>
                </Col>
                <Col lg="2" md="2">
                    <div>
                        <Button color="danger" onClick={toggle}>
                            Group Address
                        </Button>
                    </div>
                </Col>
                <Col lg="2" md="2">
                    <span style={{cursor:"pointer"}} className="cart__icon" onClick={toggleGroupCart}>
                        <i class="ri-shopping-basket-line"></i>
                        <span className="cart__badge_x">{group.groupTotalQuantity}</span>
                    </span>
                </Col>
            </Row>
            <Row>
                <h2 className="mb-4">Members</h2>
                {group?.members?.map((member, index) => (
                <Col lg="12" md="12" sm="6" xs="6" key={member.id} className="mb-4">
                    <Card className="p-4">
                        <Container>
                            <Row style={{display: "flex"}}>
                                <Col lg="10" md="10">
                                    <CardTitle tag="h3">
                                        {member.name}
                                    </CardTitle>
                                </Col>
                            </Row>
                        </Container>
                    </Card>
                </Col>
                ))}
            </Row>
            <Row>
              <Col>
                <Button onClick={() => seeRestaurants(group.id)}>Restaurants</Button>
              </Col>
            </Row>
            {/** GROUP ADDRESS SELECTION MODAL */}
            <Modal className="modal-x" isOpen={modal} toggle={toggle} >
              <ModalHeader toggle={toggle}>Group Address Selection</ModalHeader>
              <ModalBody>   
                <ListGroup>
                  {groupAddresses.length > 0 ? groupAddresses.map(address => (
                    <ListGroupItem style={{cursor: "pointer"}} active={address.id === selectedGroupAddress.id} onClick={() => changeSelAddress(address.id)}>
                      <ListGroupItemHeading>
                        {address.title}
                      </ListGroupItemHeading>
                      <ListGroupItemText>
                        {address.desc}
                      </ListGroupItemText>
                    </ListGroupItem>
                  )) : <span>No Address To Select</span>}
                </ListGroup>
              </ModalBody>
              <ModalFooter>
                <Button color="secondary" onClick={toggle}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
            {/** ADD MONEY TO GROUP BALANCE MODAL */}
            <Modal className="modal-x" isOpen={modalAddMoney} toggle={toggleAddMoney} >
              <ModalHeader toggle={toggleAddMoney}>Add Money</ModalHeader>
              <ModalBody>   
                <Container>
                  <Row>
                    <Col>
                      My Balance: 100$
                    </Col>
                    <Col>
                      <InputGroup>
                        <Input type="number" placeholder="1500" />
                        <InputGroupText>
                          $
                        </InputGroupText>
                      </InputGroup>
                      <Button color="danger" className="mt-4">Transfer Money</Button>
                    </Col>
                    <Col>
                      Group Balance: 300$
                    </Col>
                  </Row>
                </Container>
              </ModalBody>
              <ModalFooter>
                <Button color="secondary" onClick={toggleAddMoney}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
             {/** ADD MONEY TO GROUP BALANCE MODAL */}
             <Modal className="modal-y" isOpen={modalGroupCart} toggle={toggleGroupCart} style={{ width: "%70"}} >
              <ModalHeader toggle={toggleGroupCart}>Group Cart</ModalHeader>
              <ModalBody>   
              <Container>
                <Row>
                  <Col lg="12">
                    {group.groupTotalQuantity === 0 ? (
                      <h5 className="text-center">Your cart is empty</h5>
                    ) : (
                      <table className="table table-bordered">
                        <thead>
                          <tr>
                            <th>Image</th>
                            <th>Product Title</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Delete</th>
                          </tr>
                        </thead>
                        <tbody>
                          {group.groupCartItems.map((item) => (
                            <Tr item={item} groupId={group.id} key={item.id} />
                          ))}
                        </tbody>
                      </table>
                    )}

                    <div className="mt-4">
                      <h6>
                        Subtotal: $
                        <span className="cart__subtotal">{group.groupTotalAmount}</span>
                      </h6>
                      <p>Taxes and shipping will calculate at checkout</p>
                      <div className="cart__page-btn">
                        <button className="addTOCart__btn me-4">
                          <Link to="/foods">Continue Shopping</Link>
                        </button>
                        <button className="addTOCart__btn">
                          <Link to="/checkout">Proceed to checkout</Link>
                        </button>
                      </div>
                    </div>
                  </Col>
                </Row>
              </Container>
              </ModalBody>
              <ModalFooter>
                <Button color="secondary" onClick={toggleGroupCart}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
        </Container>
      </section>
    </Helmet>
  );
};


const Tr = (props) => {
  const { id, image01, title, price, quantity } = props.item;
  const groupId = props.groupId;
  const dispatch = useDispatch();

  const deleteItem = () => {
    dispatch(groupsActions.deleteItem({id, groupId}));
  };
  return (
    <tr>
      <td className="text-center cart__img-box">
        <img src={image01} alt="" />
      </td>
      <td className="text-center">{title}</td>
      <td className="text-center">${price}</td>
      <td className="text-center">{quantity}px</td>
      <td className="text-center cart__item-del">
        <i class="ri-delete-bin-line" onClick={deleteItem}></i>
      </td>
    </tr>
  );
};

export default GroupDetails;

import React, { useEffect, useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Card, CardTitle, Button, Modal, ModalHeader, ModalBody, ListGroup, ListGroupItem, ListGroupItemHeading, ListGroupItemText, ModalFooter, InputGroup, Input, InputGroupText, Dropdown, DropdownToggle, DropdownMenu, DropdownItem, Table, Alert } from "reactstrap";

import crown_image from "../assets/images/crown.jpeg"
import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate, useParams } from "react-router-dom";
import { addressActions } from "../store/user/adressSlice";
import { addGroupMemberThunk, getGroupMembersThunk, getGroupsThunk, groupsActions, transferBalanceThunk } from "../store/group/groupSlice";
import { completeGroupPurchaseThunk, deleteFoodFromGroupPurchaseThunk, getProductsUnpaidGroupPurchaseThunk, getUnpaidGroupPurchaseThunk, orderActions } from "../store/user/orderSlice";
import { getFriendsThunk } from "../store/group/friendsSlice";
import { getUserThunk } from "../store/authSlice";

import image01 from "../assets/images/bread.png"; 

const GroupDetails = () => {

    const groups = useSelector(state => state.groups.groups);
    const groupCart = useSelector(state => state.order.unpaidGroupPurchase);
    const friends = useSelector(state => state.friends.friends);
    const userId = useSelector(state => state.auth.userId);

    const { id } = useParams();
    const [group, setGroup] = useState(groups.filter(group => `${group.group_id}` === id)[0]);

    const groupAddresses = useSelector(state => state.address.groupAddress);
    const selectedGroupAddress = useSelector(state => state.address.selectedGroupAddress);

    const userDispatch = useDispatch();
    useEffect(() => {
        userDispatch(getUserThunk({authType: "customer", userId}));
     }, [userDispatch, userId]);
   const user = useSelector(state => state.auth.user);
   console.log(user)

    const [modal, setModal] = useState(false);
    const [modalAddMoney, setModalAddMoney] = useState(false);
    const [modalGroupCart, setModalGroupCart] = useState(false);
    const [modalAddMember, setModalAddMember] = useState(false);
    const [modalGroupCheckout, setModalGroupCheckout] = useState(false);
    const [reload, setReload] = useState(false);
    const [reload2, setReload2] = useState(false);

    const toggle = () => setModal(!modal);
    const toggleAddMoney = () => setModalAddMoney(!modalAddMoney);
    const toggleGroupCart = () => setModalGroupCart(!modalGroupCart);
    const toggleAddMember = () => setModalAddMember(!modalAddMember);
    const toggleGroupCheckout = () => setModalGroupCheckout(!modalGroupCheckout);

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const changeSelAddress = id => {
        dispatch(addressActions.changeSelectedGroupAddress({id}));
    }

    useEffect(() => {
      dispatch(getGroupsThunk({userId}));
      dispatch(getFriendsThunk({userId}));
     
      setReload(r => !r);
      setReload(r => !r);
    }, [id, dispatch, userId]);

    useEffect(() => {
      if (group) {
        dispatch(getUnpaidGroupPurchaseThunk({userId: group.group_id}));
        setTimeout(() => {
          dispatch(getProductsUnpaidGroupPurchaseThunk({userId: group.group_id}));
        }, 100);
      }
    }, [group, dispatch, reload2]);

    useEffect(() => {
      dispatch(getGroupMembersThunk({group_id: id}));
    }, [id, dispatch]);

    useEffect(() => {
      setGroup(groups.find(group => `${group.group_id}` === id));
    }, [groups, id, reload]);
    
    const seeRestaurants = id => {
      dispatch(orderActions.updateCurrentCart({id}));
      navigate("/restaurants");
    }

    const groupCheckout = () => {
      toggleGroupCart();
      toggleGroupCheckout();
    }

    const [dropdownOpen, setDropdownOpen] = useState(false);
    const [selectedFriend, setSelectedFriend] = useState(0);

    const toggleDropDown = () => setDropdownOpen((prevState) => !prevState);
    const addMember = () => {
      if (selectedFriend) {
        dispatch(addGroupMemberThunk({group_id: group.group_id, customer_id: selectedFriend}));
        dispatch(getGroupMembersThunk({group_id: id}));
        toggleAddMember();
      } else {
        console.error("Choose a friend!");
      }
    }

    const [note, setNote] = useState("");
    const [balance, setBalance] = useState(0);


    const [info, setInfo] = useState("");
    const [visible, setVisible] = useState(false);
    const onDismiss = () => setVisible(false);

    const handlePayment = () => {
      dispatch(completeGroupPurchaseThunk({ setInfo, setVisible,purchase_id: groupCart.purchase_id, address_id: groupAddresses.find(ad => ad.is_primary)?.address_id, note, coupon_id: -1}))
      setTimeout(() => {
        dispatch(getUnpaidGroupPurchaseThunk({userId: group.group_id}));
        dispatch(getProductsUnpaidGroupPurchaseThunk({userId: group.group_id}));
        dispatch(getGroupsThunk({userId}));
        setGroup(groups.find(group => `${group.group_id}` === id));

      }, 200);
      toggleGroupCheckout();
      
      //navigate(`/payment`)
    }
    const [reload3, setReload3] = useState(false);

    // Function to handle deleting a product
    const transferBalance = () => {
        dispatch(transferBalanceThunk({ group_id: group.group_id, customer_id: userId, balance: balance}));
        alert('Transfer Completed Successfully!');
        setTimeout(() => {
            dispatch(getGroupsThunk({userId}));
            setGroup(groups.find(group => `${group.group_id}` === id));
        }, 200);
        setReload3(!reload3);
        setReload3(!reload3);
        toggleAddMoney();
    }
  return (
    <>
      {group ? (
    <Helmet title={group.group_name}>
      <CommonSection title={group.group_name} />

      <section>
        <Container>
            <Row className="mb-4">
                <Col lg="3" md="3">
                    <h4>Balance: {group.group_balance}$</h4>
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
                        <span className="cart__badge_x">{groupCart?.products?.length || 0}</span>
                    </span>
                </Col>
            </Row>
            <Row>
                <h2 className="mb-4">Members</h2>
                <Col>
                  <Button color="success" className="mb-4" onClick={toggleAddMember}>Add Member</Button>
                </Col>
                {group?.members?.map((member, index) => (
                <Col lg="12" md="12" sm="6" xs="6" key={member.user_id} className="mb-4">
                    <Card className="p-4">
                        <Container>
                            <Row style={{display: "flex"}}>
                                <Col lg="10" md="10">
                                    <CardTitle tag="h3">
                                      { group.group_owner_id === member.user_id ? <img src={crown_image} alt="crown" style={{width: "40px", height: "40px", marginRight :"5px"}} /> : null }
                                        {member.username}
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
                <Button onClick={() => seeRestaurants(group.group_id)}>Restaurants</Button>
              </Col>
            </Row>
            {/** GROUP ADDRESS SELECTION MODAL */}
            <Modal className="modal-x" isOpen={modal} toggle={toggle} >
              <ModalHeader toggle={toggle}>Group Address Selection</ModalHeader>
              <ModalBody>   
                <ListGroup>
                  {groupAddresses?.length > 0 ? groupAddresses.map(address => (
                    <ListGroupItem style={{cursor: "pointer"}} active={address.is_primary} onClick={() => changeSelAddress(address.address_id)}>
                      <ListGroupItemHeading>
                        {address.address_name}
                      </ListGroupItemHeading>
                      <ListGroupItemText>
                        {address.street_name + " street, street no: "+address.street_num + ", " + address.district + "/" + address.city}
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
                      My Balance: {user.balance}$
                    </Col>
                    <Col>
                      <InputGroup>
                        <Input type="number" value={balance} onChange={e => setBalance(e.target.value)} placeholder="1500" />
                        <InputGroupText>
                          $
                        </InputGroupText>
                      </InputGroup>
                      <Button color="danger" className="mt-4" onClick={transferBalance}>Transfer Money</Button>
                    </Col>
                    <Col>
                      Group Balance: {group.group_balance}$
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
             {/** GROUP CART MODAL */}
             <Modal className="modal-y" isOpen={modalGroupCart} toggle={toggleGroupCart} style={{ width: "%70"}} >
              <ModalHeader toggle={toggleGroupCart}>Group Cart</ModalHeader>
              <ModalBody>   
              <Container>
                <Row>
                  <Col lg="12">
                    {groupCart?.products?.length === 0 ? (
                      <h5 className="text-center">Your cart is empty</h5>
                    ) : (
                      <table className="table table-bordered">
                        <thead>
                          <tr>
                            <th>Image</th>
                            <th>Product Title</th>
                            <th>Price</th>
                            <th>Delete</th>
                          </tr>
                        </thead>
                        <tbody>
                          {groupCart?.products?.map((item) => (
                            <Tr item={item} groupId={group.group_id} key={item.id} setReload2={setReload2}/>
                          ))}
                        </tbody>
                      </table>
                    )}

                    <div className="mt-4">
                      <h6>
                        Subtotal: $
                        <span className="cart__subtotal">{groupCart.total_price}</span>
                      </h6>
                      <p>Taxes and shipping will calculate at checkout</p>
                      <div className="cart__page-btn">
                        <button className="addTOCart__btn me-4" onClick={() => seeRestaurants(group.group_id)}>
                          Continue Shopping
                        </button>
                        <button className="addTOCart__btn" onClick={groupCheckout}>
                          Proceed to checkout
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
            {/** GROUP CHECKOUT MODAL */}
            <Modal className="modal-y" isOpen={modalGroupCheckout} toggle={toggleGroupCheckout} style={{ width: "%70"}} >
              <ModalHeader toggle={toggleGroupCheckout}>Group Checkout</ModalHeader>
              <ModalBody>   
              <Container>
                <Row>
                  <Col lg="8" md="6">
                    <Card className="p-4">
                      <CardTitle tag="h3">
                          Order 
                      </CardTitle>
                    
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
                              {groupCart.products?.map((item) => (
                                <Tr item={item} key={item.id} checkout={true} />
                              ))}
                          </tbody>
                          </Table>
                    <Card className="p-4 mt-4">
                      <CardTitle tag="h5">
                          Customer Note
                      </CardTitle>
                        <div>
                          <Input placeholder="Add note..." value={note} onChange={e=>setNote(e.target.value)}  className="mb-2"/>
                        </div>
                        <div>
                          <button type="submit" className="addTOCart__btn" onClick={handlePayment}>
                            Payment
                          </button>
                        </div>
                    </Card>
                  </Card>
                  </Col>

                  <Col lg="4" md="6">
                    <div className="checkout__bill">
                      <h6 className="d-flex align-items-center justify-content-between mb-3">
                        Subtotal: <span>${groupCart.total_price}</span>
                      </h6>
                      <h6 className="d-flex align-items-center justify-content-between mb-3">
                        Shipping: <span>${0}</span>
                      </h6>
                      <div className="checkout__total">
                        <h5 className="d-flex align-items-center justify-content-between">
                          Total: <span>${groupCart.total_price}</span>
                        </h5>
                      </div>
                    </div>
                  </Col>
                </Row>
              </Container>
              </ModalBody>
              <ModalFooter>
                <Button color="secondary" onClick={toggleGroupCheckout}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
            {/** ADD MEMBER TO GROUP BALANCE MODAL */}
            <Modal className="modal-x" isOpen={modalAddMember} toggle={toggleAddMember} >
              <ModalHeader toggle={toggleAddMember}>Add Member</ModalHeader>
              <ModalBody>   
                <Container>
                  <Row>
                    <Col lg="4" md="4"></Col>
                    <Col>
                      <Dropdown isOpen={dropdownOpen} toggle={toggleDropDown} >
                        <DropdownToggle caret>{friends.find(f => f.user_id === selectedFriend)?.username || "Selec Friend"}</DropdownToggle>
                        <DropdownMenu>
                          {friends?.map((friend, index) => (
                            <DropdownItem onClick={() => setSelectedFriend(friend.user_id)} key={friend.user_id}>{friend.username}</DropdownItem>
                          ))}
                        </DropdownMenu>
                      </Dropdown>
                    </Col>
                  </Row>
                  <Row>
                    <Col lg="4" md="4"></Col>
                    <Col>
                      <Button className="mt-4" color="primary" onClick={addMember}>
                        Add Member
                      </Button>
                    </Col>
                  </Row>
                </Container>
              </ModalBody>
              <ModalFooter>
                <Button color="secondary" onClick={toggleAddMember}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
        </Container>
      </section>
      <Alert style={{ position:"fixed", bottom: "30px",  right:"30px"}} color="danger" isOpen={visible} toggle={onDismiss}>
          {info}
      </Alert>
    </Helmet>
    ) : <span>Loading...</span>}
    </>
  );
};


const Tr = (props) => {
  const { food_id, food_name, price } = props.item.food;
  const groupId = props.groupId;
  const dispatch = useDispatch();

  const deleteItem = () => {
    dispatch(deleteFoodFromGroupPurchaseThunk({group_id: groupId, food_id, food_order: props.item.food_order}));
    setTimeout(function(){
        console.log("Executed after 1 second");
        dispatch(getUnpaidGroupPurchaseThunk({userId: groupId}));
        dispatch(getProductsUnpaidGroupPurchaseThunk({userId: groupId}));
    }, 500);
  };
  return (
    <tr>
      <td className="text-center cart__img-box">
        <img src={image01} alt="" />
      </td>
      <td className="text-center">{food_name}</td>
      <td className="text-center">${price}</td>
      {props.checkout ? null : (
        <td className="text-center cart__item-del">
          <i class="ri-delete-bin-line" onClick={deleteItem}></i>
        </td>
      )}
    </tr>
  );
};

export default GroupDetails;

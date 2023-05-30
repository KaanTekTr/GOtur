import React, { useRef, useEffect, useState } from "react";

import { Container, Button, Modal, ModalHeader, ModalBody, ModalFooter, ListGroup, ListGroupItem, ListGroupItemHeading, ListGroupItemText, List, Row, Col, InputGroup, InputGroupText } from 'reactstrap';
import logo from "../../assets/images/res-logo.png";
import { NavLink, Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";

import { cartUiActions } from "../../store/shopping-cart/cartUiSlice";

import "../../styles/header.css";
import { authActions, logoutThunk } from "../../store/authSlice";
import { addAddressesThunk, addressActions, getAddressesThunk } from "../../store/user/adressSlice";
import { orderActions } from "../../store/user/orderSlice";
import { Drawer, Input, ListItem, ListItemIcon, ListItemText } from "@material-ui/core";

import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import StorefrontIcon from '@material-ui/icons/Storefront';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import LocalShippingIcon from '@material-ui/icons/LocalShipping';
import HistoryIcon from '@material-ui/icons/History';

const nav__links = [
  {
    display: "Home",
    path: "/home",
  },
  {
    display: "Restaurants",
    path: "/restaurants",
  },
  {
    display: "Contact",
    path: "/contact",
  },
];


const ownerLinks = [
  { display: 'My Balance', path: '/balance', icon: <AccountCircleIcon /> },
  { display: 'My Profile', path: '/profile', icon: <AccountCircleIcon /> },
  { display: 'My Restaurant', path: '/restaurant-info', icon: <StorefrontIcon /> },
  { display: 'Incoming Orders', path: '/restaurantOwnerHome', icon: <LocalShippingIcon /> },
  { display: 'Past Orders', path: '/restaurantPastOrders', icon: <HistoryIcon /> },
  { display: 'Logout', path: '/login', icon: <ExitToAppIcon /> },
];

const customerLinks = [
  { display: 'My Balance', path: '/customer-balance', icon: <AccountCircleIcon /> },
  { display: 'My Profile', path: '/profile', icon: <AccountCircleIcon /> },
  { display: 'Cart', path: '/cart', icon: <HistoryIcon /> },
  { display: 'Past Orders', path: '/pastOrders', icon: <HistoryIcon /> },
  { display: 'My Coupons', path: '/coupons', icon: <HistoryIcon /> },
  { display: 'Groups', path: '/groups', icon: <StorefrontIcon /> },
  { display: 'Friends', path: '/friends', icon: <LocalShippingIcon /> },
  { display: 'Logout', path: '/login', icon: <ExitToAppIcon /> },
];

const Header = () => {
  const [modal, setModal] = useState(false);
  const [addAddressModal, setAddAddressModal] = useState(false);
  const [drawerOpen, setDrawerOpen] = useState(false);

  const [title, setTitle] = useState("");
  const [city, setCity] = useState("");
  const [district, setDistrict] = useState("");
  const [streetName, setStreetName] = useState("");
  const [streetNo, setStreetNo] = useState("");
  const [buildingNo, setBuildingNo] = useState("");
  const [desc, setDesc] = useState("");
  
  const toggleDrawer = () => {
    setDrawerOpen(!drawerOpen);
  };
  const toggle = () => setModal(!modal);
  const addAddressToggle = () => setAddAddressModal(!addAddressModal);

  const menuRef = useRef(null);
  const headerRef = useRef(null);
  const totalQuantity = useSelector((state) => state.cart.totalQuantity);
  const dispatch = useDispatch();

  const addresses = useSelector(state => state.address.address);
  const selectedAddress = useSelector(state => state.address.selectedAddress);
  const authType = useSelector(state => state.auth.authType);
  const userId = useSelector(state => state.auth.userId);

  useEffect(() => {
    dispatch(getAddressesThunk({userId}));
  },[userId, dispatch])

  const toggleMenu = () => menuRef.current?.classList.toggle("show__menu");

  const toggleCart = () => {
    dispatch(cartUiActions.toggle());
  };

  const logOut = () => {
    dispatch(logoutThunk({userId, authType}));
  }

  const eventHandler = () => {
    // Your event handling logic goes here
    // For example, you can log a message when the scroll event occurs
    console.log("Scroll event occurred");
  };

  const changeSelAddress = id => {
    dispatch(addressActions.changeSelectedAddress({id}));
  }

  useEffect(() => {
    window.addEventListener("scroll", () => {
      if (
        document.body.scrollTop > 80 ||
        document.documentElement.scrollTop > 80
      ) {
        headerRef?.current?.classList.add("header__shrink");
      } else {
        headerRef?.current?.classList.remove("header__shrink");
      }
    });

    return () => window.removeEventListener("scroll", eventHandler);
  }, []);

  const linkClicked = item => {
    if (item.display === "Restaurants") {
      dispatch(orderActions.updateCurrentCart({id: 0}));
    }
  }

  const addNewAdress = () => {
    console.log("New adress");
    const address = {
      address_name: title,
      is_primary: false,
      city,
      district,
      street_num: streetNo,
      street_name: streetName,
      building_num: buildingNo,
      detailed_desc: desc, 
    };
    dispatch(addAddressesThunk({userId, address}));
    dispatch(getAddressesThunk({userId}));
    addAddressToggle();
  }

  return (
    <header className="header" ref={headerRef}>
      <Container>
        <div className="nav__wrapper d-flex align-items-center justify-content-between">
          <Button color="secondary " onClick={toggleDrawer}>
            <i className="ri-menu-line"></i>
          </Button>
          <div className="logo">
            <img src={logo} alt="logo" />
            <h5>GOtur</h5>
          </div>

          {/* ======= menu ======= */}
          <div className="navigation" ref={menuRef} onClick={toggleMenu}>
            <div className="menu d-flex align-items-center gap-5">
              {nav__links.map((item, index) => (
                <NavLink
                  to={item.path}
                  key={index}
                  className={(navClass) =>
                    navClass.isActive ? "active__menu" : ""
                  }
                  onClick={() => linkClicked(item)}
                >
                  {item.display}
                </NavLink>
              ))}
            </div>
          </div>

          <div>
            <Button color="danger" onClick={toggle}>
              Address
            </Button>
            <Modal className="modal-x" isOpen={modal} toggle={toggle} >
              <ModalHeader toggle={toggle}>Address Selection</ModalHeader>
              <ModalBody>   
                <ListGroup>
                  {addresses?.length > 0 ? addresses.map(address => (
                    <ListGroupItem style={{cursor: "pointer"}} active={address.address_id === selectedAddress.id} onClick={() => changeSelAddress(address.address_id)}>
                      <ListGroupItemHeading>
                        {address.address_name}
                      </ListGroupItemHeading>
                      <ListGroupItemText>
                        {address.street_name + " street, street no: "+address.street_num + ", " + address.district + "/" + address.city}
                      </ListGroupItemText>
                    </ListGroupItem>
                  )) : <span>No Address To Select</span>}
                </ListGroup>
                <Button className="mt-2" color="primary" onClick={addAddressToggle}>
                  Add New Address
                </Button>
              </ModalBody>
              <ModalFooter>
                <Button color="secondary" onClick={toggle}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
            <Modal className="modal-x" isOpen={addAddressModal} toggle={addAddressToggle} >
              <ModalHeader toggle={addAddressToggle}>Add New Address</ModalHeader>
              <ModalBody>   
              <Container>
                <Row className="mb-2">
                  <Col>
                    <InputGroup>
                      <InputGroupText>Address Title</InputGroupText>
                      <Input value={title} onChange={e => setTitle(e.target.value)} placeholder=" Home" />
                    </InputGroup>
                  </Col>
                </Row>
                <Row className="mb-2">
                  <Col>
                    <InputGroup>
                      <InputGroupText>Street Name</InputGroupText>
                      <Input value={streetName} onChange={e => setStreetName(e.target.value)} placeholder=" X Street" />
                    </InputGroup>
                  </Col>
                </Row>
                <Row className="mb-2">
                  <Col>
                    <InputGroup>
                      <InputGroupText>Street No</InputGroupText>
                      <Input value={streetNo} onChange={e => setStreetNo(e.target.value)} placeholder=" 5" />
                    </InputGroup>
                  </Col>
                </Row>
                <Row className="mb-2">
                  <Col>
                    <InputGroup>
                      <InputGroupText>Building No</InputGroupText>
                      <Input value={buildingNo} onChange={e => setBuildingNo(e.target.value)} placeholder=" 21" />
                    </InputGroup>
                  </Col>
                </Row>
                <Row className="mb-2">
                  <Col>
                    <InputGroup>
                      <InputGroupText>District</InputGroupText>
                      <Input value={district} onChange={e => setDistrict(e.target.value)} placeholder=" Çankaya" />
                    </InputGroup>
                  </Col>
                </Row>
                <Row className="mb-2">
                  <Col>
                    <InputGroup>
                      <InputGroupText>City</InputGroupText>
                      <Input value={city} onChange={e => setCity(e.target.value)} placeholder=" Ankara" />
                    </InputGroup>
                  </Col>
                </Row>
                <Row className="mb-2">
                  <Col>
                    <InputGroup>
                      <InputGroupText>Detailed Description</InputGroupText>
                      <Input value={desc} onChange={e => setDesc(e.target.value)} placeholder=" Behind the X Mall" />
                    </InputGroup>
                  </Col>
                </Row>
                
            </Container>
              </ModalBody>
              <ModalFooter>
                <Button color="secondary" onClick={addNewAdress}>
                  Add
                </Button>{"  "}
                <Button color="secondary" onClick={addAddressToggle}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
          </div>

          {/* ======== nav right icons ========= */}
          <div className="nav__right d-flex align-items-center gap-4">
            <span className="cart__icon" onClick={toggleCart}>
              <i class="ri-shopping-basket-line"></i>
              <span className="cart__badge">{totalQuantity}</span>
            </span>

            <span className="user" onClick={logOut}>
              <Link to="/login">
                <i class="ri-user-line"></i>
              </Link>
            </span>

            <span className="mobile__menu" onClick={toggleMenu}>
              <i class="ri-menu-line"></i>
            </span>
          </div>
        </div>
      </Container>
      <Drawer anchor="left" open={drawerOpen} onClose={toggleDrawer}>
        <List>
          {(authType === "customer" ? customerLinks : ownerLinks).map((link, index) => (
            <ListItem button key={index} onClick={toggleDrawer} component={Link} to={link.path}>
              <ListItemIcon>{link.icon}</ListItemIcon>
              <ListItemText primary={link.display} />
            </ListItem>
          ))}
        </List>
      </Drawer>
    </header>
  );
};

export default Header;

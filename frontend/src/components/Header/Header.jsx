import React, { useRef, useEffect, useState } from "react";

import { Container, Button, Modal, ModalHeader, ModalBody, ModalFooter, ListGroup, ListGroupItem, ListGroupItemHeading, ListGroupItemText, List } from 'reactstrap';
import logo from "../../assets/images/res-logo.png";
import { NavLink, Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";

import { cartUiActions } from "../../store/shopping-cart/cartUiSlice";

import "../../styles/header.css";
import { authActions } from "../../store/authSlice";
import { addressActions } from "../../store/user/adressSlice";
import { orderActions } from "../../store/user/orderSlice";
import { Drawer, ListItem, ListItemIcon, ListItemText } from "@material-ui/core";

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
  { display: 'Past Orders', path: '/pastOrders', icon: <HistoryIcon /> },
  { display: 'Logout', path: '/login', icon: <ExitToAppIcon /> },
];

const customerLinks = [
  { display: 'My Balance', path: '/balance', icon: <AccountCircleIcon /> },
  { display: 'My Profile', path: '/profile', icon: <AccountCircleIcon /> },
  { display: 'Cart', path: '/cart', icon: <HistoryIcon /> },
  { display: 'Past Orders', path: '/pastOrders', icon: <HistoryIcon /> },
  { display: 'Groups', path: '/groups', icon: <StorefrontIcon /> },
  { display: 'Friends', path: '/friends', icon: <LocalShippingIcon /> },
  { display: 'Logout', path: '/login', icon: <ExitToAppIcon /> },
];

const Header = () => {
  const [modal, setModal] = useState(false);
  const [drawerOpen, setDrawerOpen] = useState(false);
  
  const toggleDrawer = () => {
    setDrawerOpen(!drawerOpen);
  };
  const toggle = () => setModal(!modal);
  const menuRef = useRef(null);
  const headerRef = useRef(null);
  const totalQuantity = useSelector((state) => state.cart.totalQuantity);
  const dispatch = useDispatch();

  const addresses = useSelector(state => state.address.address);
  const selectedAddress = useSelector(state => state.address.selectedAddress);
  const authType = useSelector(state => state.auth.authType);

  useEffect(() => {
    dispatch(addressActions.getAddresses());
  })

  const toggleMenu = () => menuRef.current?.classList.toggle("show__menu");

  const toggleCart = () => {
    dispatch(cartUiActions.toggle());
  };

  const logOut = () => {
    dispatch(authActions.logout());
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
                  {addresses.length > 0 ? addresses.map(address => (
                    <ListGroupItem style={{cursor: "pointer"}} active={address.id === selectedAddress.id} onClick={() => changeSelAddress(address.id)}>
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
                <Button color="primary" onClick={toggle}>
                  Do Something
                </Button>{' '}
                <Button color="secondary" onClick={toggle}>
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

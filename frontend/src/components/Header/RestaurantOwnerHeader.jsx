import React, { useRef, useEffect, useState } from "react";
import { Container, Button, Modal, ModalHeader, ModalBody, ModalFooter, ListGroup, ListGroupItem, ListGroupItemHeading, ListGroupItemText } from 'reactstrap';
import logo from "../../assets/images/res-logo.png";
import { NavLink, Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { Drawer, List, ListItem, ListItemIcon, ListItemText } from "@material-ui/core";
import HomeIcon from '@material-ui/icons/Home';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import StorefrontIcon from '@material-ui/icons/Storefront';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import LocalShippingIcon from '@material-ui/icons/LocalShipping';
import HistoryIcon from '@material-ui/icons/History';

import { cartUiActions } from "../../store/shopping-cart/cartUiSlice";
import { authActions } from "../../store/authSlice";
import { addressActions } from "../../store/user/adressSlice";
import { orderActions } from "../../store/user/orderSlice";

const ownerLinks = [
  { display: 'My Account', path: '/account', icon: <AccountCircleIcon /> },
  { display: 'My Profile', path: '/profile', icon: <AccountCircleIcon /> },
  { display: 'My Restaurant', path: '/my-restaurant', icon: <StorefrontIcon /> },
  { display: 'Incoming Orders', path: '/incoming-orders', icon: <LocalShippingIcon /> },
  { display: 'Past Orders', path: '/past-orders', icon: <HistoryIcon /> },
  { display: 'Logout', path: '/login', icon: <ExitToAppIcon /> },
];

const RestaurantOwnerHeader = () => {
  const [modal, setModal] = useState(false);
  const [drawerOpen, setDrawerOpen] = useState(false);
  const headerRef = useRef(null);
  const dispatch = useDispatch();


  // Your existing code...
  const toggle = () => setModal(!modal);

  const toggleDrawer = () => {
    setDrawerOpen(!drawerOpen);
  };
  const eventHandler = () => {
    // Your event handling logic goes here
    // For example, you can log a message when the scroll event occurs
    console.log("Scroll event occurred");
  };

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

  return (
    <header className="header" ref={headerRef}>
      <Container>
        <div className="nav__wrapper d-flex align-items-center justify-content-between">
          <div>
            <Button color="primary" onClick={toggleDrawer}>
              <i className="ri-menu-line"></i>
            </Button>
            <Drawer anchor="left" open={drawerOpen} onClose={toggleDrawer}>
              <List>
                {ownerLinks.map((link, index) => (
                  <ListItem button key={index} onClick={toggleDrawer} component={Link} to={link.path}>
                    <ListItemIcon>{link.icon}</ListItemIcon>
                    <ListItemText primary={link.display} />
                  </ListItem>
                ))}
              </List>
            </Drawer>
          </div>

        </div>
      </Container>
    </header>
  );
};

export default RestaurantOwnerHeader;

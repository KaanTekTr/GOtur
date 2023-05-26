import React, { useRef, useEffect, useState } from "react";

import { Container, Button, Modal, ModalHeader, ModalBody, ModalFooter, ListGroup, ListGroupItem, ListGroupItemHeading, ListGroupItemText } from 'reactstrap';
import logo from "../../assets/images/res-logo.png";
import { NavLink, Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";

import { cartUiActions } from "../../store/shopping-cart/cartUiSlice";

import "../../styles/header.css";
import { authActions } from "../../store/authSlice";
import { addressActions } from "../../store/user/adressSlice";

const nav__links = [
  {
    display: "Home",
    path: "/home",
  },
  {
    display: "Foods",
    path: "/foods",
  },
  {
    display: "Restaurants",
    path: "/restaurants",
  },
  {
    display: "Groups",
    path: "/groups",
  },
  {
    display: "Past Orders",
    path: "/pastOrders",
  },
  {
    display: "Cart",
    path: "/cart",
  },
  {
    display: "Contact",
    path: "/contact",
  },
];

const Header = () => {
  const [modal, setModal] = useState(false);

  const toggle = () => setModal(!modal);
  const menuRef = useRef(null);
  const headerRef = useRef(null);
  const totalQuantity = useSelector((state) => state.cart.totalQuantity);
  const dispatch = useDispatch();

  const addresses = useSelector(state => state.address.address);
  const selectedAddress = useSelector(state => state.address.selectedAddress);

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

  return (
    <header className="header" ref={headerRef}>
      <Container>
        <div className="nav__wrapper d-flex align-items-center justify-content-between">
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
    </header>
  );
};

export default Header;

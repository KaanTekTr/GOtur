import React, { useState } from "react";

import "../../../styles/product-card.css";

import { Link, useNavigate } from "react-router-dom";

import { useDispatch, useSelector } from "react-redux";
import { cartActions } from "../../../store/shopping-cart/cartSlice";
import { Button, ListGroup, ListGroupItem, ListGroupItemHeading, Modal, ModalBody, ModalFooter, ModalHeader } from "reactstrap";
import { groupsActions } from "../../../store/group/groupSlice";

const ProductCard = (props) => {
  const { id, title, image01, price } = props.item;
  const dispatch = useDispatch();

  const addToCart = () => {
    if (selectedCart === 0) {
      dispatch(
        cartActions.addItem({
          id,
          title,
          image01,
          price,
        })
      );
    } else {
      dispatch(
        groupsActions.addItem({ 
          newItem: {
            id,
            title,
            image01,
            price,
          },
          groupId: selectedCart
        })
      );
    }
  };

  const navigate = useNavigate();
  const navToFood = () => {
    navigate(`/foods/${id}`)
  }

  const [modal, setModal] = useState(false);
  const toggle = () => setModal(!modal);

  const groups = useSelector(state => state.groups.groups);

  const [selectedCart, setSelectedCart] = useState(0);


  return (
    <div className="product__item">
      <div className="product__img" onClick={navToFood}>
        <img src={image01} alt="product-img" className="w-50" />
      </div>

      <div className="product__content">
        <h5>
          <Link to={`/foods/${id}`}>{title}</Link>
        </h5>
        <div className=" d-flex align-items-center justify-content-between ">
          <span className="product__price">${price}</span>
          <button className="addTOCart__btn" onClick={toggle}>
            Add to Cart
          </button>
        </div>
      </div>

      <Modal className="modal-x" isOpen={modal} toggle={toggle} >
        <ModalHeader toggle={toggle}>Add to Cart </ModalHeader>
        <ModalBody>   
          <ListGroup>
            <ListGroupItem active={selectedCart===0 ? true : false} onClick={() => setSelectedCart(0)} style={{cursor: "pointer"}}>
              <ListGroupItemHeading>
                My Cart
              </ListGroupItemHeading>
            </ListGroupItem>
            {groups.length > 0 ? groups.map(group => (
              <ListGroupItem active={selectedCart===group.id ? true : false} onClick={() => setSelectedCart(group.id)} style={{cursor: "pointer"}}>
                <ListGroupItemHeading>
                  {group.title}'s Cart
                </ListGroupItemHeading>
              </ListGroupItem>
            )) : null}
          </ListGroup>
        </ModalBody>
        <ModalFooter>
          <Button color="primary" onClick={addToCart}>
            Add
          </Button>{' '}
          <Button color="secondary" onClick={toggle}>
            Cancel
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

export default ProductCard;

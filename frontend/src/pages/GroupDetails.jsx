import React, { useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Card, Table, CardTitle, CardSubtitle, Button, Modal, ModalHeader, ModalBody, ListGroup, ListGroupItem, ListGroupItemHeading, ListGroupItemText, ModalFooter } from "reactstrap";


import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";
import { addressActions } from "../store/user/adressSlice";

const GroupDetails = () => {

    const groups = [
        {
            id: 1,
            balance: 300,
            title: "Bros",
            members: [
                {
                    id: 1,
                    name: "Ali",
                    email: "ali@gmail.com"
                },
                {
                    id: 2,
                    name: "Korhan",
                    email: "korhan@gmail.com"
                },
                {
                    id: 3,
                    name: "Kaan",
                    email: "kaan@gmail.com"
                },
            ]
        },
        {
            id: 2,
            balance: 300,
            title: "Family",
            members: [
                {
                    id: 1,
                    name: "Ali",
                    email: "ali@gmail.com"
                },
                {
                    id: 2,
                    name: "Korhan",
                    email: "korhan@gmail.com"
                },
                {
                    id: 3,
                    name: "Kaan",
                    email: "kaan@gmail.com"
                },
            ]
        }
    ];
    const { id } = useParams();
    const [group, setGroup] = useState(groups.filter(group => `${group.id}` === id)[0]);

    
    const groupAddresses = useSelector(state => state.address.groupAddress);
    const selectedGroupAddress = useSelector(state => state.address.selectedGroupAddress);

    const [modal, setModal] = useState(false);
    const [modalAddMoney, setModalAddMoney] = useState(false);

    const toggle = () => setModal(!modal);
    const toggleAddMoney = () => setModalAddMoney(!modalAddMoney);

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const changeSelAddress = id => {
        dispatch(addressActions.changeSelectedGroupAddress({id}));
    }

    const toggleGroupCart = () => {
        navigate(`/groupCart/${id}`);
    }

    console.log(group);
  return (
    <Helmet title={group.title}>
      <CommonSection title={group.title} />

      <section>
        <Container>
            <Row className="mb-4">
                <Col lg="3" md="3">
                    <h4>Balance: 200$</h4>
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
                    <span className="cart__icon" onClick={toggleGroupCart}>
                        <i class="ri-shopping-basket-line"></i>
                        <span className="cart__badge_x">{3}</span>
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
                <Button color="primary" onClick={toggle}>
                  Do Something
                </Button>{' '}
                <Button color="secondary" onClick={toggle}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
            <Modal className="modal-x" isOpen={modalAddMoney} toggle={toggleAddMoney} >
              <ModalHeader toggle={toggleAddMoney}>Add Money</ModalHeader>
              <ModalBody>   
                
              </ModalBody>
              <ModalFooter>
                <Button color="primary" onClick={toggleAddMoney}>
                  Do Something
                </Button>{' '}
                <Button color="secondary" onClick={toggleAddMoney}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
        </Container>
      </section>
    </Helmet>
  );
};

export default GroupDetails;

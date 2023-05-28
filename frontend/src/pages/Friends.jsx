import React, { useEffect, useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Card, CardTitle, Button, Modal, ModalHeader, ModalBody, ModalFooter, InputGroup, Input } from "reactstrap";

import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useDispatch, useSelector } from "react-redux";
import { friendsActions } from "../store/group/friendsSlice";

const Friends = () => {

    const friends = useSelector(state => state.friends.friends);
    const userId = useSelector(state => state.auth.userId);

    const [email, setEmail] = useState("");
    const [modalAddMember, setModalAddMember] = useState(false);

    const toggleAddMember = () => setModalAddMember(!modalAddMember);

    const dispatch = useDispatch();


    useEffect(() => {
      dispatch(friendsActions.getFriends({userId}));
    })

    const addMember = () => {

    }
  return (
    <Helmet title="Friends">
      <CommonSection title="Friends" />

      <section>
        <Container>
            <Row>
                <h2 className="mb-4">Friends</h2>
                <Col>
                  <Button color="success" className="mb-4" onClick={toggleAddMember}>Add Friend</Button>
                </Col>
                {friends?.map((member, index) => (
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

            {/** ADD MONEY TO GROUP BALANCE MODAL */}
            <Modal className="modal-x" isOpen={modalAddMember} toggle={toggleAddMember} >
              <ModalHeader toggle={toggleAddMember}>Add Friend</ModalHeader>
              <ModalBody>   
                <Container>
                  <Row>
                    <Col lg="1" md="1"></Col>
                    <Col>
                        <InputGroup>
                            <Input value={email} onChange={e => setEmail(e.target.value)} placeholder="email" />
                        </InputGroup>
                    </Col>
                  </Row>
                  <Row>
                    <Col lg="4" md="4"></Col>
                    <Col>
                      <Button className="mt-4" color="primary" onClick={addMember}>
                        Add Friend
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
    </Helmet>
  );
};

export default Friends;

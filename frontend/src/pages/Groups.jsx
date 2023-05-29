import React, { useEffect, useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Card, Table, CardTitle, CardSubtitle, Button, Modal, ModalHeader, ModalBody, ListGroup, ModalFooter, Input } from "reactstrap";


import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { groupsActions } from "../store/group/groupSlice";

const Groups = () => {

    const groups = useSelector(state => state.groups.groups);
    const userId = useSelector(state => state.auth.userId);

    const navigate = useNavigate();
    const dispatch = useDispatch();

    const [modal, setModal] = useState(false);
    const toggle = () => setModal(!modal);

    const orderTog = id => {
        navigate(`/groups/${id}`)
    }

    useEffect(() => {
      dispatch(groupsActions.getGroups({userId}));
    })
    
  return (
    <Helmet title="My Groups">
      <CommonSection title="My Groups" />

      <section>
        <Container>
          <Row>

            {groups.map((group, index) => (
              <Col lg="12" md="12" sm="6" xs="6" key={group.id} className="mb-4">
                <Card className="p-4">
                    <Container>
                        <Row style={{display: "flex"}}>
                            <Col lg="10" md="10">
                                <CardTitle tag="h3">
                                    {group.title}
                                </CardTitle>
                                <CardSubtitle
                                    className="mb-2 text-muted"
                                    tag="h6"
                                >
                                    22 May 2023
                                </CardSubtitle>
                            </Col>
                            <Col lg="2" md="2">
                                Balance: {group.balance}$
                            
                                <Button className="mt-4" onClick={()  => orderTog(group.id)}>Order Together</Button>
                            </Col>
                        </Row>
                    </Container>
                </Card>
            </Col>
            ))}
          </Row>
          <Row>
            <Col>
                <Button color="danger" onClick={toggle}>Create Group</Button>
            </Col>
          </Row>
        </Container>
            <Modal className="modal-x" isOpen={modal} toggle={toggle} >
              <ModalHeader toggle={toggle}>Create Group</ModalHeader>
              <ModalBody> 
                <Container>
                  <Row>
                    <Col lg="5" md="5">
                      <h5>Group Name:</h5>  
                    </Col>
                    <Col lg="6" md="6">
                      <Input />
                    </Col>
                  </Row>
                </Container>
              </ModalBody>
              <ModalFooter>
                <Button color="primary" onClick={toggle}>
                  Create
                </Button>{' '}
                <Button color="secondary" onClick={toggle}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
      </section>
    </Helmet>
  );
};

export default Groups;

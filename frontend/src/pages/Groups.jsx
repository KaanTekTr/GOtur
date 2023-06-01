import React, { useEffect, useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Card, Table, CardTitle, CardSubtitle, Button, Modal, ModalHeader, ModalBody, ListGroup, ModalFooter, Input } from "reactstrap";


import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { addGroupsThunk, getGroupsThunk, groupsActions } from "../store/group/groupSlice";

const Groups = () => {

    const groups = useSelector(state => state.groups.groups);
    const userId = useSelector(state => state.auth.userId);

    const navigate = useNavigate();
    const dispatch = useDispatch();

    const [groupName, setGroupName] = useState(""); 
    const [modal, setModal] = useState(false);
    const toggle = () => setModal(!modal);

    const orderTog = id => {
        navigate(`/groups/${id}`)
    }

    useEffect(() => {
      dispatch(getGroupsThunk({userId}));
    }, [dispatch,userId]);

    const createGroup = () => {
      const group = {
        group_owner_id: userId,
        group_name: groupName,
      }
      dispatch(addGroupsThunk({group}));
      toggle();
    }
    
  return (
    <Helmet title="My Groups">
      <CommonSection title="My Groups" />

      <section>
        <Container>
          <Row>

            {groups.map((group, index) => (
              <Col lg="12" md="12" sm="6" xs="6" key={group.group_id} className="mb-4">
                <Card className="p-4">
                    <Container>
                        <Row style={{display: "flex"}}>
                            <Col lg="10" md="10">
                                <CardTitle tag="h3">
                                    {group.group_name}
                                </CardTitle>
                                <CardSubtitle
                                    className="mb-2 text-muted"
                                    tag="h6"
                                >
                                    22 May 2023
                                </CardSubtitle>
                            </Col>
                            <Col lg="2" md="2">
                                Balance: {group.group_balance}$
                            
                                <Button className="mt-4" onClick={()  => orderTog(group.group_id)}>Order Together</Button>
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
                      <Input value={groupName} onChange={e => setGroupName(e.target.value)}/>
                    </Col>
                  </Row>
                </Container>
              </ModalBody>
              <ModalFooter>
                <Button color="primary" onClick={createGroup}>
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

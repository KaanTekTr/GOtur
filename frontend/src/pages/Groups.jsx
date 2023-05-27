import React, { useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Card, Table, CardTitle, CardSubtitle, Button, Modal, ModalHeader, ModalBody, ListGroup, ModalFooter } from "reactstrap";


import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

const Groups = () => {

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
            id: 1,
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

    const navigate = useNavigate();

    const [modal, setModal] = useState(false);
    const toggle = () => setModal(!modal);

    const orderTog = id => {
        navigate(`/groups/${id}`)
    }
    
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
            <Modal isOpen={modal} toggle={toggle} >
              <ModalHeader toggle={toggle}>Group Address Selection</ModalHeader>
              <ModalBody>   
                
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
      </section>
    </Helmet>
  );
};

export default Groups;
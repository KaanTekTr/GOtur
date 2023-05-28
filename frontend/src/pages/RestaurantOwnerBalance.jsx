import React, { useState } from "react";

import Helmet from "../components/Helmet/Helmet.js";

import "../styles/hero-section.css";



import "../styles/home.css";


import { Container, Row, Col, Card, CardTitle, Button, Modal, ModalHeader, ModalBody, ModalFooter, InputGroup, Input } from "reactstrap";

import "../styles/all-foods.css";
import "../styles/pagination.css";




const RestaurantOwnerBalance = () => {
    const [balance, setBalance] = useState(1000); // Initial balance can be fetched from backend
    const [amount, setAmount] = useState(0);
    const [modalWithdrawMoney, setModalWithdrawMoney] = useState(false);

    const toggleWithdrawMoney = () => {
        setModalWithdrawMoney(!modalWithdrawMoney);

    }
    const withdrawMoney = () => {
        
        const withdrawalAmount = amount;

        if (isNaN(withdrawalAmount)) {
            alert('Invalid input. Please enter a number.');
            return;
        }

        if (withdrawalAmount <= balance) {
            setBalance(prevBalance => prevBalance - withdrawalAmount);
            alert('Money has been successfully withdrawn from your account.');

        } else {
            alert('Insufficient balance. Please enter an amount less than or equal to your balance.');
        }

    }


    return (
        <Helmet title="Balance">

        <section>

        <div>
            <Card body>
                <CardTitle tag="h5">Restaurant Balance</CardTitle>
                <p>Your current balance is: ${balance.toFixed(2)}</p>
                <div>
                
                <Button color="success" onClick={toggleWithdrawMoney} disabled={balance === 0}>Withdraw Money</Button>
                </div>
            </Card>
        </div>
        </section>
        {/** ADD MONEY TO GROUP BALANCE MODAL */}
        <Modal className="modal-x" isOpen={modalWithdrawMoney} toggle={toggleWithdrawMoney} >
              <ModalHeader toggle={toggleWithdrawMoney}>Withdraw Money</ModalHeader>
              <ModalBody>   
                <Container>
                  <Row>
                    <Col lg="1" md="1"></Col>
                    <Col>
                        <InputGroup>
                            <Input value={amount} onChange={e => setAmount(e.target.value)} placeholder="amount" />
                        </InputGroup>
                    </Col>
                  </Row>
                  <Row>
                    <Col lg="4" md="4"></Col>
                    <Col>
                      <Button className="mt-4" color="primary" onClick={withdrawMoney}>
                        Withdraw
                      </Button>
                    </Col>
                  </Row>
                </Container>
              </ModalBody>
              <ModalFooter>
                <Button color="secondary" onClick={toggleWithdrawMoney}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
      </Helmet>
    );
};

export default RestaurantOwnerBalance;

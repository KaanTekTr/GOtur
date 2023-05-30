import React, { useState } from "react";
import Helmet from "../components/Helmet/Helmet.js";
import "../styles/hero-section.css";
import "../styles/home.css";
import { Container, Row, Col, Card, CardTitle, Button, Modal, ModalHeader, ModalBody, ModalFooter, InputGroup, Input, InputGroupText } from "reactstrap";
import "../styles/all-foods.css";
import "../styles/pagination.css";

const CustomerBalance = () => {
    const [balance, setBalance] = useState(1000);
    const [amount, setAmount] = useState(0);
    const [cardName, setCardName] = useState("");
    const [cardNumber, setCardNumber] = useState("");
    const [expireDate, setExpireDate] = useState("");
    const [cvv, setCvv] = useState("");
    const [modalAddMoney, setModalAddMoney] = useState(false);

    const toggleAddMoney = () => {
        setModalAddMoney(!modalAddMoney);
    }

    const addMoney = () => {
        const addAmount = parseFloat(amount);
        if (isNaN(addAmount) || addAmount <= 0) {
            alert('Invalid input. Please enter a positive number.');
            return;
        }
        setBalance(prevBalance => prevBalance + addAmount);
        alert('Money has been successfully added to your account.');
    }

    return (
        <Helmet title="Balance">
            <section>
                <div>
                    <Card body>
                        <CardTitle tag="h5">Customer Balance</CardTitle>
                        <p>Your current balance is: ${balance.toFixed(2)}</p>
                        <div>
                            <Button color="success" onClick={toggleAddMoney}>Add Money</Button>
                        </div>
                    </Card>
                </div>
            </section>

            <Modal className="modal-x" isOpen={modalAddMoney} toggle={toggleAddMoney}>
                <ModalHeader toggle={toggleAddMoney}>Add Money</ModalHeader>
                <ModalBody>   
                    <Container>
                        <Row className="mb-2">
                            <Col>
                                <InputGroup>
                                        <InputGroupText>Card Holder's Name</InputGroupText>
                                    <Input value={cardName} onChange={e => setCardName(e.target.value)} placeholder="John Doe" />
                                </InputGroup>
                            </Col>
                        </Row>
                        <Row className="mb-2">
                            <Col>
                                <InputGroup>
                                        <InputGroupText>Card Number</InputGroupText>
                                    <Input value={cardNumber} onChange={e => setCardNumber(e.target.value)} placeholder="1234 5678 9101 1121" />
                                </InputGroup>
                            </Col>
                        </Row>
                        <Row className="mb-2">
                            <Col>
                                <InputGroup>
                                        <InputGroupText>Expiration Date</InputGroupText>
                                    <Input value={expireDate} onChange={e => setExpireDate(e.target.value)} placeholder="MM/YY" />
                                </InputGroup>
                            </Col>
                        </Row>
                        <Row className="mb-2">
                            <Col>
                                <InputGroup>
                                        <InputGroupText>CVV</InputGroupText>
                                    <Input value={cvv} onChange={e => setCvv(e.target.value)} placeholder="123" />
                                </InputGroup>
                            </Col>
                        </Row>
                        <Row className="mb-2">
                            <Col>
                                <InputGroup>
                                        <InputGroupText>Amount</InputGroupText>
                                    <Input value={amount} onChange={e => setAmount(e.target.value)} placeholder="Amount to add" />
                                </InputGroup>
                            </Col>
                        </Row>
                        <Row className="mt-4">
                            <Col className="text-right">
                                <Button color="secondary" onClick={toggleAddMoney}>
                                    Cancel
                                </Button>
                                <Button className="ml-2" color="primary" onClick={addMoney}>
                                    Add Money
                                </Button>
                            </Col>
                        </Row>
                    </Container>
                </ModalBody>
            </Modal>
        </Helmet>
    );
};

export default CustomerBalance;

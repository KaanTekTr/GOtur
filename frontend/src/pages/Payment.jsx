import React, { useState } from "react";
import Helmet from "../components/Helmet/Helmet.js";
import "../styles/hero-section.css";
import "../styles/home.css";
import { Container, Row, Col, Card, CardTitle, Button, Modal, ModalHeader, ModalBody, InputGroup, Input, InputGroupText } from "reactstrap";
import "../styles/all-foods.css";
import "../styles/pagination.css";

const Payment = () => {
    const [amount, setAmount] = useState(0);
    const [cardName, setCardName] = useState("");
    const [cardNumber, setCardNumber] = useState("");
    const [expireDate, setExpireDate] = useState("");
    const [cvv, setCvv] = useState("");
    const [modalPayCard, setModalPayCard] = useState(false);

    // Dummy order info - replace this with your actual order info
    const order = {
        product: "Delicious Pizza",
        totalAmount: 25.99
    };

    const togglePayCard = () => {
        setModalPayCard(!modalPayCard);
    }

    const payWithCard = () => {
        // Process the payment using the card info
        alert('Payment has been successfully processed.');
    }

    const payWithBalance = () => {
        // Process the payment using the balance
        alert('Payment has been successfully processed using your balance.');
    }

    const payAtDoor = () => {
        // Process the payment to be paid at the door
        alert('Your order will be paid at the door.');
    }

    return (
        <Helmet title="Payment">
            <section>
                <div>
                    <Card body>
                        <CardTitle tag="h5">Order Review</CardTitle>
                        <p>Product: {order.product}</p>
                        <p>Total Amount: ${order.totalAmount.toFixed(2)}</p>
                        <hr/>
                        <CardTitle tag="h5" className="mb-3">Choose Payment Method</CardTitle>
                        <div style={{display: 'flex', justifyContent: 'space-between', maxWidth: '500px'}}>
                            <Button color="success" onClick={payWithBalance} size="sm">Pay with Balance</Button>
                            <Button color="primary" onClick={togglePayCard} size="sm">Pay with Card</Button>
                            <Button color="warning" onClick={payAtDoor} size="sm">Pay at Door</Button>
                        </div>
                    </Card>
                </div>
            </section>

            <Modal className="modal-x" isOpen={modalPayCard} toggle={togglePayCard}>
                <ModalHeader toggle={togglePayCard}>Pay with Card</ModalHeader>
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
                                        <InputGroupText>Expire Date</InputGroupText>
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
                                    <Input value={amount} onChange={e => setAmount(e.target.value)} placeholder="Amount to pay" />
                                </InputGroup>
                            </Col>
                        </Row>
                        <Row className="mt-4">
                            <Col className="text-right">
                                <Button color="secondary" onClick={togglePayCard}>
                                    Cancel
                                </Button>
                                <Button className="ml-2" color="primary" onClick={payWithCard}>
                                    Pay
                                </Button>
                            </Col>
                        </Row>
                    </Container>
                </ModalBody>
            </Modal>
        </Helmet>
    );
};

export default Payment;

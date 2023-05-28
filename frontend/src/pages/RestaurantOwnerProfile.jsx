import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { Container, Card, CardTitle, Button, Modal, ModalHeader, ModalBody, ModalFooter, Form, FormGroup, Label, Input } from 'reactstrap';
import Helmet from "../components/Helmet/Helmet.js";

import "../styles/hero-section.css";
const RestaurantOwnerProfile = () => {
  const [modal, setModal] = useState(false);
  const [password, setPassword] = useState('password'); // For simplicity, initial password is set to 'password'. Real password should not be stored like this.

  // Get user data from the auth state
  const user = useSelector(state => state.auth);

  const toggleModal = () => setModal(!modal);

  const handlePasswordChange = (e) => {
    e.preventDefault();
    setPassword(e.target.newPassword.value);
    alert("Password has changed successfully.");
    toggleModal();
  };

  return (
    <Helmet title="Profile">
    <Container>
      <Card body>
        <CardTitle tag="h5">Restaurant Owner Profile</CardTitle>
        <p>Name: {user.fullName}</p>
        <p>Email: {user.email}</p>
        <p>Birthdate: {user.birthdate}</p>
        <p>Gender: {user.gender}</p>
        <p>User Type: {user.authType}</p>
        <p>Phone Number: {user.phoneNumber}</p>
        <div>
        <Button color="primary" onClick={toggleModal}>Change Password</Button>
        </div>
      </Card>

      <Modal isOpen={modal} toggle={toggleModal}>
        <ModalHeader toggle={toggleModal}>Change Password</ModalHeader>
        <ModalBody>
          <Form onSubmit={handlePasswordChange}>
          <FormGroup>
              <Label for="oldPassword">Current Password</Label>
              <Input type="password" name="oldPassword" id="oldPassword" required />
            </FormGroup>
            <FormGroup>
              <Label for="newPassword">New Password</Label>
              <Input type="password" name="newPassword" id="newPassword" required />
            </FormGroup>
            <Button color="primary" type="submit">Update Password</Button>
          </Form>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={toggleModal}>Cancel</Button>
        </ModalFooter>
      </Modal>
    </Container>
    </Helmet>

  );
};

export default RestaurantOwnerProfile;

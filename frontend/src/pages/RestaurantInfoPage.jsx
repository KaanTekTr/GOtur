import React, { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Container, Card, CardTitle, Button, Modal, ModalBody, ModalHeader, Input, Form } from 'reactstrap';
import { Link } from 'react-router-dom';
// import { updateRestaurant } from '../redux/actions/restaurantActions';

const RestaurantInfoPage = () => {
  // const restaurant = useSelector(state => state.restaurant);
  const restaurant = {
    id: '1',
    name: 'Anadolu Tat 1071',
    address: '123 Main St, Anytown, USA',
    minOrderLimit: 100,
    openHours: '10.00-22.00',
    cuisineType: 'Turkish Kebab'

  };

  const id = "01";
  const [orderModal, setOrderModal] = useState(false);
  const [hourModal, setHourModal] = useState(false);

  const [minOrderLimit, setMinOrderLimit] = useState(restaurant.minOrderLimit);
  const [openHours, setOpenHours] = useState(restaurant.openHours);
  
  const dispatch = useDispatch();

  const toggleOrderModal = () => {
    setOrderModal(!orderModal);
  };
  const toggleHourModal = () => {
    setHourModal(!hourModal);
  };

  const updateHourInfo = () => {
    setOpenHours(openHours);
    // dispatch(updateRestaurant({ minOrderLimit, openHours }));
    toggleHourModal();
  };
  const updateOrderInfo = () => {
    setMinOrderLimit(minOrderLimit);

    // dispatch(updateRestaurant({ minOrderLimit, openHours }));
    toggleOrderModal();
  };

  return (
    <Container>
      <Card body>
        <CardTitle tag="h5">My Restaurant Information</CardTitle>
        <p>Restaurant Name: {restaurant.name}</p>
        <p>Restaurant Address: {restaurant.address}</p>
        <p>Minimum Order Limit: {restaurant.minOrderLimit} TL 
          <Button onClick={toggleOrderModal} size="sm" color="primary">Edit</Button>
        </p>
        <p>Open Hours: {restaurant.openHours} 
          <Button onClick={toggleHourModal} size="sm" color="primary">Edit</Button>
        </p>
        <p>Cuisine Type: {restaurant.cuisineType}</p>
        <div>
        <Button color="primary">
          <Link to={`/restaurant-menu/${id}`} style={{ color: '#fff' }}>View Restaurant Menu</Link>
        </Button>
        </div>
        <Modal isOpen={orderModal} toggle={toggleOrderModal}>
          <ModalHeader toggle={toggleOrderModal}>Edit Information</ModalHeader>
          <ModalBody>
            <Form onSubmit={updateOrderInfo}>
              <Input 
                type="text" 
                placeholder="Minimum Order Limit" 
                value={minOrderLimit} 
                onChange={e => setMinOrderLimit(e.target.value)}
              />
              <Button color="primary" type="submit">Update</Button>
            </Form>
          </ModalBody>
        </Modal>
        <Modal isOpen={hourModal} toggle={toggleHourModal}>
          <ModalHeader toggle={toggleHourModal}>Edit Information</ModalHeader>
          <ModalBody>
            <Form onSubmit={updateHourInfo}>
              <Input 
                type="text" 
                placeholder="Open Hours" 
                value={openHours} 
                onChange={e => setOpenHours(e.target.value)}
              />
              <Button color="primary" type="submit">Update</Button>
            </Form>
          </ModalBody>
        </Modal>
      </Card>
    </Container>
  );
};

export default RestaurantInfoPage;

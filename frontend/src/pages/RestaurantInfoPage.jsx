import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Container, Card, CardTitle, Button, Modal, ModalBody, ModalHeader, Input, Form } from 'reactstrap';
import { Link } from 'react-router-dom';
import { getRestOfOwnerThunk } from '../store/restaurant/restaurantSlice';
// import { updateRestaurant } from '../redux/actions/restaurantActions';

const RestaurantInfoPage = () => {
  // const restaurant = useSelector(state => state.restaurant);

  const id = "01";
  const [orderModal, setOrderModal] = useState(false);
  const [hourModal, setHourModal] = useState(false);

  const [minOrderLimit, setMinOrderLimit] = useState("");
  const [openHours, setOpenHours] = useState("");
  
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

  const userId = useSelector(state => state.auth.userId);

  useEffect(() => {
    dispatch(getRestOfOwnerThunk({userId}));
  }, [dispatch, userId]);

  const restaurant = useSelector(state => state.restaurant.myRestaurant);

  return (
    <>
    { restaurant.info ? 
    <Container className='mt-4 mb-4' >
    <Card className='p-4' style={{marginBottom:"200px", marginTop:"50px"}} body>
      <CardTitle tag="h5">My Restaurant Information</CardTitle>
      <p>Restaurant Name: {restaurant.info.restaurant_name}</p>
      <p>Restaurant Address: {restaurant.info.district}</p>
      <p>Minimum Order Limit: {restaurant.info.min_delivery_price} $ {"   "}
        <Button onClick={toggleOrderModal} size="sm" color="primary">Edit</Button>
      </p>
      <p>Open Hours: {restaurant.info.open_hour} - {restaurant.info.close_hour} {"    "} 
        <Button onClick={toggleHourModal} size="sm" color="primary">Edit</Button>
      </p>
      <div>
      <Button color="primary">
        <Link to={`/restaurant-menu/${restaurant.info.restaurant_id}`} style={{ color: '#fff' }}>View Restaurant Menu</Link>
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
    : <span>Loading...</span>}
    </>
    
  );
};

export default RestaurantInfoPage;

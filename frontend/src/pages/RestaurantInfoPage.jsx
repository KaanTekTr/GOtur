import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Container, Card, CardTitle, Button, Modal, ModalBody, ModalHeader, Input, Form, Row, Col, InputGroup, InputGroupText } from 'reactstrap';
import { Link } from 'react-router-dom';
import { addNewRestaurantThunk, editRestaurantThunk, getRestOfOwnerThunk } from '../store/restaurant/restaurantSlice';
import { addNewAddress } from '../lib/api/unsplashService';
import { SettingsApplicationsRounded } from '@material-ui/icons';
// import { updateRestaurant } from '../redux/actions/restaurantActions';

const RestaurantInfoPage = () => {
  // const restaurant = useSelector(state => state.restaurant);

  const id = "01";
  const [orderModal, setOrderModal] = useState(false);
  const [hourModal, setHourModal] = useState(false);
  const [createRestModal, setCreateRestModal] = useState(false);

  const [minOrderLimit, setMinOrderLimit] = useState("");
  const [openHours, setOpenHours] = useState("");
  const [closeHours, setCloseHours] = useState("");

  const [name, setName] = useState("");
  const [district, setDistrict] = useState("");
  const [minPrice, setMinPrice] = useState(0);
  const restaurant = useSelector(state => state.restaurant.myRestaurant);
  const dispatch = useDispatch();

  const toggleOrderModal = () => {
    setOrderModal(!orderModal);
  };

  const toggleCreateRestModal = () => {
    setCreateRestModal(!createRestModal);
  };
  const toggleHourModal = () => {
    setHourModal(!hourModal);
  };

  const updateHourInfo = () => {
    const rest = {
      restaurant_name: restaurant.info.restaurant_name,
      district : restaurant.info.district,
      min_delivery_price: restaurant.info.min_delivery_price,
      open_hour : openHours,
      close_hour : closeHours
    }
    dispatch(editRestaurantThunk( { id: restaurant.info.restaurant_id, restaurant:rest}));
    setTimeout(function(){
      dispatch(getRestOfOwnerThunk({userId}));
        
    },500);
    toggleHourModal();
  };
  const updateOrderInfo = () => {
    const rest = {
      restaurant_name: restaurant.info.restaurant_name,
      district : restaurant.info.district,
      min_delivery_price: minOrderLimit,
      open_hour : restaurant.info.open_hour,
      close_hour : restaurant.info.close_hour
    }
    dispatch(editRestaurantThunk( { id: restaurant.info.restaurant_id, restaurant:rest}));
    setTimeout(function(){
      dispatch(getRestOfOwnerThunk({userId}));
        
    },500);
    toggleOrderModal();
  };

  const createRest = () => {
    const rest = {
      restaurant_name: name,
      district : district,
      min_delivery_price: minPrice
    }
    dispatch(addNewRestaurantThunk({id:userId, restaurant: rest}));
    setTimeout(function(){
      dispatch(getRestOfOwnerThunk({userId}));
        
    },500);
  }

  const userId = useSelector(state => state.auth.userId);

  useEffect(() => {
    dispatch(getRestOfOwnerThunk({userId}));
  }, [dispatch, userId]);

 

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
            <Input 
              type="text" 
              placeholder="Minimum Order Limit" 
              value={minOrderLimit} 
              onChange={e => setMinOrderLimit(e.target.value)}
            />
            <Button color="primary"onClick={updateOrderInfo} type="submit">Update</Button>
        </ModalBody>
      </Modal>
      <Modal isOpen={hourModal} toggle={toggleHourModal}>
        <ModalHeader toggle={toggleHourModal}>Edit Information</ModalHeader>
        <ModalBody>
          
            <Input 
              type="text" 
              placeholder="Opening at" 
              value={openHours} 
              onChange={e => setOpenHours(e.target.value)}
            />
            <Input 
              type="text" 
              placeholder="Closing at" 
              value={closeHours} 
              onChange={e => setCloseHours(e.target.value)}
            />
            <Button color="primary" onClick={updateHourInfo} type="submit">Update</Button>
        </ModalBody>
      </Modal>
    </Card>
  </Container>
    : <div>
        <h3 style={{margin: "100px 300px"}}>You don't have restaurant</h3>
        <Button onClick={toggleCreateRestModal} style={{marginBottom: "300px", marginLeft: "30px"}} color="primary" type="submit">Create Restaurant</Button>
        <Modal isOpen={createRestModal} toggle={toggleCreateRestModal}>
          <ModalHeader toggle={toggleCreateRestModal}>Create Restaurant</ModalHeader>
          <ModalBody>
            <Container>
              <Row className="mb-2">
                <Col>
                  <InputGroup>
                    <InputGroupText>Restaurant Name</InputGroupText>
                    <Input value={name} onChange={e => setName(e.target.value)} placeholder="Dominos" />
                  </InputGroup>
                </Col>
              </Row>
              <Row className="mb-2">
                <Col>
                  <InputGroup>
                    <InputGroupText>District</InputGroupText>
                    <Input value={district} onChange={e => setDistrict(e.target.value)} placeholder=" Çankaya" />
                  </InputGroup>
                </Col>
              </Row>
              <Row className="mb-2">
                <Col>
                  <InputGroup>
                    <InputGroupText>Min Delivery Price</InputGroupText>
                    <Input value={minPrice} onChange={e => setMinPrice(e.target.value)} placeholder=" 100" />
                  </InputGroup>
                </Col>
              </Row>
              <Button onClick={createRest} color="primary" type="submit">Create Restaurant</Button>

            </Container>
          </ModalBody>
        </Modal>
      </div>}
    </>
    
  );
};

export default RestaurantInfoPage;

import React, { useRef, useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";
import { Container, Row, Col } from "reactstrap";
import { Link, useNavigate } from "react-router-dom";

import { useDispatch, useSelector } from "react-redux";
import { authActions, loginThunk } from "../store/authSlice";

const Login = () => {
  const loginNameRef = useRef();
  const loginPasswordRef = useRef();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const authType = useSelector(state => state.auth.authType);

  const submitHandler = (e) => {

    e.preventDefault();
    dispatch(authActions.changeAuthType("customer"));
    dispatch(loginThunk({
      authType,
      email,
      password
    }));
    navigate("/home");
  };

  const submitHandlerRestaurant = (e) => {
    e.preventDefault();
    dispatch(authActions.changeAuthType("restaurantOwner"));
    dispatch(loginThunk({
      authType,
      email,
      password
    }));
    navigate("/RestaurantOwnerHome");
  };

  return (
    <Helmet title="Login">
      <CommonSection title="Login" />
      <section>
        <Container>
          <Row>
            <Col lg="6" md="6" sm="12" className="m-auto text-center">
              <form className="form mb-5" >
                <div className="form__group">
                  <input
                    type="email"
                    placeholder="Email"
                    required
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    ref={loginNameRef}
                  />
                </div>
                <div className="form__group">
                  <input
                    type="password"
                    placeholder="Password"
                    required
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    ref={loginPasswordRef}
                  />
                </div>
                <button type="submit" onClick={submitHandler} className="addTOCart__btn">
                  Login
                </button>
                <div style={{ height: '10px' }} /> {/* This adds some space between the buttons */}
                <button type="submit" onClick={submitHandlerRestaurant} className="addTOCart__btn">
                  Login as Restaurant Owner
                </button>
              </form>
              <Link to="/register">
                Don't have an account? Create an account
              </Link>
            </Col>
          </Row>
        </Container>
      </section>
    </Helmet>
  );
};

export default Login;

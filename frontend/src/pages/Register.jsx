import React, { useRef, useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";
import { Container, Row, Col } from "reactstrap";
import { Link, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { authActions, registerThunk } from "../store/authSlice";

const Register = () => {
  const signupNameRef = useRef();
  const signupPasswordRef = useRef();
  const signupEmailRef = useRef();
  const signupBirthdateRef = useRef();
  const signupGenderRef = useRef();
  const signupConfirmPasswordRef = useRef();
  const signupUserTypeRef = useRef();
  const signupPhoneNumberRef = useRef();

  const [email, setEmail] = useState("");
  const [fullName, setFullName] = useState("");
  const [password, setPassword] = useState("");
  const [age, setAge] = useState(0);
  const [gender, setGender] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [authType, setUserType] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const submitHandler = (e) => {
    e.preventDefault();

    if(password !== confirmPassword) {
      alert("Passwords do not match");
      return;
    }

    const customer = {
      username: fullName,
      hashed_password: password,
      password_salt: password,
      email,
      phone_number: phoneNumber,
      age,
      gender,
      payment_method: "Money"
    }

    const restaurantOwner = {
      username: fullName,
      hashed_password: password,
      password_salt: password,
      email,
      phone_number: phoneNumber,
      age,
      gender,
    }
    if (authType === "customer") {
      dispatch(registerThunk({authType, user:customer, navigate}));
    } else {
      dispatch(registerThunk({authType, user:restaurantOwner, navigate}));
    }
  };

  return (
    <Helmet title="Signup">
      <CommonSection title="Signup" />
      <section>
        <Container>
          <Row>
            <Col lg="6" md="6" sm="12" className="m-auto text-center">
              <form className="form mb-5" onSubmit={submitHandler}>
                <div className="form__group">
                  <input
                    type="text"
                    placeholder="Username"
                    required
                    value={fullName}
                    onChange={e => setFullName(e.target.value)}
                    ref={signupNameRef}
                  />
                </div>
                <div className="form__group">
                  <input
                    type="email"
                    placeholder="Email"
                    required
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    ref={signupEmailRef}
                  />
                </div>
                <div className="form__group">
                  <input
                    type="tel"
                    placeholder="Phone Number"
                    required
                    value={phoneNumber}
                    onChange={e => setPhoneNumber(e.target.value)}
                    ref={signupPhoneNumberRef}
                  />
                </div>
                <div className="form__group">
                  <input
                    type="password"
                    placeholder="Password"
                    required
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    ref={signupPasswordRef}
                  />
                </div>
                <div className="form__group">
                  <input
                    type="password"
                    placeholder="Confirm Password"
                    required
                    value={confirmPassword}
                    onChange={e => setConfirmPassword(e.target.value)}
                    ref={signupConfirmPasswordRef}
                  />
                </div>
                <div className="form__group">
                  <input
                    type="number"
                    placeholder="21"
                    required
                    value={age}
                    onChange={e => setAge(e.target.value)}
                    ref={signupBirthdateRef}
                  />
                </div>
                <div className="form__group">
                  <select
                    required
                    value={gender}
                    onChange={e => setGender(e.target.value)}
                    ref={signupGenderRef}
                  >
                    <option value="">Select Gender</option>
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                    <option value="other">Other</option>
                  </select>
                </div>
                <div className="form__group">
                  <select
                    required
                    value={authType}
                    onChange={e => setUserType(e.target.value)}
                    ref={signupUserTypeRef}
                  >
                    <option value="">Select User Type</option>
                    <option value="customer">Customer</option>
                    <option value="restaurantOwner">Restaurant Owner</option>
                  </select>
                </div>
                <button type="submit" className="addTOCart__btn">
                  Sign Up
                </button>
              </form>
              <Link to="/login">Already have an account? Login</Link>
            </Col>
          </Row>
        </Container>
      </section>
    </Helmet>
  );
};

export default Register;

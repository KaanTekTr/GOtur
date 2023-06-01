import React, { useState, useEffect } from "react";

import { useParams } from "react-router-dom";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";
import { Container, Row, Col } from "reactstrap";

import { useDispatch, useSelector } from "react-redux";
import { cartActions } from "../store/shopping-cart/cartSlice";

import "../styles/product-details.css";
import image01 from "../assets/images/bread.png"; 

import ProductCard from "../components/UI/product-card/ProductCard";
import { groupsActions } from "../store/group/groupSlice";

const FoodDetails = () => {
  const [tab, setTab] = useState("desc");
  const [enteredName, setEnteredName] = useState("");
  const [enteredEmail, setEnteredEmail] = useState("");
  const [reviewMsg, setReviewMsg] = useState("");
  const { id } = useParams();
  const dispatch = useDispatch();

  const products = useSelector(state => state.restaurant.products);
  const food__categories = useSelector(state => state.restaurant.foodCategories);
  const menu__categories = useSelector(state => state.restaurant.menuCategories);


  const product = products.find((product) => `${product.food_id}` === id);

  const foodCategory = food__categories.find(cat => cat.food_category_id === product.food_category_id);
  const menuCategory = menu__categories.find(cat => cat.menu_category_id === product.menu_category_id);

  const [previewImg, setPreviewImg] = useState(image01);
  const { food_name, price, fixed_ingredients } = product;

  //const relatedProduct = products.filter((item) => category === item.category);

  const selectedCart = useSelector(state => state.order.currentCart);

  const addToCart = () => {
    if (selectedCart === 0) {
      dispatch(
        cartActions.addItem({
          id,
          title: food_name,
          image01,
          price,
        })
      );
    } else {
      dispatch(
        groupsActions.addItem({ 
          newItem: {
            id,
            title: food_name,
            image01,
            price,
          },
          groupId: selectedCart
        })
      );
    }
  };

  const submitHandler = (e) => {
    e.preventDefault();

    console.log(enteredName, enteredEmail, reviewMsg);
  };

  useEffect(() => {
    setPreviewImg(product.image01);
  }, [product]);

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [product]);

  return (
    <Helmet title="Product-details">
      <CommonSection title={food_name} />

      <section>
        <Container>
          <Row>
            <Col lg="2" md="2">
              <div className="product__images ">
                <div
                  className="img__item mb-3"
                  onClick={() => setPreviewImg(image01)}
                >
                  <img src={image01} alt="" className="w-50" />
                </div>
              </div>
            </Col>

            <Col lg="4" md="4">
              <div className="product__main-img">
                <img src={previewImg} alt="" className="w-100" />
              </div>
            </Col>

            <Col lg="6" md="6">
              <div className="single__product-content">
                <h2 className="product__title mb-3">{food_name}</h2>
                <p className="product__price">
                  {" "}
                  Price: <span>${price}</span>
                </p>
                <p className="category mb-1">
                  Food Category: <span>{foodCategory.food_category_name}</span>
                </p>
                <p className="category mb-5">
                  Menu Category: <span>{menuCategory.menu_category_name}</span>
                </p>

                <button onClick={addToCart} className="addTOCart__btn">
                  Add to Cart
                </button>
              </div>
            </Col>

            <Col lg="12">
              <div className="tabs d-flex align-items-center gap-5 py-3">
                <h6
                  className={` ${tab === "desc" ? "tab__active" : ""}`}
                  onClick={() => setTab("desc")}
                >
                  Description
                </h6>
                <h6
                  className={` ${tab === "rev" ? "tab__active" : ""}`}
                  onClick={() => setTab("rev")}
                >
                  Review
                </h6>
              </div>

              {tab === "desc" ? (
                <div className="tab__content">
                  <h5>Fixed Ingrediens: {fixed_ingredients}</h5>
                </div>
              ) : (
                <div className="tab__form mb-3">
                  <div className="review pt-5">
                    <p className="user__name mb-0">Jhon Doe</p>
                    <p className="user__email">jhon1@gmail.com</p>
                    <p className="feedback__text">great product</p>
                  </div>

                  <div className="review">
                    <p className="user__name mb-0">Jhon Doe</p>
                    <p className="user__email">jhon1@gmail.com</p>
                    <p className="feedback__text">great product</p>
                  </div>

                  <div className="review">
                    <p className="user__name mb-0">Jhon Doe</p>
                    <p className="user__email">jhon1@gmail.com</p>
                    <p className="feedback__text">great product</p>
                  </div>
                  
                </div>
              )}
            </Col>

            <Col lg="12" className="mb-5 mt-4">
              <h2 className="related__Product-title">You might also like</h2>
            </Col>

            {products.map((item) => (
              <Col lg="3" md="4" sm="6" xs="6" className="mb-4" key={item.id}>
                <ProductCard item={item} />
              </Col>
            ))}
          </Row>
        </Container>
      </section>
    </Helmet>
  );
};

export default FoodDetails;

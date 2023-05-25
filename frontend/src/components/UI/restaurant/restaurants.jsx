import React, { useEffect, useState } from "react";

import "../../../styles/cart-item.css";

import { Container, Row, Col } from "reactstrap";

import foodCategoryImg01 from "../../../assets/images/hamburger.png";
import foodCategoryImg02 from "../../../assets/images/pizza.png";
import foodCategoryImg03 from "../../../assets/images/bread.png";
import RestaurantCard from "./RestaurantCard";

const Restaurants = ({ restaurants }) => {
    const [category, setCategory] = useState("ALL");
    const [allRestaurants, setAllRestaurants] = useState(restaurants);

    useEffect(() => {
        if (category === "ALL") {
            setAllRestaurants(restaurants);
        }
    
        if (category === "BURGER") {
          const filteredProducts = restaurants.filter(
            (item) => item.category === "Burger"
          );
    
          setAllRestaurants(filteredProducts);
        }
    
        if (category === "PIZZA") {
          const filteredProducts = restaurants.filter(
            (item) => item.category === "Pizza"
          );
    
          setAllRestaurants(filteredProducts);
        }
    
        if (category === "BREAD") {
          const filteredProducts = restaurants.filter(
            (item) => item.category === "Bread"
          );
    
          setAllRestaurants(filteredProducts);
        }
    }, [category, restaurants]);

    return (
        <Container>
            <Row>
            <Col lg="12" className="text-center">
                <h2>Popular Resaurants</h2>
            </Col>

            <Col lg="12">
                <div className="food__category d-flex align-items-center justify-content-center gap-4">
                <button
                    className={`all__btn  ${
                    category === "ALL" ? "foodBtnActive" : ""
                    } `}
                    onClick={() => setCategory("ALL")}
                >
                    All
                </button>
                <button
                    className={`d-flex align-items-center gap-2 ${
                    category === "BURGER" ? "foodBtnActive" : ""
                    } `}
                    onClick={() => setCategory("BURGER")}
                >
                    <img src={foodCategoryImg01} alt="" />
                    Burger
                </button>

                <button
                    className={`d-flex align-items-center gap-2 ${
                    category === "PIZZA" ? "foodBtnActive" : ""
                    } `}
                    onClick={() => setCategory("PIZZA")}
                >
                    <img src={foodCategoryImg02} alt="" />
                    Pizza
                </button>

                <button
                    className={`d-flex align-items-center gap-2 ${
                    category === "BREAD" ? "foodBtnActive" : ""
                    } `}
                    onClick={() => setCategory("BREAD")}
                >
                    <img src={foodCategoryImg03} alt="" />
                    Bread
                </button>
                </div>
            </Col>

            {allRestaurants.map((item) => (
                <Col lg="3" md="4" sm="6" xs="6" key={item.id} className="mt-5">
                    <RestaurantCard item={item} />
                </Col>
            ))}
            </Row>
        </Container>
    );
};

export default Restaurants;

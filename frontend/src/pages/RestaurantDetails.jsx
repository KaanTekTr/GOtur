import React, { useState, useEffect } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col } from "reactstrap";

import products from "../assets/fake-data/products";
import ProductCard from "../components/UI/product-card/ProductCard";
import ReactPaginate from "react-paginate";

import foodCategoryImg01 from "../assets/images/hamburger.png";
import foodCategoryImg02 from "../assets/images/pizza.png";
import foodCategoryImg03 from "../assets/images/bread.png";

import { useParams } from "react-router-dom";

import "../styles/all-foods.css";
import "../styles/pagination.css";
import restaurants from "../assets/fake-data/restaurants";

const RestaurantDetails = () => {
  const [category, setCategory] = useState("ALL");
  const [allProducts, setAllProducts] = useState(products);

  const { id } = useParams();

  const restaurant = restaurants.find((restaurant) => restaurant.id === id);

  const [searchTerm, setSearchTerm] = useState("");

  const [pageNumber, setPageNumber] = useState(0);

  const searchedProduct = allProducts.filter((item) => {
    if (searchTerm.value === "") {
      return item;
    }
    if (item.title.toLowerCase().includes(searchTerm.toLowerCase())) {
      return item;
    } else {
      return console.log("not found");
    }
  });

  const productPerPage = 12;
  const visitedPage = pageNumber * productPerPage;
  const displayPage = searchedProduct.slice(
    visitedPage,
    visitedPage + productPerPage
  );

  const pageCount = Math.ceil(searchedProduct.length / productPerPage);

  const changePage = ({ selected }) => {
    setPageNumber(selected);
  };

  useEffect(() => {
    if (category === "ALL") {
      setAllProducts(products);
    }

    if (category === "BURGER") {
      const filteredProducts = products.filter(
        (item) => item.category === "Burger"
      );

      setAllProducts(filteredProducts);
    }

    if (category === "PIZZA") {
      const filteredProducts = products.filter(
        (item) => item.category === "Pizza"
      );

      setAllProducts(filteredProducts);
    }

    if (category === "BREAD") {
      const filteredProducts = products.filter(
        (item) => item.category === "Bread"
      );

      setAllProducts(filteredProducts);
    }
  }, [category]);

  console.log(restaurant.minPrice);

  return (
    <Helmet title="All-Foods">
      <CommonSection title={restaurant.title} desc={restaurant.minPrice} image={restaurant.image01} />

      <section>
        <Container>
          <Row>
              <Col lg="12"  md="6" className="mb-4">
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
              <Col lg="6" md="3" sm="6" xs="12">
                <div className="search__widget d-flex align-items-center justify-content-between ">
                  <input
                    type="text"
                    placeholder="I'm looking for...."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                  />
                  <span>
                    <i class="ri-search-line"></i>
                  </span>
                </div>
              </Col>
              <Col lg="6" md="3" sm="6" xs="12" className="mb-2">
                <div className="sorting__widget text-end">
                  <select className="w-50">
                    <option>Default</option>
                    <option value="ascending">Alphabetically, A-Z</option>
                    <option value="descending">Alphabetically, Z-A</option>
                    <option value="high-price">High Price</option>
                    <option value="low-price">Low Price</option>
                  </select>
                </div>
              </Col>

              {displayPage.map((item) => (
                  <Col lg="3" md="4" sm="6" xs="6" key={item.id} className="mt-5">
                      <ProductCard item={item} />
                  </Col>
              ))}

              
            <div>
              <ReactPaginate
                pageCount={pageCount}
                onPageChange={changePage}
                previousLabel={"Prev"}
                nextLabel={"Next"}
                containerClassName=" paginationBttns "
              />
            </div>
          </Row>
        </Container>
      </section>
    </Helmet>
  );
};

export default RestaurantDetails;

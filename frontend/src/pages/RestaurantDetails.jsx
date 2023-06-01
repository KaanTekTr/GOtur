import React, { useState, useEffect } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Alert } from "reactstrap";

import ProductCard from "../components/UI/product-card/ProductCard";
import ReactPaginate from "react-paginate";

import foodCategoryImg01 from "../assets/images/hamburger.png";
import foodCategoryImg02 from "../assets/images/pizza.png";
import foodCategoryImg03 from "../assets/images/bread.png";
import image01 from "../assets/images/dominos.png";

import { useParams } from "react-router-dom";

import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useDispatch, useSelector } from "react-redux";
import { getAllFoodCategoryThunk, getAllFoodRestThunk, getAllMenuCategoryThunk, getRestaurantsThunk, restaurantActions } from "../store/restaurant/restaurantSlice";

const RestaurantDetails = () => {
  const [category, setCategory] = useState("ALL");
  
  const { id } = useParams();
  const dispatch = useDispatch();
  
  useEffect(() => {
    dispatch(getRestaurantsThunk());
    dispatch(getAllFoodCategoryThunk({restaurant_id: id}));
    dispatch(getAllMenuCategoryThunk({restaurant_id: id}));
    dispatch(getAllFoodRestThunk({restaurant_id: id}));

    dispatch(restaurantActions.getProducts());
    dispatch(restaurantActions.visitRest(id));
  }, [dispatch, id]);
  const restaurants = useSelector(state => state.restaurant.restaurants);
  console.log(restaurants);
  
  const restaurant = restaurants.find((restaurant) => `${restaurant.restaurant_id}` === id);

  console.log(restaurant);
  
  const [searchTerm, setSearchTerm] = useState("");
  const [pageNumber, setPageNumber] = useState(0);

  
  const products = useSelector(state => state.restaurant.products);
  const food__categories = useSelector(state => state.restaurant.foodCategories);
  const menu__categories = useSelector(state => state.restaurant.menuCategories);

  const [allProducts, setAllProducts] = useState(products);

  const searchedProduct = allProducts.filter((item) => {
    if (searchTerm.value === "") {
      return item;
    }
    if (item.food_name?.toLowerCase().includes(searchTerm.toLowerCase())) {
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
    food__categories.forEach(cat => {
      if (category === cat.food_category_name) {
        const filteredProducts = products.filter(
          (item) => item.food_category_id === cat.food_category_id
        );
  
        setAllProducts(filteredProducts);
      }
      
    });
  }, [category, products, food__categories]);

  const [visible, setVisible] = useState(false);

  const onDismiss = () => setVisible(false);

  return (
    <>
      {restaurant ?  
        <Helmet title="All-Foods">
        <CommonSection title={restaurant.restaurant_name} desc={restaurant.min_delivery_price} image={image01} />
  
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
                    {food__categories.map(cat => (
                      <button
                          className={`d-flex align-items-center gap-2 ${
                          category === cat.food_category_name ? "foodBtnActive" : ""
                          } `}
                          onClick={() => setCategory(cat.food_category_name)}
                      >
                          <img src={foodCategoryImg01} alt="" />
                          {cat.food_category_name}
                      </button>
                    ))}
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
                
                <Container>
                {menu__categories.map(cat => (
                  <Row className="mt-4">
                    <h2>{cat.menu_category_name}</h2>
                    {(displayPage && displayPage.filter(it => it.menu_category_id === cat.menu_category_id).length > 0) ? displayPage.filter(it => it.menu_category_id === cat.menu_category_id).map((item) => (
                        <Col lg="3" md="4" sm="6" xs="6" key={item.id} className="mt-3">
                            <ProductCard item={item} setVisible={setVisible} visible={visible}/>
                        </Col>
                    )) : <h5>No product</h5>}
                  </Row>

                ))}
                </Container>
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
        <Alert style={{ position:"fixed", bottom: "30px",  right:"30px"}} color="info" isOpen={visible} toggle={onDismiss}>
          Product added to cart!
        </Alert>
      </Helmet>
      : <h5>Loading...</h5>}
    </>
    
  );
};

export default RestaurantDetails;

import React, { useState, useEffect } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Alert,Button, Input } from "reactstrap";

import MenuItem from "../components/UI/menu-item/MenuItem";
import ReactPaginate from "react-paginate";

import foodCategoryImg01 from "../assets/images/hamburger.png";
import foodCategoryImg02 from "../assets/images/pizza.png";
import foodCategoryImg03 from "../assets/images/bread.png";

import { useParams } from "react-router-dom";

import "../styles/all-foods.css";
import "../styles/pagination.css";
import restaurants from "../assets/fake-data/restaurants";
import { useDispatch, useSelector } from "react-redux";
import { restaurantActions } from "../store/restaurant/restaurantSlice";

const RestaurantMenu = () => {
    const [category, setCategory] = useState("ALL");
  
    const { id } = useParams();
    const restaurant = restaurants.find((restaurant) => restaurant.id === id);
    
    const [searchTerm, setSearchTerm] = useState("");
    const [pageNumber, setPageNumber] = useState(0);
  
    const dispatch = useDispatch();
    useEffect(() => {
      dispatch(restaurantActions.getProducts())
    })
    
    const products = useSelector(state => state.restaurant.products);
    const [allProducts, setAllProducts] = useState(products);
  
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
    }, [category, products]);
  
    const [visible, setVisible] = useState(false);
  
    const onDismiss = () => setVisible(false);
  
    const [newProduct, setNewProduct] = useState({
      title: '',
      price: '',
      category: '',
    });
  
    const [newCategory, setNewCategory] = useState('');
  
  
    const deleteProduct = (productId) => {
      // Use an action from your Redux store to delete the product
      dispatch(restaurantActions.deleteProduct(productId));
    };
  
    const addProduct = () => {
      if (newProduct.title && newProduct.price && newProduct.category) {
        // dispatch(restaurantActions.addProduct(newProduct));
  
        setNewProduct({ title: '', price: '', category: '' });
      } else {
        console.error('All fields must be filled out to add a new product');
      }
    };
  
    const addCategory = () => {
      if (newCategory) {
        // dispatch(restaurantActions.addCategory(newCategory));
  
        setNewCategory('');
      } else {
        console.error('The category field must be filled out to add a new category');
      }
    };
  
    return (
        <Helmet title="Restaurant Menu">
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
        <MenuItem item={item} setVisible={setVisible} visible={visible}/>
      </Col>
    ))}  
    <Col lg="12" className="mt-5">
      <h5>Add New Product</h5>
      <Input 
        type="text" 
        placeholder="Product Title" 
        value={newProduct.title} 
        onChange={e => setNewProduct({ ...newProduct, title: e.target.value })} 
      />
      <Input 
        type="text" 
        placeholder="Product Price" 
        value={newProduct.price} 
        onChange={e => setNewProduct({ ...newProduct, price: e.target.value })} 
      />
      <Input 
        type="text" 
        placeholder="Product Category" 
        value={newProduct.category} 
        onChange={e => setNewProduct({ ...newProduct, category: e.target.value })} 
      />
      <Button color="primary" onClick={addProduct}>Add Product</Button>
    </Col>
  
    <Col lg="12" className="mt-5">
      <h5>Add New Category</h5>
      <Input 
        type="text" 
        placeholder="Category Name" 
        value={newCategory} 
        onChange={e => setNewCategory(e.target.value)} 
      />
      <Button color="primary" onClick={addCategory}>Add Category</Button>
    </Col>
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
  
  
  export default RestaurantMenu;
  
import React, { useState, useEffect } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Alert,Button, Input, Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from "reactstrap";

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
import { addNewFoodThunk, addNewMenuCatThunk, getAllFoodCategoryThunk, getAllFoodRestThunk, getAllMenuCategoryThunk, restaurantActions } from "../store/restaurant/restaurantSlice";

const RestaurantMenu = () => {
    const [category, setCategory] = useState("ALL");
  
    const { id } = useParams();
    const restaurant = useSelector(state => state.restaurant.myRestaurant);
    
    const [searchTerm, setSearchTerm] = useState("");
    const [pageNumber, setPageNumber] = useState(0);
    const [reload, setReload] = useState(false);
  
    const dispatch = useDispatch();
    useEffect(() => {
      dispatch(getAllFoodCategoryThunk({restaurant_id: restaurant.info.restaurant_id}));
      dispatch(getAllMenuCategoryThunk({restaurant_id: restaurant.info.restaurant_id}));
      dispatch(getAllFoodRestThunk({restaurant_id: restaurant.info.restaurant_id}));
    }, [dispatch, restaurant, reload]);
    
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
  
    const [newProduct, setNewProduct] = useState("");
    const [newProductPrice, setNewProductPrice] = useState(0);
    const [menuCategory, setMenuCategory] = useState("");
    const [foodCategory, setFoodCategory] = useState("");

  
    const [newCategory, setNewCategory] = useState('');
  
  
    const deleteProduct = (productId) => {
      // Use an action from your Redux store to delete the product
      dispatch(restaurantActions.deleteProduct(productId));
    };
  
    const addProduct = () => {
      if (newProduct && newProductPrice && menuCategory !== "0" && foodCategory !== "0") {
        const food = {
          food_category_id: foodCategory,
          restaurant_id: restaurant.info.restaurant_id,
          menu_category_id: menuCategory,
          food_name: newProduct,
          fixed_ingredients: "...",
          price: newProductPrice
        }

        dispatch(addNewFoodThunk({food}));
        setReload(!reload);
        setReload(!reload);

        setNewProduct("");
        setNewProductPrice(0);
        setMenuCategory("0");
        setFoodCategory("0");
      } else {
        console.error('All fields must be filled out to add a new product');
      }
    };
  
    const addCategory = () => {
      if (newCategory) {
        const cat = {
          restaurant_id: restaurant.info.restaurant_id,
          menu_category_name: newCategory,
        };
        dispatch(addNewMenuCatThunk({category: cat}));
  
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
                    <Row> 
                      <h2 className="mt-4">{cat.menu_category_name}</h2>
                      {(displayPage && displayPage.filter(it => it.menu_category_id === cat.menu_category_id).length > 0) ? displayPage.filter(it => it.menu_category_id === cat.menu_category_id).map((item) => (
                        <Col lg="3" md="4" sm="6" xs="6" key={item.id} className="mt-5">
                          <MenuItem item={item} setVisible={setVisible} visible={visible}/>
                        </Col>
                      )) : <h5>No product</h5>}  
                    </Row>
                  ))}
                  </Container>
    <Col lg="12" className="mt-5">
      <h5>Add New Product</h5>
      <Input 
        className="mb-4"
        type="text" 
        placeholder="Product Title" 
        value={newProduct} 
        onChange={e => setNewProduct(e.target.value)} 
      />
      <Input 
        className="mb-4"
        type="number" 
        placeholder="Product Price" 
        value={newProductPrice} 
        onChange={e => setNewProductPrice(e.target.value)} 
      />
      <div className="mb-4">
        Menu Category: 
        <select
          required
          value={menuCategory}
          onChange={e => setMenuCategory(e.target.value)}
        >
          <option value="0">Select Category</option>
          {menu__categories.map(cat => (
            <option key={cat.menu_category_id} value={cat.menu_category_id}>{cat.menu_category_name}</option>
          ))}
        </select>
      </div>
      <div className="mb-4">
        Food Category: 
        <select
          required
          value={foodCategory}
          onChange={e => setFoodCategory(e.target.value)}
        >
          <option value="0">Select Category</option>
          {food__categories.map(cat => (
            <option key={cat.food_category_id} value={cat.food_category_id}>{cat.food_category_name}</option>
          ))}
        </select>
      </div>
      <Button color="primary" onClick={addProduct}>Add Product</Button>
    </Col>
  
    <Col lg="12" className="mt-5">
      <h5>Add New Menu Category</h5>
      <Input 
      className="mt-4"
        type="text" 
        placeholder="Category Name" 
        value={newCategory} 
        onChange={e => setNewCategory(e.target.value)} 
      />
      <Button className="mt-4" color="primary" onClick={addCategory}>Add Category</Button>
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
  
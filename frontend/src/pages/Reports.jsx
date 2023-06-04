import React, { useEffect, useState } from "react";
import Helmet from "../components/Helmet/Helmet";
import CommonSection from "../components/UI/common-section/CommonSection";

import { Container, Row, Col, Button } from "reactstrap";


import "../styles/all-foods.css";
import "../styles/pagination.css";
import { useDispatch, useSelector } from "react-redux";
import { getRestaurantsThunk } from "../store/restaurant/restaurantSlice";
import { createReport1Thunk, createReport2Thunk, createReport3Thunk, createReport4Thunk, reportAllThunk, reportDeleteAllThunk } from "../store/authSlice";

const Reports = () => {

  const dispatch = useDispatch();

  const reports = useSelector((state) => state.auth.reports);

  useEffect(() => {
    console.log("reportt");
    dispatch(reportDeleteAllThunk());

    setTimeout(() => {
        dispatch(createReport1Thunk());
        dispatch(createReport2Thunk());
        dispatch(createReport3Thunk());
        dispatch(createReport4Thunk());
    }, 200)

    setTimeout(() => {
        dispatch(reportAllThunk());
    }, 500)
    
    
  }, [dispatch]);


  return (
    <Helmet title="Reports">
      <CommonSection title="Reports" />

      <section>
        <Container>
            {reports.map(report => (
                <Row>
                    <Col>
                        <h3>{report.report_type}</h3>
                        <span>{report.details}</span>
                    </Col>
                </Row>
            ))}

        </Container>
      </section>
    </Helmet>
  );
};

export default Reports;

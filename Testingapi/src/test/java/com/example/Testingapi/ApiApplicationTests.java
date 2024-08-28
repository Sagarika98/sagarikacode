package com.example.Testingapi;

import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiApplicationTests {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://bfhldevapigw.healthrx.co.in/automation-campus";
        RestAssured.basePath = "/create/user";
    }

    @Test
    public void testCreateUserWithValidData() {
        given()
            .header("roll-number", "1")
            .contentType("application/json")
            .body("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"phoneNumber\": 1234567890, \"emailId\": \"john.doe@test.com\" }")
        .when()
            .post()
        .then()
            .statusCode(200);  // Assuming successful creation returns 200 OK
    }

    @Test
    public void testCreateUserWithInvalidPhoneNumber() {
        given()
            .header("roll-number", "1")
            .contentType("application/json")
            .body("{ \"firstName\": \"Charlie\", \"lastName\": \"Brown\", \"phoneNumber\": \"invalid_phone\", \"emailId\": \"charlie.brown@test.com\" }")
        .when()
            .post()
        .then()
            .statusCode(400)  // Bad Request
            .body("error", equalTo("Invalid phone number format"));
    }

    @Test
    public void testCreateUserWithInvalidEmailId() {
        given()
            .header("roll-number", "1")
            .contentType("application/json")
            .body("{ \"firstName\": \"Daniel\", \"lastName\": \"Craig\", \"phoneNumber\": 4444444444, \"emailId\": \"invalid_email\" }")
        .when()
            .post()
        .then()
            .statusCode(400)  // Bad Request
            .body("error", equalTo("Invalid email ID format"));
    }


    @Test
    public void testCreateUserWithExtraFieldInBody() {
        given()
            .header("roll-number", "5")
            .contentType("application/json")
            .body("{ \"firstName\": \"Frank\", \"lastName\": \"Castle\", \"phoneNumber\": 6666666666, \"emailId\": \"frank.castle@test.com\", \"address\": \"123 Main St\" }")
        .when()
            .post()
        .then()
            .statusCode(400)
            .body("error", equalTo("Unexpected field: address"));
    }
}

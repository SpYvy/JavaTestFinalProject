package ru.gb.authorization;

import io.restassured.parsing.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class AuthorizationTest extends AuthorizationAbstractTest {

    @DisplayName("Проверка валидной авторизации")
    @Test
    void validCredentialsTest() {
        AuthorizationQueryResponse authorizationQueryResponse = given()
                .spec(getRequestSpecification())
                .formParam("username", getUserName())
                .formParam("password", getPassword())
                .expect().statusCode(200)
                .when()
                .post(getLoginUrl())
                .then()
                .parser("text/plain", Parser.JSON)// Ответ приходит в формате text/plain, мы его преобразуем в JSON
                .extract()
                .as(AuthorizationQueryResponse.class);
        assertThat(authorizationQueryResponse.getToken(), notNullValue());
        assertThat(authorizationQueryResponse.getUsername(), equalTo(getUserName()));
        System.out.println("X-Auth-Token: " + authorizationQueryResponse.getToken());
    }

    @DisplayName("Проверка валидной авторизации с длинным логином")
    @Test
    void validLongLoginCredentialsTest() {
        AuthorizationQueryResponse authorizationQueryResponse = given()
                .spec(getRequestSpecification())
                .formParam("username", getBigUserName())
                .formParam("password", getBigUserNamePassword())
                .expect().statusCode(200)
                .when()
                .post(getLoginUrl())
                .then()
                .parser("text/plain", Parser.JSON)// Ответ приходит в формате text/plain, мы его преобразуем в JSON
                .extract()
                .as(AuthorizationQueryResponse.class);
        assertThat(authorizationQueryResponse.getToken(), notNullValue());
        assertThat(authorizationQueryResponse.getUsername(), equalTo(getBigUserName()));
        System.out.println("X-Auth-Token: " + authorizationQueryResponse.getToken());
    }

    @DisplayName("Проверка авторизации с невалидным логином")
    @Test
    void invalidLoginValidPasswordCredentialsTest() { // Ответ приходит в формате application/json, преобразовывать не нужно
        AuthorizationBadCredentialsQueryResponse badCredentials = given()
                .spec(getRequestSpecification())
                .formParam("username", "invalidLogin")
                .formParam("password", getPassword())
                .expect().statusCode(401)
                .when()
                .post(getLoginUrl())
                .then()
                .extract()
                .as(AuthorizationBadCredentialsQueryResponse.class);
        assertThat(badCredentials.getCode(), equalTo(401));
        assertThat(badCredentials.getError(), equalTo("Invalid credentials."));
        System.out.println("Error: " + badCredentials.getError());
    }

    @DisplayName("Проверка авторизации с невалидным паролем")
    @Test
    void validLoginInvalidPasswordCredentialsTest() { // Ответ приходит в формате application/json, преобразовывать не нужно
        AuthorizationBadCredentialsQueryResponse badCredentials = given()
                .spec(getRequestSpecification())
                .formParam("username", getUserName())
                .formParam("password", "invalidPassword")
                .expect().statusCode(401)
                .when()
                .post(getLoginUrl())
                .then()
                .extract()
                .as(AuthorizationBadCredentialsQueryResponse.class);
        assertThat(badCredentials.getCode(), equalTo(401));
        assertThat(badCredentials.getError(), equalTo("Invalid credentials."));
        System.out.println("Error: " + badCredentials.getError());
    }
}





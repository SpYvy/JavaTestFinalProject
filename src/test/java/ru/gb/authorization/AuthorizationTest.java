package ru.gb.authorization;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class AuthirizationTest extends AbstractTest {
    @Test
        // Проверка валидной авторизации
    void validCredentialsTest() {
        RestAssured.registerParser("text/plain", Parser.JSON); // Ответ приходит в формате text/plain, мы его преобразуем в JSON
        AuthorizationQueryResponse authorizationQueryResponse = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", getUserName())
                .formParam("password", getPassword())
                .expect().statusCode(200)
                .when()
                .post(getLoginUrl())
                .then()
                .extract()
                .as(AuthorizationQueryResponse.class);
        assertThat(authorizationQueryResponse.getToken(), notNullValue());
        assertThat(authorizationQueryResponse.getUsername(), equalTo(getUserName()));
        System.out.println("X-Auth-Token: " + authorizationQueryResponse.getToken());
    }

    @Test
        // Проверка валидной авторизации
    void validLongLoginCredentialsTest() {
        RestAssured.registerParser("text/plain", Parser.JSON); // Ответ приходит в формате text/plain, мы его преобразуем в JSON
        AuthorizationQueryResponse authorizationQueryResponse = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", getBigUserName())
                .formParam("password", getBigUserNamePassword())
                .expect().statusCode(200)
                .when()
                .post(getLoginUrl())
                .then()
                .extract()
                .as(AuthorizationQueryResponse.class);
        assertThat(authorizationQueryResponse.getToken(), notNullValue());
        assertThat(authorizationQueryResponse.getUsername(), equalTo(getUserName()));
        System.out.println("X-Auth-Token: " + authorizationQueryResponse.getToken());
    }

    @Test
        // Проверка невалидидной авторизации с невалидным логином
    void invalidLoginValidPasswordCredentialsTest() { // Ответ приходит в формате application/json, преобразовывать не нужно
        AuthorizationBadCredentialsQueryResponse badCredentials = given()
                .contentType("application/x-www-form-urlencoded")
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

    @Test
        // Проверка невалидидной авторизации с невалидным паролем
    void validLoginInvalidPasswordCredentialsTest() { // Ответ приходит в формате application/json, преобразовывать не нужно
        AuthorizationBadCredentialsQueryResponse badCredentials = given()
                .contentType("application/x-www-form-urlencoded")
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





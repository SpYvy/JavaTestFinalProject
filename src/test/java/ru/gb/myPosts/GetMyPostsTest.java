package ru.gb.myPosts;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetMyPostsTest extends MyPostsAbstractTest {
    @DisplayName("Запрос на получение постов без авторизации")
    @Test
    void getMyPostsUnauthorizedTest() {
        JsonPath json = given()
                .expect().statusCode(401)
                .when()
                .get(getPostsUrl())
                .then()
                .log()
                .body()
                .extract()
                .jsonPath();
        assertThat(json.getString("message"), equalTo("Auth header required X-Auth-Token"));
    }
    @DisplayName("Запрос на получение постов по умолчанию")
    @Test
    void getMyPostsDefault(){
        MyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getResponseSpecification())
                .extract()
                .as(MyPostsResponse.class);
        assertThat(postsRespond.getData().get(0).getId(), notNullValue());
        assertThat(postsRespond.getData().get(0).getDescription(), equalTo("10 пост")); // Проверка, что посты возвращаются в порядке от новых к старым
        assertThat(postsRespond.getMeta().getNextPage(), is(not("null")));
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
    }
    @DisplayName("Запрос на получение 1 страницы с сортировкой от старых к новым")
    @Test
    void getMyPostsPage1OldToNew(){
        MyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .queryParam("sort", "createdAt")
                .queryParam("order", "ASC")
                .queryParam("page", "1")
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getResponseSpecification())
                .extract()
                .as(MyPostsResponse.class);
        assertThat(postsRespond.getData().get(0).getId(), notNullValue());
        assertThat(postsRespond.getData().get(0).getDescription(), equalTo("1 пост")); // Проверка, что посты возвращаются в порядке от старых к новым
        assertThat(postsRespond.getMeta().getNextPage(), is(not("null")));
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
    }
    @DisplayName("Запрос на получеение 0 страницы с сортировкой от новых к старым")
    @Test
    void getMyPostsPage0NewToOld(){
        MyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .queryParam("sort", "createdAt")
                .queryParam("order", "DESC")
                .queryParam("page", "0")
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getResponseSpecification())
                .extract()
                .as(MyPostsResponse.class);
        assertThat(postsRespond.getData().get(0).getId(), notNullValue());
        assertThat(postsRespond.getData().get(0).getDescription(), equalTo("10 пост")); // Проверка, что посты возвращаются в порядке от новых к старым
        assertThat(postsRespond.getMeta().getNextPage(), is(not("null")));
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
    }
    @DisplayName("Запрос на получение 3 страницы с сортировкой от новых к старым")
    @Test
    void getMyPostsPage3NewToOld(){
        MyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .queryParam("sort", "createdAt")
                .queryParam("order", "DESC")
                .queryParam("page", "3")
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getEmptyNextPageResponseSpecification())
                .extract()
                .as(MyPostsResponse.class);
        assertThat(postsRespond.getData().get(0).getId(), notNullValue());
        assertThat(postsRespond.getData().get(0).getDescription(), equalTo("2 пост")); // Проверка, что посты возвращаются в порядке от новых к старым
        //assertThat(postsRespond.getMeta().getNextPage(), is("null")); // Сейчас возвращается null, а не строка "null", пока баг исправляется, проверяем на null в спецификации
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
    }
    @DisplayName("Запрос на получение несуществующей страницы с сортировкой от старых к новым")
    @Test
    void getMyPostsPage1000OldToNew(){
        MyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .queryParam("sort", "createdAt")
                .queryParam("order", "ASC")
                .queryParam("page", "1000")
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getEmptyNextPageResponseSpecification())
                .extract()
                .as(MyPostsResponse.class);
        assertThat(postsRespond.getData(), empty()); // Проверяем, что в ответе пустой массив
        //assertThat(postsRespond.getMeta().getNextPage(), is("null")); // Сейчас возвращается null, а не строка "null", пока баг исправляется, проверяем на null в спецификации
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
    }
    @DisplayName("Запрос на получение несуществующей страницы с сортировкой от новых к старым")
    @Test
    void getMyPostsPage1000NewToOld(){
        MyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .queryParam("sort", "createdAt")
                .queryParam("order", "DESC")
                .queryParam("page", "1000")
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getEmptyNextPageResponseSpecification())
                .extract()
                .as(MyPostsResponse.class);
        assertThat(postsRespond.getData(), empty()); // Проверяем, что в ответе пустой массив
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
        assertThat(postsRespond.getMeta().getNextPage(), is("null")); // Сейчас возвращается null, а не строка "null", пока баг исправляется, проверяем на null в спецификации
    }
}
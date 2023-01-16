package ru.gb.notMyPosts;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetNotMyPostsTest extends NotMyPostsAbstractTest {
    private boolean isResultRandom(NotMyPostsResponse responseData) { // Метод поможет определить, что посты в ответе в случайном порядке
        boolean result = false;
        for(int i = 0; i < 4; i++){
            if(responseData.getData().get(i).getId() >= 50 && responseData.getData().get(i).getId() <= 10000){
                result = true;
                break;
            }
        }
        return result;
    }
    @DisplayName("Запрос на получение постов без авторизации")
    @Test
    void getNotMyPostsUnauthorizedTest() {
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
    void getNotMyPostsDefault(){
        NotMyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getResponseSpecification())
                .extract()
                .as(NotMyPostsResponse.class);
        assertThat(postsRespond.getData().get(0).getId(), notNullValue());
        assertThat(postsRespond.getData().get(0).getId(), greaterThan(10000));
        assertThat(postsRespond.getMeta().getNextPage(), is(not("null")));
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
    }

    @DisplayName("Запрос на получение 0 страницы с сортировкой от новых к старым")
    @Test
    void getNotMyPostsPage0SortNewToOld(){
        NotMyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .queryParam("sort", "createdAt")
                .queryParam("order", "DESC")
                .queryParam("page", 0)
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getResponseSpecification())
                .extract()
                .as(NotMyPostsResponse.class);
        assertThat(postsRespond.getData().get(0).getId(), notNullValue());
        assertThat(postsRespond.getData().get(0).getId(), greaterThan(10000));
        assertThat(postsRespond.getMeta().getNextPage(), is(not("null")));
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
    }

    @DisplayName("Запрос на получение 500 страницы с сортировкой от новых к старым")
    @Test
    void getNotMyPostsPage500SortNewToOld(){
        NotMyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .queryParam("sort", "createdAt")
                .queryParam("order", "DESC")
                .queryParam("page", 500)
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getResponseSpecification())
                .extract()
                .as(NotMyPostsResponse.class);
        assertThat(postsRespond.getData().get(0).getId(), notNullValue());
        assertThat(postsRespond.getData().get(0).getId(), lessThan(10000));
        assertThat(postsRespond.getMeta().getNextPage(), is(not("null")));
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
    }

    @DisplayName("Запрос на получение 1 страницы со случайной сортировкой")
    @Test
    void getNotMyPostsPage1SortRandom(){
        NotMyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .queryParam("sort", "createdAt")
                .queryParam("order", "ALL") // С этим параметром код ответа всегда 500 Internal Server Error
                .queryParam("page", 1)
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getResponseSpecification())
                .extract()
                .as(NotMyPostsResponse.class);
        assertThat(isResultRandom(postsRespond), is(true)); // Проверяем, что посты в ответе в случайном порядке
        assertThat(postsRespond.getData().get(0).getId(), notNullValue());
        assertThat(postsRespond.getMeta().getNextPage(), is(not("null")));
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
    }

    @DisplayName("Запрос на получение 100 страницы с сортировкой от старых к новым")
    @Test
    void getNotMyPostsPage100SortOldToNew(){
        NotMyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .queryParam("sort", "createdAt")
                .queryParam("order", "ASC")
                .queryParam("page", 100)
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getResponseSpecification())
                .extract()
                .as(NotMyPostsResponse.class);
        assertThat(postsRespond.getData().get(0).getId(), notNullValue());
        assertThat(postsRespond.getData().get(0).getId(), lessThan(1000));
        assertThat(postsRespond.getMeta().getNextPage(), is(not("null")));
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
    }

    @DisplayName("Запрос на получение несуществующей страницы с сортировкой от старых к новым")
    @Test
    void getNotMyPostsPage5000SortOldToNew(){
        NotMyPostsResponse postsRespond = given()
                .spec(getRequestSpecification())
                .queryParam("sort", "createdAt")
                .queryParam("order", "ASC")
                .queryParam("page", 5000)
                .when()
                .get(getPostsUrl())
                .then()
                .spec(getEmptyNextPageResponseSpecification())
                .extract()
                .as(NotMyPostsResponse.class);
        assertThat(postsRespond.getData(), empty()); // Проверяем, что в ответе пустой массив
        assertThat(postsRespond.getMeta().getCount(), notNullValue());
        assertThat(postsRespond.getMeta().getNextPage(), equalTo("null")); // Сейчас возвращается null, а не строка "null", пока баг исправляется, проверяем на null в спецификации
    }
}





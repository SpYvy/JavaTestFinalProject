package ru.gb.notMyPosts;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class AuthorizationTest extends NotMyPostsAbstractTest {
    @Test
    void getNotMyPostDefault(){
        NotMyPostsRequest postsRespond = given()
                .queryParam("owner", "notMe")
                .expect().statusCode(200)
                .when()
                .get(getPostsUrl())
                .then()
                .extract()
                .as(NotMyPostsRequest.class);
        System.out.println(postsRespond);
        assertThat(postsRespond.getData().get(0).title, notNullValue());
    }
}





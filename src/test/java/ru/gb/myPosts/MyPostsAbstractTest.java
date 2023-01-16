package ru.gb.myPosts;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class MyPostsAbstractTest {
    static Properties prop = new Properties();
    private static String postsUrl;
    protected static RequestSpecification requestSpecification;
    protected static ResponseSpecification responseSpecification;
    protected static ResponseSpecification emptyNextPageResponseSpecification;
    public static RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }
    public static ResponseSpecification getResponseSpecification() {
        return responseSpecification;
    }

    public static ResponseSpecification getEmptyNextPageResponseSpecification() {
        return emptyNextPageResponseSpecification;
    }

    @BeforeAll
    static void initTest() throws IOException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        InputStream configFile = new FileInputStream("src/main/resources/my.properties");
        prop.load(configFile);

        postsUrl = prop.getProperty("postsUrl");

        requestSpecification = new RequestSpecBuilder()
                .addHeader("X-Auth-Token", "e67dac510312944c106b7cc62f4dc65f")
                .log(LogDetail.ALL)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectBody("meta.nextPage", Matchers.notNullValue())
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();

        emptyNextPageResponseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectBody("meta.nextPage", Matchers.nullValue())
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();
    }
    public static String getPostsUrl() {
        return postsUrl;
    }
}


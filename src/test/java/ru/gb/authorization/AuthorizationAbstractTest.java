package ru.gb.authorization;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class AuthorizationAbstractTest {

    static Properties prop = new Properties();
    private static InputStream configFile;
    private static String loginUrl;
    private static String userName;
    private static String bigUserName;
    private static String password;
    private static String bigUserNamePassword;
    protected static ResponseSpecification responseSpecification;
    public static RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }

    protected static RequestSpecification requestSpecification;

    @BeforeAll
    static void initTest() throws IOException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        configFile = new FileInputStream("src/main/resources/my.properties");
        prop.load(configFile);

        loginUrl = prop.getProperty("loginUrl");
        userName = prop.getProperty("userName");
        bigUserName = prop.getProperty("bigUserName");
        password = prop.getProperty("password");
        bigUserNamePassword = prop.getProperty("bigUserNamePassword");

        requestSpecification = new RequestSpecBuilder()
                .setContentType("application/x-www-form-urlencoded")
                .log(LogDetail.ALL)
                .build();
    }

    public static String getLoginUrl() {
        return loginUrl;
    }
    public static String getUserName() {
        return userName;
    }
    public static String getBigUserName() {
        return bigUserName;
    }
    public static String getPassword() {
        return password;
    }
    public static String getBigUserNamePassword() {
        return bigUserNamePassword;
    }
}

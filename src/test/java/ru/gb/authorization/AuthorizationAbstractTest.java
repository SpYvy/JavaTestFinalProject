package ru.gb.authorization;

import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class AbstractTest {

    static Properties prop = new Properties();
    private static InputStream configFile;
    private static String loginUrl;
    private static String userName;
    private static String bigUserName;
    private static String password;
    private static String bigUserNamePassword;

    @BeforeAll
    static void initTest() throws IOException {
        configFile = new FileInputStream("src/main/resources/my.properties");
        prop.load(configFile);

        loginUrl = prop.getProperty("loginUrl");
        userName = prop.getProperty("userName");
        bigUserName = prop.getProperty("bigUserName");
        password = prop.getProperty("password");
        bigUserNamePassword = prop.getProperty("bigUserNamePassword");
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

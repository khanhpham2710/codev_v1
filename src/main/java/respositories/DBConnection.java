package respositories;

import config.ConfigProperties;
import enums.EPropertyKey;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String URL = ConfigProperties.getInstance().getProperties(EPropertyKey.CONNECTION_STRING);
    private static final String USER = ConfigProperties.getInstance().getProperties(EPropertyKey.DB_USERNAME);
    private static final String PASSWORD = ConfigProperties.getInstance().getProperties(EPropertyKey.DB_PASSWORD);

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
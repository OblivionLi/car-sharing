package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/";
    private static final String USER = ""; //sa
    private static final String PASS = "";

    public static Connection getConnection(String databaseFileName) throws SQLException {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL + databaseFileName, USER, PASS);
            conn.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException se) {
            se.printStackTrace();
        }

        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void createCompanyTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(255) UNIQUE NOT NULL)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createCarTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(255) UNIQUE NOT NULL," +
                    "COMPANY_ID INT NOT NULL," +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID))"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createCustomerTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(255) UNIQUE NOT NULL," +
                    "RENTED_CAR_ID INT NULL," +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR (ID))"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package crm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLLoader {

    private static final String URL = "jdbc:mysql://localhost:3306/crm_warehouse";

    private static final String USER = "root";

    private static final String PASSWORD = "root12";

    // private static final String PASSWORD = System.getenv("CRM_DB_PASSWORD");

    public Connection connect() throws SQLException {

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

        System.out.println("Database connection successful.");

        return connection;
    }
}
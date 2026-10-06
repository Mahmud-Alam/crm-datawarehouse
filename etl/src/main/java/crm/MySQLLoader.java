package crm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class MySQLLoader {

    private static final String URL = "jdbc:mysql://localhost:3306/crm_warehouse";

    private static final String USER = "root";

    private static final String PASSWORD = "root12";

    public Connection connect() throws Exception {

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

        System.out.println("Database connection successful.");

        return connection;
    }

    public void loadProducts(Connection connection,
            List<String[]> products) throws Exception {

        String sql = "INSERT INTO products (product, series, sales_price) " +
                "VALUES (?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        for (String[] row : products) {

            statement.setString(1, row[0]);
            statement.setString(2, row[1]);
            statement.setDouble(3, Double.parseDouble(row[2]));

            statement.executeUpdate();
        }

        statement.close();

        System.out.println("Products loaded: " + products.size());
    }
}
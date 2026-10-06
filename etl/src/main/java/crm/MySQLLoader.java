package crm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

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

    public void loadAccounts(Connection connection,
            List<Map<String, String>> accounts,
            DataTransformer transformer) throws Exception {

        String sql = "INSERT INTO accounts " +
                "(account, sector, year_established, revenue, employees, " +
                "office_location, subsidiary_of) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        for (Map<String, String> row : accounts) {

            statement.setString(
                    1,
                    transformer.cleanText(row.get("account")));

            statement.setString(
                    2,
                    transformer.cleanSector(row.get("sector")));

            statement.setObject(
                    3,
                    transformer.cleanInteger(
                            row.get("year_established")));

            statement.setObject(
                    4,
                    transformer.cleanDecimal(
                            row.get("revenue")));

            statement.setObject(
                    5,
                    transformer.cleanInteger(
                            row.get("employees")));

            statement.setString(
                    6,
                    transformer.cleanText(
                            row.get("office_location")));

            statement.setString(
                    7,
                    transformer.cleanText(
                            row.get("subsidiary_of")));

            statement.executeUpdate();
        }

        statement.close();

        System.out.println("Accounts loaded: " + accounts.size());
    }

    public void loadSalesTeams(Connection connection,
            List<Map<String, String>> teams,
            DataTransformer transformer) throws Exception {

        String sql = "INSERT INTO sales_teams " +
                "(sales_agent, manager, regional_office) " +
                "VALUES (?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        for (Map<String, String> row : teams) {

            statement.setString(
                    1,
                    transformer.cleanText(
                            row.get("sales_agent")));

            statement.setString(
                    2,
                    transformer.cleanText(
                            row.get("manager")));

            statement.setString(
                    3,
                    transformer.cleanText(
                            row.get("regional_office")));

            statement.executeUpdate();
        }

        statement.close();

        System.out.println("Sales teams loaded: " + teams.size());
    }

    public int findProductId(Connection connection,
            String product) throws Exception {

        String sql = "SELECT product_id FROM products WHERE product = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, product);

        ResultSet result = statement.executeQuery();

        if (result.next()) {
            int id = result.getInt("product_id");

            result.close();
            statement.close();

            return id;
        }

        result.close();
        statement.close();

        return 0;
    }

    public int findAccountId(Connection connection,
            String account) throws Exception {

        String sql = "SELECT account_id FROM accounts WHERE account = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, account);

        ResultSet result = statement.executeQuery();

        if (result.next()) {
            int id = result.getInt("account_id");

            result.close();
            statement.close();

            return id;
        }

        result.close();
        statement.close();

        return 0;
    }

    public int findSalesAgentId(Connection connection,
            String salesAgent) throws Exception {

        String sql = "SELECT sales_agent_id FROM sales_teams " +
                "WHERE sales_agent = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, salesAgent);

        ResultSet result = statement.executeQuery();

        if (result.next()) {
            int id = result.getInt("sales_agent_id");

            result.close();
            statement.close();

            return id;
        }

        result.close();
        statement.close();

        return 0;
    }
}
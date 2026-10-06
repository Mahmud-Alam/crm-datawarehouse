package crm;

import java.sql.Connection;
import java.util.List;

public class TestMySQLLoader {

    public static void main(String[] args) {

        try {

            MySQLLoader loader = new MySQLLoader();

            Connection connection = loader.connect();

            CSVParser parser = new CSVParser();

            List<String[]> products = parser.readCSV(
                    "data/products.csv");

            loader.loadProducts(connection, products);

            connection.close();

            System.out.println("Product loading test passed.");

        } catch (Exception e) {

            System.out.println("Product loading test failed.");

            e.printStackTrace();
        }
    }
}
package crm;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class TestMySQLLoader {

    public static void main(String[] args) {

        try {

            MySQLLoader loader = new MySQLLoader();

            Connection connection = loader.connect();

            CSVParser csvParser = new CSVParser();

            XMLParser xmlParser = new XMLParser();

            DataTransformer transformer = new DataTransformer();

            // Products
            List<String[]> products = csvParser.readCSV(
                    "D:/projects/crm-datawarehouse/data/products.csv");

            loader.loadProducts(connection, products);

            // Accounts
            List<Map<String, String>> accounts = xmlParser.readXML(
                    "D:/projects/crm-datawarehouse/data/accounts.xml",
                    "account");

            loader.loadAccounts(
                    connection,
                    accounts,
                    transformer);

            // Sales teams
            List<Map<String, String>> teams = xmlParser.readXML(
                    "D:/projects/crm-datawarehouse/data/sales_teams.xml",
                    "row");

            loader.loadSalesTeams(
                    connection,
                    teams,
                    transformer);

            List<String[]> pipeline = csvParser.readCSV(
                    "D:/projects/crm-datawarehouse/data/sales_pipeline.csv");

            loader.loadSalesPipeline(
                    connection,
                    pipeline,
                    transformer);

            connection.close();

            System.out.println("ETL loading test passed.");

        } catch (Exception e) {

            System.out.println("ETL loading test failed.");

            e.printStackTrace();
        }
    }
}
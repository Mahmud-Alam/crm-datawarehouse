package crm;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class ETL_Main {

    public static void main(String[] args) {

        String dataPath = "D:/projects/crm-datawarehouse/data/";

        try {

            System.out.println("CRM ETL starting...");

            CSVParser csvParser = new CSVParser();
            XMLParser xmlParser = new XMLParser();
            DataTransformer transformer = new DataTransformer();
            MySQLLoader loader = new MySQLLoader();

            Connection connection = loader.connect();

            // 1. Load products
            List<String[]> products = csvParser.readCSV(
                    dataPath + "products.csv");

            loader.loadProducts(
                    connection,
                    products);

            // 2. Load accounts
            List<Map<String, String>> accounts = xmlParser.readXML(
                    dataPath + "accounts.xml",
                    "account");

            loader.loadAccounts(
                    connection,
                    accounts,
                    transformer);

            // 3. Load sales teams
            List<Map<String, String>> teams = xmlParser.readXML(
                    dataPath + "sales_teams.xml",
                    "row");

            loader.loadSalesTeams(
                    connection,
                    teams,
                    transformer);

            // 4. Load sales pipeline
            List<String[]> pipeline = csvParser.readCSV(
                    dataPath + "sales_pipeline.csv");

            loader.loadSalesPipeline(
                    connection,
                    pipeline,
                    transformer);

            connection.close();

            System.out.println("CRM ETL completed successfully.");

        } catch (Exception e) {

            System.out.println("CRM ETL failed.");

            e.printStackTrace();
        }
    }
}
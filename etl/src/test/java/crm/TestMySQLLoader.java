package crm;

import java.sql.Connection;

public class TestMySQLLoader {

    public static void main(String[] args) {

        MySQLLoader loader = new MySQLLoader();

        try {
            Connection connection = loader.connect();

            connection.close();

            System.out.println("Connection test passed.");

        } catch (Exception e) {
            System.out.println("Connection test failed.");
            e.printStackTrace();
        }
    }
}
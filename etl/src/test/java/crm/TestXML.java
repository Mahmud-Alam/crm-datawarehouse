package crm;

import java.util.List;
import java.util.Map;

public class TestXML {

    public static void main(String[] args) throws Exception {

        XMLParser parser = new XMLParser();

        List<Map<String, String>> accounts = parser.readXML("data/accounts.xml", "account");

        System.out.println("Accounts loaded: " + accounts.size());

        for (Map<String, String> row : accounts) {
            System.out.println(row);
        }

        List<Map<String, String>> salesTeams = parser.readXML("data/sales_teams.xml", "row");

        System.out.println("\nSales teams loaded: " + salesTeams.size());

        for (Map<String, String> row : salesTeams) {
            System.out.println(row);
        }
    }
}
package crm;

import java.util.List;

public class TestCSV {

    public static void main(String[] args) throws Exception {

        CSVParser parser = new CSVParser();

        List<String[]> rows = parser.readCSV("data/products.csv");

        System.out.println("Rows loaded: " + rows.size());

        for (String[] row : rows) {
            System.out.println(String.join(" | ", row));
        }
    }
}
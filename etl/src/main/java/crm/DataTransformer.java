package crm;

public class DataTransformer {

    public String cleanText(String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    public String cleanSector(String sector) {

        String value = cleanText(sector);

        if (value.equalsIgnoreCase("technolgy")) {
            return "technology";
        }

        return value;
    }
}
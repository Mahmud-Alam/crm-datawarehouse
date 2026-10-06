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

    public String cleanProductName(String product) {

        String value = cleanText(product);

        if (value.equalsIgnoreCase("GTXPro")) {
            return "GTX Pro";
        }

        return value;
    }

    public Integer cleanInteger(String value) {

        String cleaned = cleanText(value);

        if (cleaned.isEmpty()) {
            return null;
        }

        return Integer.parseInt(cleaned);
    }
}
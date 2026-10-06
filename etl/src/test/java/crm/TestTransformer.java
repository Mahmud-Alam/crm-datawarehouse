package crm;

public class TestTransformer {

        public static void main(String[] args) {

                DataTransformer transformer = new DataTransformer();

                System.out.println(
                                transformer.cleanText("  Acme Corporation  "));

                System.out.println(
                                transformer.cleanText("   technology   "));

                System.out.println(
                                "[" + transformer.cleanText(null) + "]");

                System.out.println(
                                transformer.cleanSector("technolgy"));

                System.out.println(
                                transformer.cleanSector(" medical "));

                System.out.println(
                                transformer.cleanProductName("GTXPro"));

                System.out.println(
                                transformer.cleanProductName(" GTX Pro "));

                System.out.println(
                                transformer.cleanInteger("1996"));

                System.out.println(
                                transformer.cleanInteger(" 2822 "));

                System.out.println(
                                transformer.cleanInteger(""));

                System.out.println(
                                transformer.cleanDecimal("1100.04"));

                System.out.println(
                                transformer.cleanDecimal(" 251.41 "));

                System.out.println(
                                transformer.cleanDecimal(""));

                System.out.println(
                                transformer.cleanDate("2025-10-15"));

                System.out.println(
                                transformer.cleanDate(""));
        }
}
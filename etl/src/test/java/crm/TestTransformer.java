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
        }
}
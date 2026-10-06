package crm;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLParser {

    public List<Map<String, String>> readXML(
            String filePath,
            String recordTag) throws Exception {

        List<Map<String, String>> rows = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(filePath);

        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName(recordTag);

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                Map<String, String> row = new HashMap<>();

                NodeList children = element.getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {

                    Node child = children.item(j);

                    if (child.getNodeType() == Node.ELEMENT_NODE) {

                        String fieldName = child.getNodeName();

                        String value = child.getTextContent().trim();

                        row.put(fieldName, value);
                    }
                }

                rows.add(row);
            }
        }

        return rows;
    }
}
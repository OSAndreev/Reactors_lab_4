package ReactorImport;

import Reactors.Reactor;
import Reactors.ReactorsOwner;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLReactorImporter extends ReactorImporterBase {

    @Override
    public void processFile(File file, ReactorsOwner reactorsOwner) {
        if (file.getName().endsWith(".xml")) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                try (var fis = new FileInputStream(file)) {
                    Document doc = builder.parse(fis);
                    doc.getDocumentElement().normalize();

                    NodeList nodeList = doc.getDocumentElement().getChildNodes();

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) nodeList.item(i);
                            Reactor reactor = parseReactor(element);

                            reactorsOwner.addReactor(element.getNodeName(), reactor);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (successor != null) {
            successor.processFile(file, reactorsOwner);
        } else {
            System.out.println("Не удается распознать формат файла");
        }
    }

    private Reactor parseReactor(Element element) {
        String type = element.getNodeName();
        Map<String, String> attributes = getAttributes(element);
        return new Reactor(
                type,
                attributes.get("class"),
                parseDouble(attributes, "burnup"),
                parseDouble(attributes, "kpd"),
                parseDouble(attributes, "enrichment"),
                parseDouble(attributes, "termal_capacity"),
                parseDouble(attributes, "electrical_capacity"),
                parseInt(attributes, "life_time"),
                parseDouble(attributes, "first_load"),
                "XML"
        );
    }

    private Map<String, String> getAttributes(Element element) {
        Map<String, String> attributes = new HashMap<>();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                attributes.put(childElement.getNodeName(), childElement.getTextContent());
            }
        }
        return attributes;
    }

    private Double parseDouble(Map<String, String> attributes, String key) {
        return attributes.containsKey(key) ? Double.parseDouble(attributes.get(key)) : null;
    }

    private Integer parseInt(Map<String, String> attributes, String key) {
        return attributes.containsKey(key) ? Integer.parseInt(attributes.get(key)) : null;
    }
}
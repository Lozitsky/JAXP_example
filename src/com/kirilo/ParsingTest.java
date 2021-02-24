package com.kirilo;

import com.kirilo.parsers.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParsingTest {
    public static void main(String[] args) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(ClassLoader.getSystemResourceAsStream("xml/people.xml"));
            prettyPrint(document);
            DomParser parser = new DomParser(document);
            List<Node> nodes = parser.getAllNodes();
            showNameAndValue(nodes, parser);
            parser.delAllNodes("date");
            prettyPrint(document);

            parser.addNode("person","date", LocalDate.now().toString());
//            document.normalize();
            prettyPrint(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void prettyPrint(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        System.out.println(out.toString());
    }

    private static void showNameAndValue(List<Node> nodes, DomParser parser) {
        for (Node node : nodes) {
            String nodeName = node.getNodeName();
            if (!nodeName.equals("#text")) {
                Map<String, String> map = parser.getAttributes(node.getAttributes());
//                https://www.baeldung.com/java-map-to-string-conversion
                String collect = "(%s)".formatted(map.keySet().stream().map(par -> par + "=" + map.get(par)).collect(Collectors.joining(", ", "", "")));
                System.out.println(nodeName + (nodeName.equals("person") ? collect : ""));
            }
            String nodeValue = node.getNodeValue();
            if (nodeValue != null && nodeValue.trim().length() > 0) {
                System.out.println(node.getNodeValue());
            }
        }
    }


}

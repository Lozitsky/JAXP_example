package com.kirilo;

import com.kirilo.parsers.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kirilo.XMLUtil.*;

public class DomParserTest {
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
            parser.delAllTags("date");
            prettyPrint(document);

            parser.addTag("person", "date", LocalDate.now().toString());
//            document.normalize();
            prettyPrint(document);

//            parser.getAllTagValuePairs().forEach((s, strings) -> System.out.println(s + " : " + strings));
            parser.getAllTagValuePairs("people").forEach((s, strings) -> System.out.println(s + " : " + strings));
            parser.getAllAttrValuePairs("person").forEach((s, strings) -> System.out.println(s + " : " + strings));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

package com.kirilo;

import com.kirilo.parsers.SAXHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SAXParserTest {
    public static void main(String[] args) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            SAXHandler handler = new SAXHandler();
            parser.parse(ClassLoader.getSystemResourceAsStream("xml/people.xml"), handler);
            List<Map<String, String>> lists = handler.getLists();
            lists.forEach(map ->
                    System.out.printf("{%s}%n", map.keySet()
                            .stream().map(tag -> tag + "=" + map.get(tag)).collect(Collectors.joining(", ", "", "")))
            );
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

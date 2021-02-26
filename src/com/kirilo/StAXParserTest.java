package com.kirilo;

import com.kirilo.parsers.StAXParser;

import javax.xml.stream.*;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.io.*;
import java.time.LocalDate;

public class StAXParserTest {
    public static void main(String[] args) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(ClassLoader.getSystemResourceAsStream("xml/people.xml"));
            StAXParser parser = new StAXParser(reader);
            parser.getAllTagValuePairs("people").forEach((s, strings) -> System.out.println(s + " : " + strings));

            parser = new StAXParser(factory.createXMLStreamReader(ClassLoader.getSystemResourceAsStream("xml/people.xml")));
            parser.getAllAttrValuePairs("person").forEach((s, strings) -> System.out.println(s + " : " + strings));

            Writer out = new StringWriter();
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLEventWriter writer = outputFactory.createXMLEventWriter(out);
            parser = new StAXParser(factory.createXMLEventReader(ClassLoader.getSystemResourceAsStream("xml/people.xml")), writer);
            parser.delAllTags("date");
            String xmlString = out.toString();
            System.out.println(xmlString);

            out = new StringWriter();
            parser = new StAXParser(factory.createXMLEventReader(
                    new ByteArrayInputStream(xmlString.getBytes())),
                    XMLOutputFactory.newInstance().createXMLEventWriter(out)
            );
            parser.addTag("person", "date", LocalDate.now().toString());
            System.out.println(out.toString());

        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}

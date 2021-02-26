package com.kirilo.parsers;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.*;

import static javax.xml.stream.XMLStreamConstants.*;

public class StAXParser implements XMLParser {

    public XMLStreamReader getXMLStreamReader() {
        if (reader instanceof XMLStreamReader) {
            return (XMLStreamReader) reader;
        }
        return null;
    }

    public XMLEventReader getXMLEventReader() {
        if (reader instanceof XMLEventReader) {
            return (XMLEventReader) reader;
        }
        return null;
    }

    private final Object reader;
    private XMLEventWriter writer;

    public StAXParser(XMLStreamReader reader) {
        this.reader = reader;
    }

    public StAXParser(XMLEventReader reader, XMLEventWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }


    @Override
    public void delAllTags(String tag) {
        XMLEventReader reader = getXMLEventReader();
        boolean insideTag = false;
        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement() && tag.equals(event.asStartElement().getName().getLocalPart())) {
                    insideTag = true;
                } else if (event.isEndElement() && tag.equals(event.asEndElement().getName().getLocalPart())) {
                    insideTag = !insideTag;
                } else if (!insideTag) {
                    writer.add(event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int addTag(String parent, String name, String value) {
        XMLEventReader reader = getXMLEventReader();
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartDocument()) {
                } else if (event.isStartElement()) {
                } else if (event.isEndElement()) {
                    if (parent.equals(event.asEndElement().getName().getLocalPart())) {
                        XMLEvent end = eventFactory.createDTD("\n");
                        XMLEvent tab = eventFactory.createDTD("\t");
                        StartElement sElement = eventFactory.createStartElement("", "", name);
                        writer.add(tab);
                        writer.add(sElement);
                        Characters characters = eventFactory.createCharacters(value);
                        writer.add(characters);
                        EndElement eElement = eventFactory.createEndElement("", "", name);
                        writer.add(eElement);
                        writer.add(end);
                        writer.add(tab);
                    }
                }
                writer.add(event);
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Map<String, List<String>> getAllTagValuePairs() {
        return getAllTagValuePairs("people");
    }

    @Override
    public Map<String, List<String>> getAllTagValuePairs(String tag) {
        XMLStreamReader reader = getXMLStreamReader();
        Map<String, List<String>> listMap = new TreeMap<>();
        List<String> list = new ArrayList<>();

        try {
            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case START_DOCUMENT:
                        break;
                    case START_ELEMENT:
                        list.add(reader.getLocalName());
                        break;
                    case CHARACTERS:
                        String value = reader.getText().trim();
                        int last = list.size() - 1;
                        List<String> strings = new ArrayList<>();
                        if (listMap.containsKey(list.get(last))) {
                            strings = listMap.get(list.get(last));
                        }
                        strings.add(value);
                        listMap.put(list.get(last), strings);
                        break;
                    case END_ELEMENT:
                        list.remove(list.size() - 1);
                        break;
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return listMap;
    }

    @Override
    public Map<String, List<String>> getAllAttrValuePairs(String tag) {
        XMLStreamReader reader = getXMLStreamReader();
        Map<String, List<String>> map = new HashMap<>();
        try {
            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case START_DOCUMENT:
                        break;
                    case START_ELEMENT:
                        if (tag.equals(reader.getLocalName())) {
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                List<String> list = new ArrayList<>();
                                String value = reader.getAttributeValue(i);
                                String key = reader.getAttributeLocalName(i);
                                if (map.containsKey(key)) {
                                    list = map.get(key);
                                }
                                list.add(value);
                                map.put(key, list);
                            }
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return map;
    }
}

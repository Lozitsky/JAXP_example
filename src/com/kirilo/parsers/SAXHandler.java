package com.kirilo.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

public class SAXHandler extends DefaultHandler {

    private Map<String, String> map;
    private List<String> list = new ArrayList<>();
    private List<Map<String, String>> lists = new ArrayList<>();


    public List<Map<String, String>> getLists() {
        return lists;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("person") || qName.equals("people")) {
            map = new HashMap<>();
        }
        list.add(qName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        list.remove(list.size() - 1);
        if (qName.equals("person") || qName.equals("people")) {
            lists.add(map);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = String.copyValueOf(ch, start, length).trim();
        map.put(list.get(list.size() - 1), value);
    }
}

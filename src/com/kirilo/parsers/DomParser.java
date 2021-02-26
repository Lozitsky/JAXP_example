package com.kirilo.parsers;

import org.w3c.dom.*;

import java.util.*;

public class DomParser implements XMLParser {
    private Document document;

    public DomParser(Document document) {
        this.document = document;
    }

    public List<Node> getAllNodes() {
        return getAllNodes(document.getDocumentElement());
    }

    public List<Node> getAllNodes(Node node) {
        List<Node> nodes = new ArrayList<>();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node nextNode = childNodes.item(i);
            if (nextNode instanceof Element) {
                nodes.addAll(getAllNodes(nextNode));
            }
            if (nextNode != null) {
                nodes.add(nextNode);
            }
        }
        return nodes;
    }

    @Override
    public void delAllTags(String tag) {
        delAllNodes(document.getDocumentElement(), tag);
    }

    public void delAllNodes(Node node, String tag) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node nextNode = childNodes.item(i);
//            if (nextNode instanceof Element) {
            if (nextNode.getNodeType() == Node.ELEMENT_NODE) {
                if (nextNode.getNodeName().equals(tag)) {
                    while (nextNode.hasChildNodes()) {
                        nextNode.removeChild(nextNode.getFirstChild());
                    }

                    Node parentNode = nextNode.getParentNode();
                    parentNode.removeChild(nextNode);
                    if (parentNode.hasChildNodes()) {
                        NodeList nodeList = parentNode.getChildNodes();
                        for (int j = 0; j < nodeList.getLength(); j++) {
                            Node item = nodeList.item(j);
                            if (item.getNodeType() == Node.TEXT_NODE) {
                                parentNode.removeChild(item);
                            }
                        }
                    }
                }
                delAllNodes(nextNode, tag);
            }
        }
    }

    public List<Element> getAllElements() {
        return getAllElements(document.getDocumentElement());
    }

    public List<Element> getAllElements(Element element) {
        List<Element> elements = new ArrayList<>();
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element nextElement = (Element) node;
                if (nextElement.hasChildNodes()) {
                    elements.addAll(getAllElements(nextElement));
                }
                elements.add(nextElement);
            }
        }
        return elements;
    }

    public String getElementTextValue(Element element) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                return node.getNodeValue().trim();
            }
        }
        return "";
    }

    //    https://zetcode.com/java/dom/
    @Override
    public int addTag(String parent, String name, String value) {
        int count = 0;
        NodeList childNodes = document.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.hasChildNodes()) {
                NodeList nodeList = node.getChildNodes();
                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node item = nodeList.item(j);
                    if (item.getNodeType() == Node.ELEMENT_NODE && item.getNodeName().equals(parent)) {
                        Element element = document.createElement(name);
                        element.appendChild(document.createTextNode(value));
                        item.appendChild(element);
                        count++;
                    }
                }
            }
        }
        return count;
    }

    @Override
    public Map<String, List<String>> getAllTagValuePairs() {
        Map<String, List<String>> listMap = new TreeMap<>();
        for (Element element : getAllElements()) {
            List<String> list = new ArrayList<>();
            if (listMap.containsKey(element.getNodeName())) {
                list = listMap.get(element.getNodeName());
            }
            list.add(getElementTextValue(element));
            listMap.put(element.getNodeName(), list);
        }
        return listMap;
    }

    @Override
    public Map<String, List<String>> getAllTagValuePairs(String tag) {
        Map<String, List<String>> listMap = new TreeMap<>();
        NodeList nodeList = document.getElementsByTagName(tag);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                for (Element element : getAllElements((Element) node)) {
                    List<String> list = new ArrayList<>();
                    if (listMap.containsKey(element.getNodeName())) {
                        list = listMap.get(element.getNodeName());
                    }
                    list.add(getElementTextValue(element));
                    listMap.put(element.getNodeName(), list);
                }
            }
        }

        return listMap;
    }


    public Map<String, String> getAttributes(NamedNodeMap map) {
        Map<String, String> attrMap = new HashMap<>();
        for (int i = 0; i < map.getLength(); i++) {
            Node node = map.item(i);
            String nodeName = node.getNodeName();
            String nodeValue = node.getNodeValue();
            attrMap.put(nodeName, nodeValue);
        }
        return attrMap;
    }

    @Override
    public Map<String, List<String>> getAllAttrValuePairs(String tag) {
        Map<String, List<String>> attrMap = new HashMap<>();
        NodeList nodeList = document.getElementsByTagName(tag);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                for (Map.Entry<String, String> entry : getAttributes(node.getAttributes()).entrySet()) {
                    List<String> strings = new ArrayList<>();
                    if (attrMap.containsKey(entry.getKey())) {
                        strings = attrMap.get(entry.getKey());
                    }
                    strings.add(entry.getValue());
                    attrMap.put(entry.getKey(), strings);
                }
            }
        }
        return attrMap;
    }

}

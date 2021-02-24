package com.kirilo.parsers;

import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomParser {
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

    public void delAllNodes(String tag) {
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

    public Map<String, String> getAttributes(NamedNodeMap map) {
        Map<String, String> attMap = new HashMap<>();
        for (int i = 0; i < map.getLength(); i++) {
            Node node = map.item(i);
            String nodeName = node.getNodeName();
            String nodeValue = node.getNodeValue();
            attMap.put(nodeName, nodeValue);
        }
        return attMap;
    }

    //    https://zetcode.com/java/dom/
    public int addNode(String parent, String name, String value) {
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

}

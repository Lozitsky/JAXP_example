package com.kirilo.parsers;

import java.util.List;
import java.util.Map;

public interface XMLParser {
    void delAllTags(String tag);

    int addTag(String parent, String name, String value);

    Map<String, List<String>> getAllTagValuePairs();

    Map<String, List<String>> getAllTagValuePairs(String tag);

    Map<String, List<String>> getAllAttrValuePairs(String tag);
}

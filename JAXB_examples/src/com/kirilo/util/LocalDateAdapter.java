package com.kirilo.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

// https://stackoverflow.com/a/36157446/9586230
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    @Override
    public LocalDate unmarshal(String s) throws Exception {
        return LocalDate.parse(s);
    }

    @Override
    public String marshal(LocalDate localDate) throws Exception {
        return localDate.toString();
    }
}

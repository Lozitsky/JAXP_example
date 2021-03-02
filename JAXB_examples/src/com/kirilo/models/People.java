package com.kirilo.models;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "people", namespace = "com.kirilo.models")
@XmlType(propOrder = {"personList"})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class People {
    private List<Person> personList;

    public People() {
    }

    public List<Person> getPersonList() {
        return personList;
    }

    @XmlElement(name = "person")
    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    @Override
    public String toString() {
        return "People{" +
                "\npersonList=" + personList +
                "\n}";
    }
}

package com.kirilo;

import com.kirilo.models.People;
import com.kirilo.models.Person;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// https://www.javacodegeeks.com/2013/02/jaxb-tutorial-getting-started.html
// https://mkyong.com/java/jaxb-hello-world-example/
public class JAXBMarshallerExample {
    public static void main(String[] args) {
        People people = new People();
        Person person = new Person();
        Person person2 = new Person();
        Person person3 = new Person();
        Person person4 = new Person();
        Person person5 = new Person();
        person.setId(1);
        person.setType("2");
        person.setName("Ivan");
        person.setDate(LocalDate.now());
        person2.setId(2);
        person2.setType("2");
        person2.setName("Микола");
        person2.setDate(LocalDate.now());
        person3.setId(3);
        person3.setType("3");
        person3.setName("David");
        person3.setDate(LocalDate.now());
        person4.setId(4);
        person4.setType("1");
        person4.setName("Michele");
        person4.setDate(LocalDate.now());
        person5.setId(5);
        person5.setType("2");
        person5.setName("Kate");
        person5.setDate(LocalDate.now());
        List<Person> list = new ArrayList<>();
        list.add(person);
        list.add(person2);
        list.add(person3);
        list.add(person4);
        list.add(person5);
        people.setPersonList(list);
        System.out.println(people);
        try {
            File file = new File("./xml/people.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(People.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(people, file);
            marshaller.marshal(people, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}

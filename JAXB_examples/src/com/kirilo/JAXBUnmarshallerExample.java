package com.kirilo;

import com.kirilo.models.People;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class JAXBUnmarshallerExample {
    public static void main(String[] args) {
        try {
//            https://stackoverflow.com/a/13011927/9586230
            File file = new File("./xml/people.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(People.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            People people = (People) unmarshaller.unmarshal(file);
            System.out.println(people);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}

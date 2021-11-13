package org.example.kafkastreams.xmltoavro;

import books.BooksForm;
import books.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class Unmarshaller {

    public void unmarshal() throws JAXBException, FileNotFoundException {
        var jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        BooksForm booksForm = jaxbUnmarshaller.unmarshal(
                new StreamSource(new FileInputStream("src/main/resources/books.xml")),
                BooksForm.class
        ).getValue();
        System.out.println(booksForm);
    }
}

package org.example.kafkastreams.xmltoavro;

import books.BooksForm;
import books.ObjectFactory;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

@Service
public class Unmarshaller {

    @SneakyThrows
    public BooksForm unmarshal(byte[] bytes) {
        var jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return jaxbUnmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(bytes)),
                BooksForm.class
        ).getValue();
    }
}

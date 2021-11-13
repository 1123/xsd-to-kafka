package org.example.kafkastreams.xmltoavro;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class UnmarshallerTest {

    @Test
    public void test() throws JAXBException, FileNotFoundException {
        Unmarshaller xmlParser = new Unmarshaller();
        xmlParser.unmarshal();
    }
}

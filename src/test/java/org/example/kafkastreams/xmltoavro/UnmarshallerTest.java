package org.example.kafkastreams.xmltoavro;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UnmarshallerTest {

    @Test
    public void test() throws IOException {
        String xml = Files.readString(Paths.get("src/main/resources/books.xml"), Charset.defaultCharset());
        Unmarshaller xmlParser = new Unmarshaller();
        xmlParser.unmarshal(xml.getBytes(StandardCharsets.UTF_8));
    }
}

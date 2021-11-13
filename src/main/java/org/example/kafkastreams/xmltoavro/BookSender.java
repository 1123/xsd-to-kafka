package org.example.kafkastreams.xmltoavro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
public class BookSender {

    @Autowired
    private KafkaTemplate<String, String> booksTemplate;

    @Scheduled(initialDelay = 1000, fixedDelay = 15000)
    public void sendBooks() throws IOException {
        log.info("Sending book in XML format");
        booksTemplate.send(
                "books-xml",
                Files.readString(Paths.get("src/main/resources/books.xml"), Charset.defaultCharset())
        );
    }

}

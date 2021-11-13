package org.example.kafkastreams.xmltoavro;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class XmlWithXsdToAvroConverterApp {

	private final Unmarshaller unmarshaller;

	public XmlWithXsdToAvroConverterApp(@Autowired Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	public static void main(String[] args) {
		SpringApplication.run(XmlWithXsdToAvroConverterApp.class, args);
	}


	@Bean
	public Properties kafkaStreamsProperties() throws IOException {
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream("kafkastreams.properties");
		prop.load(stream);
		return prop;
	}

	@Bean
	public KafkaStreams stream(Properties kafkaStreamsProperties) {
		StreamsBuilder streamsBuilder = new StreamsBuilder();
		var xmlStream = streamsBuilder.stream("books-xml", Consumed.with(new Serdes.StringSerde(), new Serdes.ByteArraySerde()));
		xmlStream.peek((k,v) -> log.info(new String(v)));
		var bookStream = xmlStream.mapValues(unmarshaller::unmarshal);
		bookStream.peek((k,v) -> log.info(v.toString()));
		var topology = streamsBuilder.build();
		KafkaStreams kafkaStreams = new KafkaStreams(topology, kafkaStreamsProperties);
		kafkaStreams.start();
		return kafkaStreams;
	}

}


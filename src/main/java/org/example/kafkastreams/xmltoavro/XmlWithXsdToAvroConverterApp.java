package org.example.kafkastreams.xmltoavro;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class XmlWithXsdToAvroConverterApp {


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
		streamsBuilder.stream("xml-books");
		var topology = streamsBuilder.build();
		KafkaStreams kafkaStreams = new KafkaStreams(topology, kafkaStreamsProperties);
		kafkaStreams.start();
		return kafkaStreams;
	}

}


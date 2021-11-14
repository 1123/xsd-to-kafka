package org.example.kafkastreams.xmltoavro;

import books.BooksForm;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.ReflectionAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

	@Value("${schema.registry.url}")
	private String schemaRegistryUrl;

	@Value("${schema.registry.user_info}")
	private String schemaRegistryUserInfo;

	@Bean
	public Properties kafkaStreamsProperties() throws IOException {
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		var env = System.getenv("SPRING_PROFILES_ACTIVE");
		var file = (env == null || env.equals("")) ? "kafkastreams.properties" : "kafkastreams-" + env + ".properties";
		InputStream stream = loader.getResourceAsStream(file);
		prop.load(stream);
		return prop;
	}

	private Map<String, String> schemaRegistryConfig() {
		Map<String, String> schemaRegistryconfig = new HashMap<>();
		schemaRegistryconfig.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
		schemaRegistryconfig.put(AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO");
		schemaRegistryconfig.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, schemaRegistryUserInfo);
		return schemaRegistryconfig;
	}

	@Bean
	public KafkaStreams stream(Properties kafkaStreamsProperties) {
		StreamsBuilder streamsBuilder = new StreamsBuilder();
		var xmlStream = streamsBuilder
				.stream("books-xml",
						Consumed.with(Serdes.String(), Serdes.ByteArray())
				);
		var bookStream = xmlStream.mapValues(unmarshaller::unmarshal);
		bookStream.peek((k,v) -> log.info(v.toString()));
		Serde<BooksForm> reflectionAvroSerde = new ReflectionAvroSerde<>();
		reflectionAvroSerde.configure(schemaRegistryConfig(), false);
		bookStream.to("books-avro", Produced.with(Serdes.String(), reflectionAvroSerde));
		var topology = streamsBuilder.build();
		KafkaStreams kafkaStreams = new KafkaStreams(topology, kafkaStreamsProperties);
		kafkaStreams.start();
		return kafkaStreams;
	}

}


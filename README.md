# Kafka Streams App for Conversion of XML with XSD to Kafka formats

This repository shows how to convert XML files that correspond to a 
provided XSD schema to Avro and Json Schema. 

This is a use-case frequently found in large organizations, since
XSD has been and still is a standard for data interchange between
systems in the financial industry, retail, logistics, intellectual property (e.g. patents), travel industry, and many more. 

## Prerequisites

* Linux or MacOS
* JDK 11 or higher
* Confluent Platform 7 or higher
* Maven
* Access to maven central or a local mirror

## Running the demo

* Start Confluent Platform (at least Zookeeper, Kafka and Schema Registry)
* Start the application: `sh run-locally.sh`
* Inspect the Kafka topics books-xml, books-avro and books-json-schema either within Control Center, or via the kafka-console-consumer, kafka-avro-console-consumer and kafka-json-schema-console-consumer tools. 



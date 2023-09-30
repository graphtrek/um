# Getting Started

## Links

These additional references should also help you:
### TMUX
* [tmux Key Combinations](https://keycombiner.com/collections/tmux/)

### Elastic
* [Spring Boot + Elasticsearch + CRUD example](https://www.pixeltrice.com/spring-boot-elasticsearch-crud-example/)
* [GIT Spring Boot + Elasticsearch + CRUD example](https://github.com/sk444/springboot-elasticsearch/tree/main)
* [GIT spring-boot-elasticserach-example](https://github.com/Simplifying-Tech/spring-boot-elasticserach-example/tree/main)
* [Exploring Elasticsearch 8: Utilizing Spring Boot 3 and Spring Data Elasticsearch 5](https://medium.com/@truongbui95/exploring-elasticsearch-8-utilizing-spring-boot-3-and-spring-data-elasticsearch-5-495650115197)
* [GIT Exploring Elasticsearch 8: Utilizing Spring Boot 3 and Spring Data Elasticsearch 5](https://github.com/buingoctruong/springboot3-elasticsearch8/tree/master)
* [Using Elasticsearch with Spring Boot](https://reflectoring.io/spring-boot-elasticsearch/)
* [GIT Using Elasticsearch with Spring Boot](https://github.com/thombergs/code-examples/tree/master/spring-boot/spring-boot-elasticsearch)
* [GIT springboot3-elasticsearch8](https://github.com/buingoctruong/springboot3-elasticsearch8/tree/master)
* [GIT elastic-demo](https://github.com/eric-chao/spring-boot/tree/master)
* [GIT Spring Data Elastic Search Example](https://github.com/TechPrimers/spring-data-elastic-example-3/tree/master)

### KAFKA
``` 
# Start Kafka without ZooKeeper  
kafka-storage random-uuid 
kafka-storage format -t <uuid> -c ../etc/kafka/kraft/server.properties
kafka-server-start ../etc/kafka/kraft/server.properties

# Create Topic
kafka-topics --bootstrap-server localhost:9092 --create --topic ActivityLog --partitions 1 --replication-factor 1

# Delete Topic
kafka-topics --bootstrap-server localhost:9092 --delete --topic ActivityLog

# View topic contents
kcat -b localhost:9092 -t ActivityLog

# Metadata Listing
kcat -b localhost:9092 -L -J
kcat -b localhost:9092 -L

``` 

* [Kafka without zookeeper](https://linuxhint.com/run-apache-kafka-without-zookeeper)
* [KafkaCat](https://docs.confluent.io/platform/current/tools/kafkacat-usage.html)
* [Apache Kafka® Producer Example using SpringBoot 3.x | Java Techie](https://github.com/Java-Techie-jt/kafka-producer-example/tree/main)
* [Apache Kafka® Consumer Example using SpringBoot 3 | Consumer Groups | Java Techie](https://github.com/Java-Techie-jt/kafka-consumer-example)
* [How to Integrate Apache Kafka in Your Spring Boot Application](https://www.pixeltrice.com/how-to-integrate-apache-kafka-in-your-spring-boot-application/)
* [GIT How to Integrate Apache Kafka in Your Spring Boot Application](https://github.com/sk444/spring-boot-Kafka-app)

## Reference Documentation

For further reference, please consider the following sections:
* [Official Gradle documentation](https://docs.gradle.org)
* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.3/gradle-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#using.devtools)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#actuator)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.servlet.spring-mvc.template-engines)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web)
* [Spring Data Elasticsearch (Access+Driver)](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#data.nosql.elasticsearch)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)

## Guides

The following guides illustrate how to use some features concretely:
* [Dan Vega Getting Started with Java 21 - JDK 21 First Look](https://www.youtube.com/watch?v=aqc5YB7TISM)
* [Dan Vega A First Look at the new Rest Client in Spring Boot 3.2](https://www.youtube.com/watch?v=UDNrJAvKc0k)
* [Dan Vega Spring ResponseEntity - How to customize the response in Spring Boot](https://www.youtube.com/watch?v=B5Zrn1Tzyqw)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
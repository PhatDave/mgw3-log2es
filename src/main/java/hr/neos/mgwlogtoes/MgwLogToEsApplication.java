package hr.neos.mgwlogtoes;

import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class MgwLogToEsApplication {
	private static final String TOPIC_PREFIX = "mgw-log-reader-";
	public static Properties baseProducerProperties = new Properties();
	public static Properties baseConsumerProperties = new Properties();
	public static Properties fileDataProducerProperties = new Properties();
	public static Properties fileDataConsumerProperties = new Properties();
	public static AdminClient adminClient;

	@SneakyThrows
	public static void main(String[] args) {
		baseProducerProperties.put("bootstrap.servers", "localhost:9092");
		baseProducerProperties.put("acks", "all");
		baseProducerProperties.put("retries", 0);
		baseProducerProperties.put("batch.size", 16384);
		baseProducerProperties.put("linger.ms", 1);
		baseProducerProperties.put("buffer.memory", 33554432);
		baseProducerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		baseProducerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		baseProducerProperties.put("auto.create.topics.enable", "true");

		baseConsumerProperties.put("bootstrap.servers", "localhost:9092");
		baseConsumerProperties.put("group.id", TOPIC_PREFIX + "group");
		baseConsumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		baseConsumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		baseConsumerProperties.put("auto.offset.reset", "earliest");
		baseProducerProperties.put("auto.create.topics.enable", "true");

		fileDataProducerProperties = (Properties) baseProducerProperties.clone();
		fileDataProducerProperties.put("topic", TOPIC_PREFIX + "file-data");

		fileDataConsumerProperties = (Properties) baseConsumerProperties.clone();
		fileDataConsumerProperties.put("topic", TOPIC_PREFIX + "file-data");
		fileDataConsumerProperties.put("max.poll.records", "2000");


		Properties adminClientProps = new Properties();
		adminClientProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		adminClient = AdminClient.create(adminClientProps);

		deleteTopic(TOPIC_PREFIX + "file-data");
		createTopic(TOPIC_PREFIX + "file-data");
		adminClient.close();

		SpringApplication.run(MgwLogToEsApplication.class, args);
	}

	public static void deleteTopic(String topic) {
		DeleteTopicsResult result = adminClient.deleteTopics(Collections.singletonList(topic));

		try {
			result.all().get();
			System.out.println("Topic deleted successfully");
		} catch (ExecutionException e) {
			System.out.println("Failed to delete topic: " + e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("Failed to delete topic: " + e.getMessage());
		}
	}

	public static void createTopic(String topic) {
		try {
			adminClient.createTopics(Collections.singletonList(new NewTopic(topic, 32, (short) 1))).all().get();
		} catch (InterruptedException | ExecutionException e) {
			if (e.getCause() instanceof TopicExistsException) {
				System.out.println("Topic already exists");
				deleteTopic(topic);
				createTopic(topic);
			} else {
				e.printStackTrace();
			}
		}
	}
}

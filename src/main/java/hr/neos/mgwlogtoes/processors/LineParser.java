package hr.neos.mgwlogtoes.processors;

import hr.neos.mgwlogtoes.MgwLogToEsApplication;
import hr.neos.mgwlogtoes.entity.LogLineData;
import hr.neos.mgwlogtoes.repository.ESRepository;
import lombok.Setter;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class LineParser implements Processor {
	private final ESRepository esRepository;
	private final List<Thread> threads = new ArrayList<>();
	@Setter
	private Integer threadCount;
	private boolean run = true;
	private boolean stopDemanded = false;
	private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public LineParser(ESRepository esRepository) {
		this.esRepository = esRepository;
	}

	@Override
	public void start() {
		for (int i = 0; i < threadCount; i++) {
			Thread thread = new Thread(() -> {
				KafkaConsumer<String, String> consumer = new KafkaConsumer<>(MgwLogToEsApplication.fileDataConsumerProperties);
				consumer.subscribe(Collections.singleton(MgwLogToEsApplication.fileDataConsumerProperties.getProperty("topic")));

				while (run) {
					ConsumerRecords<String, String> records = consumer.poll(100);

					StreamSupport.stream(records.spliterator(), true).forEach(record -> {
								LogLineData.LogLineDataBuilder builder = LogLineData.builder();
								builder.line(record.value());

								String key = record.key();
								String lineNumber = key.split("-")[1];
								builder.lineNumber(Long.valueOf(lineNumber));
								builder.filePath(key.split("-")[0]);

								try {
									builder.timestamp(dateTimeFormat.parse(record.value().substring(0, 23).trim()).getTime());
								} catch (ParseException e) {
								}
								builder.level(record.value().substring(24, 30).trim());
								builder.threadName(record.value().substring(31, 47).trim());
								builder.className(record.value().substring(48, 60).trim());

								LogLineData logLineData = builder.build();

								esRepository.save(logLineData);
							}
					);

					if (stopDemanded) {
						run = false;
					}
				}
			});
			threads.add(thread);
			thread.start();
		}
	}

	@Override
	public void stop() {
		run = false;
	}

	@Override
	public void stopWhenDone() {
		stopDemanded = true;
	}

	@Override
	public List<Thread> getThreads() {
		return threads;
	}
}

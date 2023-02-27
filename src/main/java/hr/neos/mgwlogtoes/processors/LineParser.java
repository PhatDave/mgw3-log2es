package hr.neos.mgwlogtoes.processors;

import hr.neos.mgwlogtoes.MgwLogToEsApplication;
import hr.neos.mgwlogtoes.entity.LogLineData;
import hr.neos.mgwlogtoes.repository.ESRepository;
import lombok.Setter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class LineParser implements Processor {
	private final ESRepository esRepository;
	private final List<Thread> threads = new ArrayList<>();
	private final Pattern datePattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+) (\\d+):(\\d+):(\\d+)\\.(\\d+)");
	@Setter
	private Integer threadCount;
	private boolean run = true;
	private boolean stopDemanded = false;

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
					ConsumerRecords<String, String> records = consumer.poll(1000);
					System.out.println("read records " + records.count());

					Stream<ConsumerRecord<String, String>> stream = StreamSupport.stream(records.spliterator(), true);
					List<LogLineData> logLineDatas = stream.map(record -> {
						LogLineData.LogLineDataBuilder builder = LogLineData.builder();
						builder.line(record.value());

						String key = record.key();
						String lineNumber = key.split("-")[1];
						builder.lineNumber(Long.valueOf(lineNumber));
						builder.filePath(key.split("-")[0]);

						parseInfo(builder, record.value());
						return builder.build();
					}).toList();
					System.out.println(logLineDatas.size());
					esRepository.saveAll(logLineDatas);

					if (stopDemanded) {
						run = false;
					}
				}
			});
			threads.add(thread);
			thread.start();
		}
	}

	public void parseInfo(LogLineData.LogLineDataBuilder builder, String line) {
		String[] fields = line.split("\\s+");
		try {
			String date = fields[0] + " " + fields[1];
			String logLevel = fields[2];
			String thread = fields[5];
			String className = fields[6];

			builder.timestamp(toLocalDateTime(date));
			builder.level(logLevel);
			builder.threadName(thread);
			builder.className(className);
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private LocalDateTime toLocalDateTime(String date) {
		Matcher matcher = datePattern.matcher(date);
		if (matcher.find()) {
			try {
				int year = Integer.parseInt(matcher.group(1));
				int month = Integer.parseInt(matcher.group(2));
				int day = Integer.parseInt(matcher.group(3));
				int hour = Integer.parseInt(matcher.group(4));
				int minute = Integer.parseInt(matcher.group(5));
				int second = Integer.parseInt(matcher.group(6));
				int millis = Integer.parseInt(matcher.group(7));

				LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, minute, second, millis * 1000000);
				return ldt;
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
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

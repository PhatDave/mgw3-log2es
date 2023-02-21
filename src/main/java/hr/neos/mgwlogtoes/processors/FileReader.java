package hr.neos.mgwlogtoes.processors;

import hr.neos.mgwlogtoes.MgwLogToEsApplication;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class FileReader implements Processor {
	private final List<Thread> threads = new ArrayList<>();
	private List<Path> paths;

	private boolean run = true;
	private boolean stopDemanded = false;

	public FileReader(Path path) {
		paths = new ArrayList<>();
		paths.add(path);
	}

	public FileReader(List<Path> paths) {
		this.paths = paths;
	}

	@Override
	public void start() {
		paths.stream().map(path -> {
			Thread thread = new Thread(() -> {
				Producer<String, String> producer = new KafkaProducer<>(MgwLogToEsApplication.fileDataProducerProperties);
				try {
					BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());

					String line;
					Long lineNumber = 0L;
					while ((line = reader.readLine()) != null) {
						if (!run) {
							return;
						}

						lineNumber++;
						String trimmedLine = line.trim();
						trimmedLine.replace('"', '\'');
						producer.send(new ProducerRecord<>(MgwLogToEsApplication.fileDataProducerProperties.getProperty("topic"), path + "-" + lineNumber, trimmedLine));

						if (stopDemanded) {
							run = false;
						}
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
			threads.add(thread);
			return thread;
		}).forEach(Thread::start);
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

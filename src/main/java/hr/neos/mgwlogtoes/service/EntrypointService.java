package hr.neos.mgwlogtoes.service;

import hr.neos.mgwlogtoes.processors.FileReaderProcessor.FileReader;
import hr.neos.mgwlogtoes.processors.LogLineParser.LogLineParser;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Component
@RequiredArgsConstructor
public class EntrypointService {
	private final static String ROOT_PATH = "c:\\tmp\\mgwlogs\\";

	@EventListener(ApplicationReadyEvent.class)
	public void startup() {
		Map<Path, Stack<String>> fileData = new HashMap<>();
		File root = new File(ROOT_PATH);
		File[] files = root.listFiles();
		for (File file : files) {
			fileData.put(file.toPath(), new Stack<>());
		}

		FileReader fileReader = new FileReader(fileData);
		LogLineParser logLineParser = new LogLineParser(fileData, 4);

		fileReader.start();
		logLineParser.start();
	}
}

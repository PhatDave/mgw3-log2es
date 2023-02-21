package hr.neos.mgwlogtoes.service;

import hr.neos.mgwlogtoes.processors.FileReader;
import hr.neos.mgwlogtoes.processors.LineParser;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Component
@RequiredArgsConstructor
public class EntrypointService {
	private final static String ROOT_PATH = "c:\\tmp\\";
	private final LineParser lineParser;

	@EventListener(ApplicationReadyEvent.class)
	public void startup() {
		Map<Path, Stack<String>> fileData = new HashMap<>();
		File root = new File(ROOT_PATH);
		File[] files = root.listFiles();
		if (files == null) {
			throw new RuntimeException("No files found in " + ROOT_PATH);
		}
		files = Arrays.stream(files).filter(File::isFile).toArray(File[]::new);

		FileReader fileReader = new FileReader(Arrays.stream(files).map(File::toPath).toList());
		lineParser.setThreadCount(8);

		fileReader.start();
		lineParser.start();
	}
}

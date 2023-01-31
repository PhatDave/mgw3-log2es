package hr.neos.mgwlogtoes.processors.FileReaderProcessor;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Stack;

@Service
public class FileReaderService {
	private final FileReaderProcessor fileReaderProcessor;

	public FileReaderService() {
		this.fileReaderProcessor = new FileReaderProcessor();
	}

	public Stack<String> getLinesSync(Path path) {
		Stack<String> lines = getLinesAsync(path);
		fileReaderProcessor.waitUntilDone();
		return lines;
	}

	public Stack<String> getLinesAsync(Path path) {
		return fileReaderProcessor.start(path);
	}
}

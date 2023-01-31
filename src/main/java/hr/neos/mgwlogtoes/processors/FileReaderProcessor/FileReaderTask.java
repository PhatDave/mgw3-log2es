package hr.neos.mgwlogtoes.processors.FileReaderProcessor;

import hr.neos.mgwlogtoes.processors.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;

public class FileReaderTask implements Task {
	private final Stack<String> output;
	private final Path path;

	public FileReaderTask(Path path, Stack<String> output) {
		this.path = path;
		this.output = output;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());

			String line;
			while ((line = reader.readLine()) != null) {
				output.add(line.trim());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setRun(boolean run) {
	}

	@Override
	public void setStopDemanded(boolean stopDemanded) {
	}
}

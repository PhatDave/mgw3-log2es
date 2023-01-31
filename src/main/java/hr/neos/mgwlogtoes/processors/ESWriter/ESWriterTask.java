package hr.neos.mgwlogtoes.processors.ESWriter;

import hr.neos.mgwlogtoes.processors.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;

public class ESWriterTask implements Task {
	private final Stack<String> lines;
	private final Path path;

	public ESWriterTask(Path path, Stack<String> lines) {
		this.path = path;
		this.lines = lines;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());

			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line.trim());
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

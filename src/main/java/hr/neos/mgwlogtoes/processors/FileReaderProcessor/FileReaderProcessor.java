package hr.neos.mgwlogtoes.processors.FileReaderProcessor;

import hr.neos.mgwlogtoes.processors.Processor;
import hr.neos.mgwlogtoes.processors.Task;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class FileReaderProcessor implements Processor {
	private final Map<Path, Stack<String>> lines = new HashMap<>();
	private final ThreadPoolExecutor executor;
	private final List<Task> pendingTasks = new ArrayList<>();
	private final List<Future> runningTasks = new ArrayList<>();
	private Path path;

	public FileReaderProcessor() {
		this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	}

	public List<Stack<String>> start(List<Path> paths) {
		List<Stack<String>> lines = new ArrayList<>();
		for (Path path : paths) {
			lines.add(start(path));
		}
		return lines;
	}

	public Stack<String> start(Path path) {
		Stack<String> lines = new Stack<>();
		this.lines.put(path, lines);
		pendingTasks.add(new FileReaderTask(path, lines));
		start();
		return lines;
	}

	@Override
	public void start() {
		if (pendingTasks.isEmpty()) {
			return;
		}
		while (!pendingTasks.isEmpty()) {
			Task task = pendingTasks.remove(0);
			runningTasks.add(executor.submit(task));
		}
	}

	@Override
	public void stop() {
		runningTasks.forEach(task -> task.cancel(true));
	}

	@Override
	public void stopWhenDone() {
		runningTasks.forEach(task -> task.cancel(false));
	}

	@Override
	public List<Future> getPendingTasks() {
		return runningTasks;
	}
}

package hr.neos.mgwlogtoes.processors.FileReaderProcessor;

import hr.neos.mgwlogtoes.processors.Processor;
import hr.neos.mgwlogtoes.processors.Task;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class FileReader implements Processor {
	private final Map<Path, Stack<String>> output;
	private final ThreadPoolExecutor executor;
	private final List<Task> pendingTasks = new ArrayList<>();
	private final List<Future> runningTasks = new ArrayList<>();
	private Path path;

	public FileReader(Map<Path, Stack<String>> output) {
		this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		this.output = output;
	}

	@Override
	public void start() {
		output.forEach((path, lines) -> {
			pendingTasks.add(new FileReaderTask(path, lines));
		});

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

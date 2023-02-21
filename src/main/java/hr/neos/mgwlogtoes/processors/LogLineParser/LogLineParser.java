//package hr.neos.mgwlogtoes.processors.LogLineParser;
//
//import hr.neos.mgwlogtoes.entity.LogLine;
//import hr.neos.mgwlogtoes.processors.Processor;
//import hr.neos.mgwlogtoes.processors.Task;
//
//import java.nio.file.Path;
//import java.util.*;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import java.util.concurrent.ThreadPoolExecutor;
//
//public class LogLineParser implements Processor {
//	private Map<Path, Stack<String>> input;
//	private Map<Path, Stack<LogLine>> output;
//	private final ThreadPoolExecutor executor;
//	private final List<Task> pendingTasks = new ArrayList<>();
//	private final List<Future> runningTasks = new ArrayList<>();
//	private Integer threadsPerPath;
//
//	public LogLineParser(Map<Path, Stack<String>> input, Integer threadsPerPath) {
//		this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
//		this.input = input;
//		this.threadsPerPath = threadsPerPath;
//		this.output = new HashMap<>();
//		this.input.forEach((path, lines) -> this.output.put(path, new Stack<>()));
//	}
//
//	public Map<Path, Stack<LogLine>> getOutput() {
//		return output;
//	}
//
//	@Override
//	public void start() {
//		for (int i = 0; i < threadsPerPath; i++) {
//			input.forEach((path, lines) -> {
//				pendingTasks.add(new LogLineParserTask(lines, output.get(path), path));
//			});
//		}
//
//		while (!pendingTasks.isEmpty()) {
//			Task task = pendingTasks.remove(0);
//			runningTasks.add(executor.submit(task));
//		}
//	}
//
//	@Override
//	public void stop() {
//		runningTasks.forEach(task -> task.cancel(true));
//	}
//
//	@Override
//	public void stopWhenDone() {
//		runningTasks.forEach(task -> task.cancel(false));
//	}
//
//	@Override
//	public List<Thread> getThreads() {
//		return null;
//	}
//}

//package hr.neos.mgwlogtoes.processors.ESWriter;
//
//import hr.neos.mgwlogtoes.processors.Processor;
//import hr.neos.mgwlogtoes.processors.Task;
//
//import java.nio.file.Path;
//import java.util.*;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import java.util.concurrent.ThreadPoolExecutor;
//
//public class ESWriter implements Processor {
//	private Map<Path, Stack<String>> inputLines;
//	private final ThreadPoolExecutor executor;
//	private final List<Task> pendingTasks = new ArrayList<>();
//	private final List<Future> runningTasks = new ArrayList<>();
//	private Integer threadsPerPath;
//
//	public ESWriter(Map<Path, Stack<String>> inputLines, Integer threadsPerPath) {
//		this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
//		this.inputLines = inputLines;
//		this.threadsPerPath = threadsPerPath;
//	}
//
//	@Override
//	public void start() {
//		for (int i = 0; i < threadsPerPath; i++) {
//			inputLines.forEach((path, lines) -> {
//				if (lines.isEmpty()) {
//					return;
//				}
////				pendingTasks.add(new ESWriterTask(lines));
//			});
//		}
//
//		if (pendingTasks.isEmpty()) {
//			return;
//		}
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

package hr.neos.mgwlogtoes.processors;

import java.nio.file.Path;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Future;

public interface Processor {
	void start();

	default void waitUntilDone() {
		getPendingTasks().forEach(task -> {
			try {
				task.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	void stop();

	void stopWhenDone();

	List<Future> getPendingTasks();
}

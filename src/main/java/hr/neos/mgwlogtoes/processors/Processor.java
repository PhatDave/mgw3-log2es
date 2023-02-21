package hr.neos.mgwlogtoes.processors;

import java.util.List;

public interface Processor {
	void start();

	default void waitUntilDone() {
		getThreads().forEach(thread -> {
			try {
				thread.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	void stop();

	void stopWhenDone();

	List<Thread> getThreads();
}

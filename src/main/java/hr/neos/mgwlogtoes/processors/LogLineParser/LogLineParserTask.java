package hr.neos.mgwlogtoes.processors.LogLineParser;

import hr.neos.mgwlogtoes.entity.LogLine;
import hr.neos.mgwlogtoes.processors.Task;

import java.util.Stack;

public class LogLineParserTask implements Task {
	private final Stack<String> input;
	private final Stack<LogLine> output;

	private boolean run = true;
	private boolean stopDemanded = false;

	public LogLineParserTask(Stack<String> input, Stack<LogLine> output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public void run() {
		while (run) {
			if (!input.isEmpty()) {
				String line = input.pop();
				System.out.println(line);
//				output.add(new LogLine(line));
			}

			if (input.isEmpty() && stopDemanded) {
				run = false;
			}
		}
	}

	@Override
	public void setRun(boolean run) {
		this.run = run;
	}

	@Override
	public void setStopDemanded(boolean stopDemanded) {
		this.stopDemanded = stopDemanded;
	}
}

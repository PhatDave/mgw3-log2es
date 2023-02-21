//package hr.neos.mgwlogtoes.processors.LogLineParser;
//
//import hr.neos.mgwlogtoes.entity.LogLine;
//import hr.neos.mgwlogtoes.processors.Task;
//import lombok.SneakyThrows;
//
//import java.nio.file.Path;
//import java.text.SimpleDateFormat;
//import java.util.Stack;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class LogLineParserTask implements Task {
//	private final Stack<String> input;
//	private final Stack<LogLine> output;
//	private final Pattern pattern = Pattern.compile("(\\d+-\\d+-\\d+ \\d+:\\d+:\\d+.\\d+)\\s+(\\w+)\\s*\\[([a-zA-Z0-9 /.:_\\s-]+)]\\s*(\\w+)");
//	private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//
//	private boolean run = true;
//	private boolean stopDemanded = false;
//	private Path filePath;
//
//	public LogLineParserTask(Stack<String> input, Stack<LogLine> output, Path filePath) {
//		this.input = input;
//		this.output = output;
//		this.filePath = filePath;
//	}
//
//	@SneakyThrows
//	@Override
//	public void run() {
//		while (run) {
//			if (!input.isEmpty()) {
//				String line = input.pop();
//				LogLine.LogLineBuilder builder = LogLine.builder();
//				builder.line(line);
//				builder.filePath(filePath.toString());
//
//				Matcher matcher = pattern.matcher(line);
//				boolean patternFound = matcher.find();
//
//				if (patternFound) {
//					builder.timestamp(dateTimeFormat.parse(matcher.group(1).trim()).getTime());
//					builder.level(matcher.group(2).trim());
//					builder.threadName(matcher.group(3).trim());
//					builder.className(matcher.group(4).trim());
//				}
//
//				LogLine logLine = builder.build();
////				System.out.println(logLine);
//				output.add(logLine);
//			}
//
//			if (input.isEmpty() && stopDemanded) {
//				run = false;
//			}
//		}
//	}
//
//	@Override
//	public void setRun(boolean run) {
//		this.run = run;
//	}
//
//	@Override
//	public void setStopDemanded(boolean stopDemanded) {
//		this.stopDemanded = stopDemanded;
//	}
//}

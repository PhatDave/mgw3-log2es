package hr.neos.mgwlogtoes.entity;

import org.springframework.data.annotation.Id;

public class LogLine {
	@Id
	private Long id;

	private String line;
}

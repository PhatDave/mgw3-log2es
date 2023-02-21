package hr.neos.mgwlogtoes.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogLine {
	@Id
	private Long id;
	private String line;
	private Long lineNumber;
}

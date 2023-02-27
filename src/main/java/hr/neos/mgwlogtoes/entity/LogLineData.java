package hr.neos.mgwlogtoes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.sql.Timestamp;
import java.util.Date;
import java.util.StringJoiner;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "loglinedata")
public class LogLineData {
	@Id
	@Field(type = FieldType.Keyword)
	private Long id;
	private String line;
	@Field(type = FieldType.Date, format = DateFormat.date_time)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date timestamp;
	private String level;
	private String threadName;
	private String className;
	private String filePath;
	private Long lineNumber;

	@Override
	public String toString() {
		return new StringJoiner(", ", LogLineData.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("line='" + line + "'")
				.add("timestamp=" + timestamp)
				.add("level='" + level + "'")
				.add("threadName='" + threadName + "'")
				.add("className='" + className + "'")
				.add("filePath='" + filePath + "'")
				.toString();
	}
}

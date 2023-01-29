package hr.neos.mgwlogtoes.repository;

import hr.neos.mgwlogtoes.entity.LogLine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ESRepository extends ElasticsearchRepository<LogLine, Long> {
}

package hr.neos.mgwlogtoes.repository;

import hr.neos.mgwlogtoes.entity.LogLineData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ESRepository extends ElasticsearchRepository<LogLineData, Long> {
}

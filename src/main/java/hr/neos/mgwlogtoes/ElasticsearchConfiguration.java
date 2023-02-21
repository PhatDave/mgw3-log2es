package hr.neos.mgwlogtoes;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.RestClients;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

	private final ElasticsearchProperties elasticsearchProperties;

	@Override
	@Bean
	public RestHighLevelClient elasticsearchClient() {
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9205, "http")));
		return client;
	}

	@Bean
	public RestHighLevelClient client() {
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9205, "http")));
		return client;
	}
}

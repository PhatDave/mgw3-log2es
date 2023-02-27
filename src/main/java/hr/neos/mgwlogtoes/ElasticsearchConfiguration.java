package hr.neos.mgwlogtoes;

import lombok.RequiredArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@RequiredArgsConstructor
@EnableElasticsearchRepositories(basePackages = "*")
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
		RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9205, "http"))
		                                      .setDefaultHeaders(compatibilityHeaders());
		RestHighLevelClient client = new RestHighLevelClient(builder);
		return client;
	}

	private Header[] compatibilityHeaders() {
		return new Header[]{new BasicHeader(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7"), new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7")};
	}
}

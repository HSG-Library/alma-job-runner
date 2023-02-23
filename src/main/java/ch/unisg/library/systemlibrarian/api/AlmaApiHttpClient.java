package ch.unisg.library.systemlibrarian.api;

import java.lang.invoke.MethodHandles;
import java.net.URI;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unisg.library.systemlibrarian.api.models.AlmaApiJobResponse;
import ch.unisg.library.systemlibrarian.jobs.JobConfig;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Singleton;

@Singleton
public class AlmaApiHttpClient {

	private static final String API_KEY_PARAMETER = "apiKey";
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final AlmaApiConfig almaApiConfig;
	private final HttpClient httpClient;

	public AlmaApiHttpClient(AlmaApiConfig almaApiConfig, HttpClient httpClient) {
		this.almaApiConfig = almaApiConfig;
		this.httpClient = httpClient;
	}

	public AlmaApiJobResponse sendJobRequest(JobConfig jobConfig) {
		URI uri = createRequestUri(jobConfig);
		MutableHttpRequest<String> request = HttpRequest.create(HttpMethod.parse(jobConfig.httpMethod()), uri.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_XML)
				.body(jobConfig.xmlPayload());
		LOG.info("Request for job '{}': '{}'", jobConfig.name(), request.toString());
		try {
			HttpResponse<AlmaApiJobResponse> response = httpClient.toBlocking().exchange(request, AlmaApiJobResponse.class);
			return response.getBody()
					.orElseThrow(() -> new HttpClientResponseException("Response not serializable or Empty", response));
		} catch (HttpClientResponseException e) {
			LOG.error("Request not successful. HTTP Status Code: '{} {}', URI: '{}'", e.getStatus().getCode(), e.getResponse().status(), uri);
			LOG.info("Full Stacktrace: ", e);
			return null;
		}
	}

	private URI createRequestUri(JobConfig jobConfig) {
		LOG.info("Creating request URI from job config '{}'", jobConfig.name());
		URI uri = UriBuilder.of(almaApiConfig.getUrl())
				.path(jobConfig.apiPath())
				.queryParam(jobConfig.apiQuery().getKey(), jobConfig.apiQuery().getValue())
				.queryParam(API_KEY_PARAMETER, almaApiConfig.getKey())
				.build();
		LOG.info("URI for job '{}': '{}'", jobConfig.name(), uri);
		return uri;
	}
}

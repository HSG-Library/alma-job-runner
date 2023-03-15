package ch.unisg.library.systemlibrarian.api;

import ch.unisg.library.systemlibrarian.api.models.AlmaApiJobResponse;
import ch.unisg.library.systemlibrarian.api.models.CommonResponse;
import ch.unisg.library.systemlibrarian.api.models.ResponseStatus;
import ch.unisg.library.systemlibrarian.jobs.JobConfig;
import ch.unisg.library.systemlibrarian.notification.NotificationMessageProvider;
import ch.unisg.library.systemlibrarian.notification.NotificationSenderService;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.http.*;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@Prototype
public class AlmaApiHttpClient {

	private static final String API_KEY_PARAMETER = "apiKey";
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final AlmaApiConfig almaApiConfig;
	private final HttpClient httpClient;
	private final NotificationMessageProvider messageProvider;
	private final NotificationSenderService notificationSender;

	public AlmaApiHttpClient(
			final AlmaApiConfig almaApiConfig,
			final HttpClient httpClient,
			final NotificationMessageProvider messageProvider,
			final NotificationSenderService notificationSender) {
		this.almaApiConfig = almaApiConfig;
		this.httpClient = httpClient;
		this.messageProvider = messageProvider;
		this.notificationSender = notificationSender;
	}

	public CommonResponse<String> apiHealthCheck() {
		final URI uri = UriBuilder.of(almaApiConfig.getUrl())
				.path("/almaws/v1/conf/test")
				.queryParam(API_KEY_PARAMETER, almaApiConfig.getKey())
				.build();
		final MutableHttpRequest<?> request = HttpRequest.GET(uri)
				.accept(MediaType.APPLICATION_XML);
		return send(request, String.class);
	}

	public CommonResponse<AlmaApiJobResponse> sendJobRequest(JobConfig jobConfig) {
		final URI uri = createRequestUri(jobConfig);
		final MutableHttpRequest<String> request = HttpRequest.create(HttpMethod.parse(jobConfig.httpMethod()), uri.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_XML)
				.body(jobConfig.xmlPayload());
		LOG.info("Request for job '{}': '{}'", jobConfig.name(), request.toString());
		final CommonResponse<AlmaApiJobResponse> response = send(request, AlmaApiJobResponse.class);
		if (ResponseStatus.ERROR.equals(response.responseStatus())) {
			notificationSender.send(messageProvider.requestFailed(response.httpStatus(), jobConfig));
		}
		return response;
	}

	private <T> CommonResponse<T> send(HttpRequest<?> request, Class<T> responseType) {
		try {
			final HttpResponse<T> httpResponse = httpClient.toBlocking().exchange(request, responseType);
			final T apiResponse = httpResponse.getBody()
					.orElseThrow(() -> new HttpClientResponseException("Response not serializable or Empty", httpResponse));
			return new CommonResponse<>(httpResponse.getStatus(), ResponseStatus.SUCCESS, httpResponse.status().getReason(), LocalDateTime.now(), Optional.of(apiResponse));
		} catch (HttpClientResponseException e) {
			final HttpResponse<?> httpResponse = e.getResponse();
			LOG.error("Request not successful. HTTP Status Code: '{} {}', URI: '{}'", httpResponse.getStatus().getCode(), httpResponse.status().getReason(), request.getUri());
			LOG.info("Full Stacktrace: ", e);
			return new CommonResponse<>(httpResponse.getStatus(), ResponseStatus.ERROR, httpResponse.getStatus().getReason(), LocalDateTime.now(), Optional.empty());
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

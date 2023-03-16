package ch.unisg.library.systemlibrarian.health;

import ch.unisg.library.systemlibrarian.jobs.JobConfig;
import ch.unisg.library.systemlibrarian.jobs.JobSchedulerService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Set;

@MicronautTest
@Property(name = "alma-api.key", value = "test-key")
@Property(name = "alma-api.url", value = "http://localhost:8888")
class HealthControllerTest {

	@Inject
	@Client("/")
	HttpClient client;

	@Inject
	private JobSchedulerService jobSchedulerService;

	@MockBean(JobSchedulerService.class)
	JobSchedulerService jobSchedulerService() {
		return Mockito.mock(JobSchedulerService.class);
	}

	@Test
	void testHealth(){
		HttpResponse<String> exchange = client.toBlocking().exchange(HttpRequest.GET("/health"), String.class);
		Assertions.assertEquals(200, exchange.code());
		Assertions.assertEquals("OK", exchange.body());
	}

	@Test
	void testStatusOk() {
		// mock Alma API, with response healthy
		EmbeddedServer almaApiMockServer = ApplicationContext.run(
				EmbeddedServer.class,
				Map.of(
						"guard", "AlmaApiTest",
						"micronaut.server.port", 8888,
						"response", "any-string.txt",
						"status", HttpStatus.OK
				)
		);
		// mock non empty joblist
		Mockito.when(jobSchedulerService.getScheduledJobs()).thenReturn(Set.of(Mockito.mock(JobConfig.class)));
		HttpResponse<String> exchange = client.toBlocking().exchange(HttpRequest.GET("/status"), String.class);
		Assertions.assertEquals(200, exchange.code());
		Assertions.assertEquals("OK", exchange.body());
		almaApiMockServer.close();
	}

	@Test
	void testStatusAlmaApiNotAvailable() {
		// mock Alma API, with unsuccessful response
		EmbeddedServer almaApiMockServer = ApplicationContext.run(
				EmbeddedServer.class,
				Map.of(
						"guard", "AlmaApiTest",
						"micronaut.server.port", 8888,
						"response", "any-string.txt",
						"status", HttpStatus.INTERNAL_SERVER_ERROR
				)
		);
		// mock non empty joblist
		Mockito.when(jobSchedulerService.getScheduledJobs()).thenReturn(Set.of(Mockito.mock(JobConfig.class)));
		try {
			client.toBlocking().exchange(HttpRequest.GET("/status"), String.class);
		} catch (HttpClientResponseException e) {
			Assertions.assertEquals(500, e.getResponse().code());
			Assertions.assertEquals("Alma API not available", e.getResponse().body());
		}
		almaApiMockServer.close();
	}

	@Test
	void testStatusNoJobs() {
		// mock Alma API, with response healthy
		EmbeddedServer almaApiMockServer = ApplicationContext.run(
				EmbeddedServer.class,
				Map.of(
						"guard", "AlmaApiTest",
						"micronaut.server.port", 8888,
						"response", "any-string.txt",
						"status", HttpStatus.OK
				)
		);
		// mock empty joblist
		Mockito.when(jobSchedulerService.getScheduledJobs()).thenReturn(Set.of());
		try {
			client.toBlocking().exchange(HttpRequest.GET("/status"), String.class);
		} catch (HttpClientResponseException e) {
			Assertions.assertEquals(500, e.getResponse().code());
			Assertions.assertEquals("No jobs registered", e.getResponse().body());
		}
		almaApiMockServer.close();
	}
}

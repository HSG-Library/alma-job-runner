package ch.unisg.library.systemlibrarian.api;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.unisg.library.systemlibrarian.api.models.AlmaApiJobResponse;
import ch.unisg.library.systemlibrarian.jobs.JobConfig;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
@Property(name = "alma-api.key", value = "test-key")
@Property(name = "alma-api.url", value = "http://localhost:8888")
public class AlmaApiHttpClientTest {

	@Inject
	private AlmaApiHttpClient almaApiHttpClient;

	@Test
	void testCall() {
		EmbeddedServer mock = ApplicationContext.run(
				EmbeddedServer.class,
				Map.of(
						"guard", "AlmaApiTest",
						"micronaut.server.port", 8888,
						"response", "start-job-response.json"));

		final JobConfig jobConfig = new JobConfig("test", "*****", "POST", "/hello/velo", Pair.of("op", "run"), "myXML");
		AlmaApiJobResponse  sendJobRequest = almaApiHttpClient.sendJobRequest(jobConfig);

		System.out.println(sendJobRequest);
		Assertions.assertEquals("https://api-eu.hosted.exlibrisgroup.com/almaws/v1/conf/jobs/M50216/instances/3896509020005506", sendJobRequest.getAdditionalInfo().getLink());
		mock.close();
	}
}

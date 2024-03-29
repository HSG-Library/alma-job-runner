package ch.unisg.library.systemlibrarian.api;

import ch.unisg.library.systemlibrarian.api.models.AlmaApiJobResponse;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@MicronautTest
public class AlmaResponseSerializeTest {

	@Inject
	private ObjectMapper objectMapper;

	@Inject
	private ClassPathResourceLoader resourceLoader;

	@Test
	void serializeTest() throws IOException {
		Optional<InputStream> resourceAsStream = resourceLoader.getResourceAsStream("mock/start-job-response.json");
		Assertions.assertTrue(resourceAsStream.isPresent());
		AlmaApiJobResponse jobResponse = objectMapper.readValue(resourceAsStream.get(), AlmaApiJobResponse.class);
		Assertions.assertNotNull(jobResponse);
		Assertions.assertEquals("M50216", jobResponse.getId());
		Assertions.assertEquals("Remove Titles from Collection", jobResponse.getName());
		Assertions.assertEquals("Job no. 3896509020005506 triggered on Wed, 15 Feb 2023 12:46:18 GMT", jobResponse.getAdditionalInfo().getValue());
		Assertions.assertEquals("https://api-eu.hosted.exlibrisgroup.com/almaws/v1/conf/jobs/M50216/instances/3896509020005506", jobResponse.getAdditionalInfo().getLink());
	}
}

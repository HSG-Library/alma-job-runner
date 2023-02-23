package ch.unisg.library.systemlibrarian.api;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

@Requires(property = "guard", value = "AlmaApiTest")
@Controller
public class AlmaApiMockController {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final Environment env;
	private final ResourceLoader resourceLoader;

	public AlmaApiMockController(
			final Environment env,
			final ResourceLoader resourceLoader) {
		this.env = env;
		this.resourceLoader = resourceLoader;
	}

	@Get(value = "{path:.*}", consumes = {
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})
	public String catchallGet(String path) {
		LOG.info("GET CatchAll for path '{}'", path);
		return getResponseFromResources();
	}

	@Post(value = "{path:.*}", consumes = {
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})
	public String catchallPost(String path, @Body String payload) {
		LOG.info("POST CatchAll for path '{}' with payload '{}'", path, payload);
		return getResponseFromResources();
	}

	private String getResponseFromResources() {
		final String responseFileName = "mock/" + env.getProperty("response", String.class).orElse(null);
		LOG.info("Loading response '{}'", responseFileName);
		return resourceLoader.getResourceAsStream(responseFileName)
				.map(inputStream -> {
					try {
						return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
					} catch (IOException e) {
						return StringUtils.EMPTY;
					}
				})
				.orElse(StringUtils.EMPTY);
	}
}

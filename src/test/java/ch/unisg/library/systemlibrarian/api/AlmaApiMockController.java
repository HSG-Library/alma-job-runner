package ch.unisg.library.systemlibrarian.api;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.DefaultEnvironment;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Requires(property = "guard", value = "AlmaApiTest")
@Controller
public class AlmaApiMockController {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final DefaultEnvironment env;
	private final ClassPathResourceLoader resourceLoader;

	@Inject
	public AlmaApiMockController(
			final DefaultEnvironment env,
			final ClassPathResourceLoader resourceLoader) {
		this.env = env;
		this.resourceLoader = resourceLoader;
	}

	@Get(value = "{path:.*}", consumes = {
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})
	public HttpResponse<String> catchallGet(String path) {
		LOG.info("GET CatchAll for path '{}'", path);
		return getResponseFromResources()
				.map(body -> HttpResponse.status(getStatus()).body(body))
				.orElseGet(() -> HttpResponse.status(getStatus()));
	}

	@Post(value = "{path:.*}", consumes = {
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})
	public HttpResponse<String> catchallPost(String path, @Body String payload) {
		LOG.info("POST CatchAll for path '{}' with payload '{}'", path, payload);
		return getResponseFromResources()
				.map(body -> HttpResponse.status(getStatus()).body(body))
				.orElseGet(() -> HttpResponse.status(getStatus()));
	}

	private HttpStatus getStatus() {
		return env.getProperty("status", HttpStatus.class).orElse(HttpStatus.OK);
	}

	private Optional<String> getResponseFromResources() {
		return env.getProperty("response", String.class)
				.map(fileName -> StringUtils.prependIfMissing(fileName, "mock/"))
				.flatMap(resourceLoader::getResourceAsStream)
				.map(inputStream -> {
					try {
						return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
					} catch (IOException e) {
						return null;
					}
				});
	}
}

package ch.unisg.library.systemlibrarian.jobs;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@MicronautTest
public class JobConfigServiceTest {

	@Inject
	private JobConfigService jobConfigService;

	@Test
	public void testGetJobConfigs() throws IOException {
		final String testFile = """
				test-name

				*/4 * * 3 *

				POST /alma/api/test/path?op=test

				<xml>hello</xml>
				""";

		Path testWorkTree = Files.createTempDirectory("test");
		File testConfigFile = Files.createFile(Paths.get(testWorkTree.toString(), "test.conf")).toFile();
		Files.writeString(Paths.get(testConfigFile.toURI()), testFile);

		List<JobConfig> jobConfigs = jobConfigService.getJobConfigs(testWorkTree.toFile());

		Assertions.assertEquals(1, jobConfigs.size());
		JobConfig jobConfig = jobConfigs.get(0);
		Assertions.assertEquals("test-name", jobConfig.name());
		Assertions.assertEquals("*/4 * * 3 *", jobConfig.cronExpression());
		Assertions.assertEquals("POST", jobConfig.httpMethod());
		Assertions.assertEquals("/alma/api/test/path", jobConfig.apiPath());
		Assertions.assertEquals(Pair.of("op", "test"), jobConfig.apiQuery());
		Assertions.assertEquals("<xml>hello</xml>", jobConfig.xmlPayload());

		final boolean result = testConfigFile.delete();
		Assertions.assertTrue(result);
		Files.delete(testWorkTree);
	}
}

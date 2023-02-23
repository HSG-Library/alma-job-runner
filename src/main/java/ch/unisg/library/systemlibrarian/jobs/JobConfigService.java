package ch.unisg.library.systemlibrarian.jobs;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unisg.library.systemlibrarian.cron.CronValidatorService;
import jakarta.inject.Singleton;

@Singleton
public class JobConfigService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final CronValidatorService cronValidatorService;

	public JobConfigService(CronValidatorService cronValidatorService) {
		this.cronValidatorService = cronValidatorService;
	}

	public List<JobConfig> getJobConfigs(File workTree) {
		List<File> configFiles = getConfigFiles(workTree);
		return createJobConfigs(configFiles);
	}

	private List<File> getConfigFiles(File workTree) {
		final File[] fileArray = Objects.requireNonNull(workTree.listFiles((file, name) -> StringUtils.endsWith(name, ".conf")));
		return Arrays.stream(fileArray)
				.collect(Collectors.toList());
	}

	private List<JobConfig> createJobConfigs(List<File> configFiles) {
		return configFiles.stream()
				.map(this::createJobConfig)
				.flatMap(Optional::stream)
				.collect(Collectors.toList());
	}

	private Optional<JobConfig> createJobConfig(File configFile) {
		final List<String> configLines = getFileContents(configFile);

		if (configLines.size() < 4) {
			LOG.warn("Config file '{}', is not complete, the file has less than 4 lines", configFile.getAbsolutePath());
			return Optional.empty();
		}
		LOG.info("Trying to parse '{}'", configFile.getAbsolutePath());
		try {
			return parseConfigFile(configLines);
		} catch (RuntimeException e) {
			LOG.error("Could not parse the config file '{}'. Contents: '{}'", configFile.getAbsolutePath(), String.join("\n", configLines));
			LOG.info("Full Stacktrace:", e);
			return Optional.empty();
		}
	}

	private Optional<JobConfig> parseConfigFile(final List<String> configLines) {
		final String name = StringUtils.trim(configLines.remove(0));
		LOG.info("Name: '{}'", name);

		final String cronExpression = StringUtils.trim(configLines.remove(0));
		if (!cronValidatorService.isValid(cronExpression)) {
			throw new RuntimeException("Could not parse cron expression: " + cronExpression);
		}
		final String normalizedCronExpression = cronValidatorService.normalize(cronExpression);
		LOG.info("Cron expression: '{}', '{}'", normalizedCronExpression, cronValidatorService.describe(normalizedCronExpression));

		final String apiInfo = StringUtils.trim(configLines.remove(0));
		LOG.info("Api Info: '{}'", apiInfo);

		final String xmlPayload = StringUtils.trim(String.join("\n", configLines));
		LOG.info("XML payload: '{}'", xmlPayload);

		final String regex = "^(\\w+)\\s+([\\w/]+)\\?(\\w+)=(\\w+)$";
		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(apiInfo);

		if (matcher.find(0)) {
			final String httpMethod = matcher.group(1);
			LOG.info("HTTP method: '{}'", httpMethod);
			final String apiPath = matcher.group(2);
			LOG.info("API path: '{}'", apiPath);
			final Pair<String, String> query = Pair.of(matcher.group(3), matcher.group(4));
			LOG.info("Query: '{}'", query);
			JobConfig jobConfig = new JobConfig(name, cronExpression, httpMethod, apiPath, query, xmlPayload);
			LOG.info("Config file parsed, add config for job '{}'", name);
			return Optional.of(jobConfig);
		} else {
			throw new RuntimeException("Could not parse apiInfo, regex did not match: " + apiInfo);
		}
	}

	private List<String> getFileContents(File configFile) {
		try {
			LOG.info("Reading contents of '{}'", configFile.getAbsolutePath());
			return Files.readAllLines(configFile.toPath(), StandardCharsets.UTF_8).stream()
					.filter(StringUtils::isNotBlank)
					.collect(Collectors.toList());
		} catch (IOException e) {
			LOG.error("Could not read the contents of config file '{}'", configFile.getAbsolutePath());
			throw new RuntimeException("Could not read the contents of config file", e);
		}
	}
}

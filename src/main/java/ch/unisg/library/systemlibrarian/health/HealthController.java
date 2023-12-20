package ch.unisg.library.systemlibrarian.health;

import ch.unisg.library.systemlibrarian.api.AlmaApiHttpClient;
import ch.unisg.library.systemlibrarian.api.models.ResponseStatus;
import ch.unisg.library.systemlibrarian.jobs.JobSchedulerService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Controller
@ExecuteOn(TaskExecutors.BLOCKING)
public class HealthController {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final AlmaApiHttpClient almaApiHttpClient;
	private final JobSchedulerService jobSchedulerService;

	public HealthController(
			final AlmaApiHttpClient almaApiHttpClient,
			final JobSchedulerService jobSchedulerService
	) {
		this.almaApiHttpClient = almaApiHttpClient;
		this.jobSchedulerService = jobSchedulerService;
	}

	@Get(value = "/health", produces = MediaType.TEXT_PLAIN)
	public HttpResponse<String> health() {
		LOG.info("Health endpoint called, health OK");
		return HttpResponse.ok("OK");
	}

	@Get(value = "/status", produces = MediaType.TEXT_PLAIN)
	public HttpResponse<String> status() {
		final List<String> issues = new ArrayList<>();
		if (ResponseStatus.ERROR == almaApiHttpClient.apiHealthCheck().responseStatus()) {
			issues.add("Alma API not available");
		}
		if (jobSchedulerService.getScheduledJobs().isEmpty()) {
			issues.add("No jobs registered");
		}
		if (issues.isEmpty()) {
			LOG.info("Health endpoint called, health OK");
			return HttpResponse.ok("OK");
		}
		final String issuesList = String.join("\n", issues);
		LOG.warn("Health endpoint called, health NOT OK: '{}'", issuesList);
		return HttpResponse.serverError(issuesList);
	}
}

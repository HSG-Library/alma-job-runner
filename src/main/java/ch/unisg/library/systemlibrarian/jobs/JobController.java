package ch.unisg.library.systemlibrarian.jobs;

import java.util.List;
import java.util.stream.Collectors;

import ch.unisg.library.systemlibrarian.cron.CronValidatorService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/jobs")
public class JobController {

	private final JobSchedulerService jobSchedulerService;
	private final CronValidatorService cronValidatorService;

	public JobController(JobSchedulerService jobService, CronValidatorService cronValidatorService) {
		this.jobSchedulerService = jobService;
		this.cronValidatorService = cronValidatorService;
	}

	@Get("/register")
	public List<String> resgister() {
		jobSchedulerService.reRegisterAllJobs();
		return List.of("register done");
	}

	@Get("/unregister")
	public List<String> unregister() {
		jobSchedulerService.unregisterAllJobs();
		return List.of("unregister done");
	}

	@Get("/reregister")
	public List<String> reregister() {
		jobSchedulerService.reRegisterAllJobs();
		return List.of("reregister done");
	}

	@Get("/list")
	public List<String> list() {
		return jobSchedulerService.getScheduledJobs().stream()
				.map(jobConfig -> jobConfig.getName() + " - " + jobConfig.getCronExpression() + " (" + cronValidatorService.describe(jobConfig.getCronExpression()) + ")")
				.collect(Collectors.toList());
	}
}

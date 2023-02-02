package ch.unisg.library.systemlibrarian.jobs;

import java.util.List;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/jobs")
public class JobController {

	private final JobSchedulerService jobSchedulerService;

	public JobController(JobSchedulerService jobService) {
		this.jobSchedulerService = jobService;
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
}

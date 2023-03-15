package ch.unisg.library.systemlibrarian.jobs;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller("/jobs")
public class JobController {

	private final JobSchedulerService jobSchedulerService;
	private final JobDataTransformerService jobDataTransformerService;

	public JobController(
			final JobSchedulerService jobService,
			final JobDataTransformerService jobDataTransformerService) {
		this.jobSchedulerService = jobService;
		this.jobDataTransformerService = jobDataTransformerService;
	}

	@View("unregister")
	@Get("/unregister")
	public String unregister() {
		jobSchedulerService.unregisterAllJobs();
		return "Unregistered";
	}

	@View("reload")
	@Get("/reload")
	public HttpResponse<Map<String, Set<JobInfo>>> reregister() {
		final Set<JobInfo> jobInfo = jobSchedulerService.reRegisterAllJobs().stream()
				.map(jobDataTransformerService::toJobInfo)
				.collect(Collectors.toSet());
		return HttpResponse.ok().body(Map.of(
						"jobInfoList", jobInfo
				)
		);
	}

	@View("list")
	@Get("/list")
	public HttpResponse<Map<String, Set<JobInfo>>> list() {
		final Set<JobInfo> jobInfo = jobSchedulerService.getScheduledJobs().stream()
				.map(jobDataTransformerService::toJobInfo)
				.collect(Collectors.toSet());
		return HttpResponse.ok().body(Map.of(
						"jobInfoList", jobInfo
				)
		);
	}

	@View("results")
	@Get("/results")
	public HttpResponse<Map<String, Map<String, List<JobResultInfo>>>> results() {
		final Map<String, List<JobResultInfo>> results = jobSchedulerService.getResults().entrySet().stream()
				.map(entry -> Pair.of(entry.getKey().name(), entry.getValue().stream().map(jobDataTransformerService::toJobResultInfo).collect(Collectors.toList())))
				.collect(Collectors.toMap(Pair::getKey, Pair::getValue));

		return HttpResponse.ok().body(Map.of(
						"results", results
				)
		);
	}
}

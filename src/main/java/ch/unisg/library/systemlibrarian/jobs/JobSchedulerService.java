package ch.unisg.library.systemlibrarian.jobs;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unisg.library.systemlibrarian.api.AlmaApiHttpClient;
import ch.unisg.library.systemlibrarian.api.models.AlmaApiJobResponse;
import ch.unisg.library.systemlibrarian.cron.CronValidatorService;
import ch.unisg.library.systemlibrarian.git.GitService;
import ch.unisg.library.systemlibrarian.git.TempDirectoryService;
import io.micronaut.scheduling.TaskScheduler;
import jakarta.inject.Singleton;

@Singleton
public class JobSchedulerService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final TaskScheduler taskScheduler;
	private final JobConfigService jobConfigService;
	private final GitService gitService;
	private final TempDirectoryService gitDirectoryService;
	private final AlmaApiHttpClient almaApiHttpClient;
	private final CronValidatorService cronValidatorService;

	private Map<JobConfig, ScheduledFuture<?>> scheduledJobs;
	private final ConcurrentHashMap<JobConfig, List<AlmaApiJobResponse>> results;

	public JobSchedulerService(
			final TaskScheduler taskScheduler,
			final GitService gitService,
			final TempDirectoryService gitDirectoryService,
			final JobConfigService jobConfigService,
			final AlmaApiHttpClient almaApiHttpClient,
			final CronValidatorService cronValidatorService) {
		this.taskScheduler = taskScheduler;
		this.gitService = gitService;
		this.gitDirectoryService = gitDirectoryService;
		this.jobConfigService = jobConfigService;
		this.almaApiHttpClient = almaApiHttpClient;
		this.cronValidatorService = cronValidatorService;
		this.scheduledJobs = new HashMap<>();
		this.results = new ConcurrentHashMap<>();
	}

	public void registerAllJobs() {
		List<JobConfig> jobConfigs = getJobConfigs();
		LOG.info("Register '{}' jobs", jobConfigs.size());
		jobConfigs.forEach(this::registerJob);
	}

	public void unregisterAllJobs() {
		LOG.info("Unregister all '{}' jobs", scheduledJobs.size());
		scheduledJobs.forEach((key, value) -> {
			LOG.info("Unregister job '{}'", key.name());
			value.cancel(true);
		});
		scheduledJobs = new HashMap<>();
	}

	public Set<JobConfig> getScheduledJobs() {
		return scheduledJobs.keySet();
	}

	public Map<JobConfig, List<AlmaApiJobResponse>> getResults() {
		return results;
	}

	public void reRegisterAllJobs() {
		unregisterAllJobs();
		registerAllJobs();
	}

	private List<JobConfig> getJobConfigs() {
		final File cloneDir = gitDirectoryService.createTempCloneDirectory();
		final Git configRepo = gitService.cloneRepo(cloneDir);
		final File workTree = getWorkTree(configRepo);
		return jobConfigService.getJobConfigs(workTree);
	}

	private File getWorkTree(Git configRepo) {
		return configRepo.getRepository().getWorkTree();
	}

	private void registerJob(final JobConfig jobConfig) {
		LOG.info("Register job '{}' with cron expression '{}', '{}'", jobConfig.name(), jobConfig.cronExpression(), cronValidatorService.describe(jobConfig.cronExpression()));
		ScheduledFuture<?> scheduledJob = taskScheduler.schedule(jobConfig.cronExpression(), new JobRunnable(jobConfig, almaApiHttpClient, results));
		scheduledJobs.put(jobConfig, scheduledJob);
	}
}

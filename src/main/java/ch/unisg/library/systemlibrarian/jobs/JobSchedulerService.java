package ch.unisg.library.systemlibrarian.jobs;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unisg.library.systemlibrarian.api.AlmaApiHttpClient;
import ch.unisg.library.systemlibrarian.git.GitDirectoryService;
import ch.unisg.library.systemlibrarian.git.GitService;
import io.micronaut.scheduling.TaskScheduler;
import jakarta.inject.Singleton;

@Singleton
public class JobSchedulerService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final TaskScheduler taskScheduler;
	private final JobConfigService jobConfigService;
	private final GitService gitService;
	private final GitDirectoryService gitDirectoryService;
	private final AlmaApiHttpClient almaApiHttpClient;

	private Set<ScheduledFuture<?>> scheduledJobs;

	public JobSchedulerService(
			TaskScheduler taskScheduler,
			GitService gitService,
			GitDirectoryService gitDirectoryService,
			JobConfigService jobConfigService,
			AlmaApiHttpClient almaApiHttpClient) {
		this.taskScheduler = taskScheduler;
		this.gitService = gitService;
		this.gitDirectoryService = gitDirectoryService;
		this.jobConfigService = jobConfigService;
		this.almaApiHttpClient = almaApiHttpClient;
		this.scheduledJobs = new HashSet<>();
	}

	public void registerAllJobs() {
		List<JobConfig> jobConfigs = getJobConfigs();
		LOG.info("Register '{}' jobs", jobConfigs.size());
		jobConfigs.stream()
				.forEach(jobConfig -> registerJob(jobConfig));
	}

	public void unregisterAllJobs() {
		LOG.info("Unregister '{}' jobs", scheduledJobs.size());
		scheduledJobs.stream()
				.forEach(job -> job.cancel(true));
		scheduledJobs = new HashSet<>();
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
		LOG.info("Register job '{}' with cron expression '{}'", jobConfig.getName(), jobConfig.getCronExpression());
		ScheduledFuture<?> scheduledJob = taskScheduler.schedule(jobConfig.getCronExpression(), new JobRunnable(jobConfig, almaApiHttpClient));
		scheduledJobs.add(scheduledJob);
	}
}

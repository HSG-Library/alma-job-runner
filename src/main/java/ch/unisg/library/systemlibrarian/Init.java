package ch.unisg.library.systemlibrarian;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unisg.library.systemlibrarian.jobs.JobSchedulerService;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;

@Singleton
public class Init implements ApplicationEventListener<StartupEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final JobSchedulerService jobSchedulerService;

	public Init(JobSchedulerService jobSchedulerService) {
		this.jobSchedulerService = jobSchedulerService;
	}

	@Override
	public void onApplicationEvent(StartupEvent event) {
		LOG.info("Alma Job Runner Startup");
		jobSchedulerService.registerAllJobs();
	}
}

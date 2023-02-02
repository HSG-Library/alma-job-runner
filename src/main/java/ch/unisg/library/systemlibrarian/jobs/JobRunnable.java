package ch.unisg.library.systemlibrarian.jobs;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unisg.library.systemlibrarian.api.AlmaApiHttpClient;

public class JobRunnable implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final JobConfig jobConfig;
	private AlmaApiHttpClient almaApiHttpClient;

	public JobRunnable(JobConfig jobConfig, AlmaApiHttpClient almaApiHttpClient) {
		this.jobConfig = jobConfig;
		this.almaApiHttpClient = almaApiHttpClient;
	}

	@Override
	public void run() {
		LOG.info("*** Running job '{}' ***", jobConfig.getName());
		almaApiHttpClient.sendJobRequest(jobConfig);
		LOG.info("*** Job '{}' is done ***", jobConfig.getName());
	}
}

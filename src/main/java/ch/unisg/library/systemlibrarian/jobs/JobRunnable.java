package ch.unisg.library.systemlibrarian.jobs;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unisg.library.systemlibrarian.api.AlmaApiHttpClient;
import ch.unisg.library.systemlibrarian.api.models.AlmaApiJobResponse;

public class JobRunnable implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final JobConfig jobConfig;
	private final AlmaApiHttpClient almaApiHttpClient;
	private final ConcurrentHashMap<JobConfig, List<AlmaApiJobResponse>> results;

	public JobRunnable(
			final JobConfig jobConfig,
			final AlmaApiHttpClient almaApiHttpClient,
			final ConcurrentHashMap<JobConfig, List<AlmaApiJobResponse>> results) {
		this.jobConfig = jobConfig;
		this.almaApiHttpClient = almaApiHttpClient;
		this.results = results;
	}

	@Override
	public void run() {
		LOG.info("*** Running job '{}' ***", jobConfig.getName());
		final AlmaApiJobResponse response = almaApiHttpClient.sendJobRequest(jobConfig);
		if (results.containsKey(jobConfig)) {
			List<AlmaApiJobResponse> responses = results.get(jobConfig);
			responses.add(response);
			results.put(jobConfig, responses);
		} else {
			List<AlmaApiJobResponse> responses = new ArrayList<>();
			responses.add(response);
			results.put(jobConfig, responses);
		}
		LOG.info("response: '{}'", response);
		LOG.info("*** Job '{}' is done ***", jobConfig.getName());
	}
}

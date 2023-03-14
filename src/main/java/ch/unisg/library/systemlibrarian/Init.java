package ch.unisg.library.systemlibrarian;

import ch.unisg.library.systemlibrarian.api.AlmaApiConfig;
import ch.unisg.library.systemlibrarian.git.GitConfig;
import ch.unisg.library.systemlibrarian.jobs.JobSchedulerService;
import ch.unisg.library.systemlibrarian.notification.NotificationConfig;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@Singleton
@Requires(notEnv = "test")
public class Init implements ApplicationEventListener<StartupEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final JobSchedulerService jobSchedulerService;
	private final AlmaApiConfig almaApiConfig;
	private final GitConfig gitConfig;
	private final NotificationConfig notificationConfig;

	public Init(
			final JobSchedulerService jobSchedulerService,
			final AlmaApiConfig almaApiConfig,
			final GitConfig gitConfig,
			final NotificationConfig notificationConfig
	) {
		this.jobSchedulerService = jobSchedulerService;
		this.almaApiConfig = almaApiConfig;
		this.gitConfig = gitConfig;
		this.notificationConfig = notificationConfig;
	}

	@Override
	public void onApplicationEvent(StartupEvent event) {
		LOG.info("*** Alma Job Runner Startup ***");
		LOG.info("* Configuration status *");
		LOG.info("Config repo: '{}'", gitConfig.getRepoSSH());
		LOG.info("Git repo private key set: '{}'", StringUtils.isNotBlank(gitConfig.getPrivateKeyBase64()));
		LOG.info("Alma API URL: '{}'", almaApiConfig.getUrl());
		LOG.info("Alma API key set: '{}'", StringUtils.isNotBlank(almaApiConfig.getKey()));

		LOG.info("* Notifications status *");
		LOG.info("Notifications Active: '{}'", notificationConfig.getActive());
		if (notificationConfig.getActive()) {
			LOG.info("Using SMTP server: '{}:{}'", notificationConfig.getSmtpServer(), notificationConfig.getSmtpPort());
			LOG.info("SMTP user set: '{}'", StringUtils.isNotBlank(notificationConfig.getSmtpUser()));
			LOG.info("SMTP password set: '{}'", StringUtils.isNotBlank(notificationConfig.getSmtpPassword()));
			LOG.info("Sending emails to: '{}'", notificationConfig.getToAddress());
			LOG.info("Sending emails as: '{}'", notificationConfig.getFromAddress());
		}
		LOG.info("* Register all jobs *");
		jobSchedulerService.registerAllJobs();
	}
}

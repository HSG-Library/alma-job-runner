package ch.unisg.library.systemlibrarian.notification;

import ch.unisg.library.systemlibrarian.jobs.JobConfig;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.http.HttpStatus;

@Prototype
public class NotificationMessageProvider {
	private static final String SUBJECT_PREFIX = "[ALMA-JOB-RUNNER]";

	public NotificationMessage requestFailed(final HttpStatus httpStatus, final JobConfig jobConfig) {
		final String subject = SUBJECT_PREFIX + " Job " + jobConfig.name() + " failed to start";
		final String message = """
				The Job '%s' failed to start
								
				The request to Alma failed with response code '%s' and message '%s'.
								
				Full job config:
				%s
				"""
				.formatted(jobConfig.name(), httpStatus.getCode(), httpStatus.getReason(), jobConfig);
		return new NotificationMessage(subject, message);
	}

	public NotificationMessage testMessage() {
		return new NotificationMessage(SUBJECT_PREFIX + " test-subject", "test-body");
	}
}

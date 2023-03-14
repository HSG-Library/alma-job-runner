package ch.unisg.library.systemlibrarian.notification;

import ch.unisg.library.systemlibrarian.jobs.JobConfig;
import io.micronaut.http.HttpResponse;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
class NotificationMessageProviderTest {

	@Inject
	private NotificationMessageProvider messageProvider;

	@Test
	void testRequestFailedMessage() {
		final HttpResponse<?> response = HttpResponse.badRequest();
		final JobConfig jobConfig = new JobConfig("Test-Job name", "* * * * *", "POST", "/api/path", Pair.of("op", "run"), "payload");
		NotificationMessage message = messageProvider.requestFailed(response, jobConfig);
		final String expectedSubject = "[ALMA-JOB-RUNNER] Job Test-Job name failed to start";
		final String expectedMessage = """
				The Job 'Test-Job name' failed to start
								
				The request to Alma failed with response code '400' and message 'Bad Request'.
								
				Full job config:
				JobConfig{name='Test-Job name', cronExpression='* * * * *', httpMethod='POST', apiPath='/api/path', apiQuery=(op,run), xmlPayload='payload'}
				""";
		Assertions.assertEquals(expectedSubject, message.subject());
		Assertions.assertEquals(expectedMessage, message.message());
	}
}
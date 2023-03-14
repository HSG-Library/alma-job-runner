package ch.unisg.library.systemlibrarian.notification;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.value.PropertyNotFoundException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@MicronautTest
class NotificationSenderServiceTest {

	@Inject
	private NotificationSenderService notificationSenderService;

	@Inject
	private NotificationMessageProvider messageProvider;

	@Test
	@Property(name = "notification.active", value = "true")
	void failWhenActivatedButNotConfigured() {
		Assertions.assertThrows(PropertyNotFoundException.class, () -> notificationSenderService.send(messageProvider.testMessage()));
	}

	@Test
	void disabledMails() {
		NotificationSenderService senderSpy = Mockito.spy(notificationSenderService);
		senderSpy.send(messageProvider.testMessage());
		Mockito.verify(senderSpy, Mockito.times(1)).send(messageProvider.testMessage());
	}

	@Disabled
	@Test
	@Property(name = "notification.active", value = "true")
	@Property(name = "notification.smtp-server", value = "smtp.domain.tld")
	@Property(name = "notification.smtp-user", value = "user@domain.tld")
	@Property(name = "notification.smtp-password", value = "secret")
	@Property(name = "notification.to-address", value = "admin@domain.tld")
	@Property(name = "notification.from-address", value = "alma-job-runner@domain.tld")
	void integrationSendEmail() {
		notificationSenderService.send(messageProvider.testMessage());
	}
}

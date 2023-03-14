package ch.unisg.library.systemlibrarian.notification;

import io.micronaut.context.annotation.Prototype;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@Prototype
public class NotificationSenderService {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final NotificationConfig notificationConfig;
	private final MailerProvider mailerProvider;

	public NotificationSenderService(
			final NotificationConfig notificationConfig,
			final MailerProvider mailerProvider) {
		this.notificationConfig = notificationConfig;
		this.mailerProvider = mailerProvider;
	}

	public void send(NotificationMessage notificationMessage) {
		if (!notificationConfig.getActive()) {
			LOG.warn("Don't send a notification since sending emails is disabled (property 'mail.activate-notifications' set to 'false')");
			return;
		}
		final Email email = createEmail(notificationMessage);
		mailerProvider.getMailer()
				.ifPresentOrElse(
						mailer -> {
							try {
								LOG.info("Send notification email with subject '{}' to '{}'", notificationMessage.subject(), notificationConfig.getToAddress());
								mailer.sendMail(email);
							} catch (RuntimeException e) {
								LOG.error("Could not send notification email: '{}'", e.getMessage(), e);
							}
						},
						() -> LOG.error("Trying to send notification but could not get the mailer")
				);
	}

	private Email createEmail(final NotificationMessage notificationMessage) {
		return EmailBuilder.startingBlank()
				.to(notificationConfig.getToAddress())
				.from(notificationConfig.getFromAddress())
				.withSubject(notificationMessage.subject())
				.withPlainText(notificationMessage.message())
				.buildEmail();
	}
}

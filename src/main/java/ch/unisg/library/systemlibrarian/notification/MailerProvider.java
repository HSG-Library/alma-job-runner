package ch.unisg.library.systemlibrarian.notification;

import jakarta.inject.Singleton;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;

import java.util.Objects;
import java.util.Optional;

@Singleton
public class MailerProvider {

	private final NotificationConfig notificationConfig;
	private Optional<Mailer> mailer;

	public MailerProvider(final NotificationConfig notificationConfig) {
		this.notificationConfig = notificationConfig;
	}

	public Optional<Mailer> getMailer() {
		if (Objects.isNull(mailer)) {
			mailer = setMailer();
		}
		return mailer;
	}

	private Optional<Mailer> setMailer() {
		if (notificationConfig.getActive()) {
			return Optional.of(createMailer());
		} else {
			return Optional.empty();
		}
	}

	private Mailer createMailer() {
		return MailerBuilder
				.withSMTPServerHost(notificationConfig.getSmtpServer())
				.withSMTPServerPort(notificationConfig.getSmtpPort())
				.withSMTPServerUsername(notificationConfig.getSmtpUser())
				.withSMTPServerPassword(notificationConfig.getSmtpPassword())
				.withTransportStrategy(TransportStrategy.SMTP_TLS)
				.buildMailer();
	}
}

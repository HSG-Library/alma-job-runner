package ch.unisg.library.systemlibrarian.notification;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.bind.annotation.Bindable;

@ConfigurationProperties("notification")
@Requires(property = "notification")
public interface NotificationConfig {

	/*
	 * Set this to true if you want notifications via email, default is 'false'
	 * - override via environment variable 'NOTIFICATION_ACTIVE = true'
	 */
	@Bindable
	boolean getActive();

	/*
	 * Set via environment variable 'NOTIFICATION_SMTP_SERVER = "smtp.domain.tld"'
	 */
	@Bindable
	String getSmtpServer();

	/*
	 * Default is 587, override via environment variable 'NOTIFICATION_SMTP_PORT = "587"'
	 */
	@Bindable
	int getSmtpPort();

	/*
	 * Set via environment variable 'NOTIFICATION_SMTP_USER = "mailer"'
	 */
	@Bindable
	String getSmtpUser();

	/*
	 * Set via environment variable 'NOTIFICATION_SMTP_PASSWORD = "secret"'
	 */
	@Bindable
	String getSmtpPassword();

	/*
	 * Set via environment variable 'NOTIFICATION_TO_ADDRESS = "recipient@domain.tld"'
	 */
	@Bindable
	String getToAddress();

	/*
	 * Set via environment variable 'NOTIFICATION_FROM_ADDRESS = "sender@domain.tld"'
	 */
	@Bindable
	String getFromAddress();
}

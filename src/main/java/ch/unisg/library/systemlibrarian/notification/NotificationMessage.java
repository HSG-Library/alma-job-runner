package ch.unisg.library.systemlibrarian.notification;

import java.util.Objects;

public record NotificationMessage(String subject, String message) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		NotificationMessage that = (NotificationMessage) o;

		if (!Objects.equals(subject, that.subject)) return false;
		return Objects.equals(message, that.message);
	}

	@Override
	public int hashCode() {
		int result = subject != null ? subject.hashCode() : 0;
		result = 31 * result + (message != null ? message.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "NotificationMessage{" +
				"subject='" + subject + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}

package ch.unisg.library.systemlibrarian.api.models;

import io.micronaut.http.HttpStatus;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Serdeable
public record CommonResponse<T>(HttpStatus httpStatus,
                                ResponseStatus responseStatus,
                                String message,
																LocalDateTime date,
                                Optional<T> response) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CommonResponse<?> that = (CommonResponse<?>) o;

		if (httpStatus != that.httpStatus) return false;
		if (responseStatus != that.responseStatus) return false;
		if (!Objects.equals(message, that.message)) return false;
		if (!Objects.equals(date, that.date)) return false;
		return Objects.equals(response, that.response);
	}

	@Override
	public int hashCode() {
		int result = httpStatus != null ? httpStatus.hashCode() : 0;
		result = 31 * result + (responseStatus != null ? responseStatus.hashCode() : 0);
		result = 31 * result + (message != null ? message.hashCode() : 0);
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + (response != null ? response.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "CommonResponse{" +
				"httpStatus=" + httpStatus +
				", responseStatus=" + responseStatus +
				", message='" + message + '\'' +
				", date=" + date +
				", response=" + response +
				'}';
	}
}

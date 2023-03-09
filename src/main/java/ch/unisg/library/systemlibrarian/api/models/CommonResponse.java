package ch.unisg.library.systemlibrarian.api.models;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;
import java.util.Optional;

@Serdeable
public record CommonResponse<T>(int code, ResponseStatus responseStatus, String message, Optional<T> response) {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CommonResponse<?> that = (CommonResponse<?>) o;

		if (code != that.code) return false;
		if (responseStatus != that.responseStatus) return false;
		if (!Objects.equals(message, that.message)) return false;
		return Objects.equals(response, that.response);
	}

	@Override
	public String toString() {
		return "CommonResponse{" +
				"code=" + code +
				", status=" + responseStatus +
				", message='" + message + '\'' +
				", response=" + response +
				'}';
	}
}

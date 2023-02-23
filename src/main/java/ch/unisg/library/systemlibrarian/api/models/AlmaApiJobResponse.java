package ch.unisg.library.systemlibrarian.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public class AlmaApiJobResponse {
	private final String id;
	private final String name;
	@JsonProperty("additional_info")
	private AlmaApiAdditionalInfo additionalInfo;

	public AlmaApiJobResponse(String id, String name, AlmaApiAdditionalInfo additionalInfo) {
		this.id = id;
		this.name = name;
		this.additionalInfo = additionalInfo;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public AlmaApiAdditionalInfo getAdditionalInfo() {
		return additionalInfo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AlmaApiJobResponse that = (AlmaApiJobResponse) o;

		if (!Objects.equals(id, that.id)) return false;
		if (!Objects.equals(name, that.name)) return false;
		return Objects.equals(additionalInfo, that.additionalInfo);
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (additionalInfo != null ? additionalInfo.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "AlmaApiJobResponse{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", additionalInfo=" + additionalInfo +
				'}';
	}
}

package ch.unisg.library.systemlibrarian.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class AlmaApiJobResponse {
	private String id;
	private String name;
	@JsonProperty("additional_info")
	private AlmaApiAdditinalInfo additionalInfo;

	public AlmaApiJobResponse(String id, String name, AlmaApiAdditinalInfo additionalInfo) {
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

	public AlmaApiAdditinalInfo getAdditionalInfo() {
		return additionalInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AlmaApiJobResponse other = (AlmaApiJobResponse) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (additionalInfo == null) {
			if (other.additionalInfo != null) {
				return false;
			}
		} else if (!additionalInfo.equals(other.additionalInfo)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "AlmaApiJobResponse [id=" + id + ", name=" + name + ", additionalInfo=" + additionalInfo + "]";
	}
}

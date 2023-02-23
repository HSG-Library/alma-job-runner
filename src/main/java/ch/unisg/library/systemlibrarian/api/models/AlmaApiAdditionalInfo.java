package ch.unisg.library.systemlibrarian.api.models;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public class AlmaApiAdditionalInfo {

	private final String value;
	private final String link;

	public AlmaApiAdditionalInfo(String value, String link) {
		this.value = value;
		this.link = link;
	}

	public String getValue() {
		return value;
	}

	public String getLink() {
		return link;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AlmaApiAdditionalInfo that = (AlmaApiAdditionalInfo) o;

		if (!Objects.equals(value, that.value)) return false;
		return Objects.equals(link, that.link);
	}

	@Override
	public int hashCode() {
		int result = value != null ? value.hashCode() : 0;
		result = 31 * result + (link != null ? link.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "AlmaApiAdditionalInfo{" +
				"value='" + value + '\'' +
				", link='" + link + '\'' +
				'}';
	}
}

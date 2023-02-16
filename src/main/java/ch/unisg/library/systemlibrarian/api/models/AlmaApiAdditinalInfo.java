package ch.unisg.library.systemlibrarian.api.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class AlmaApiAdditinalInfo {

	private String link;

	public AlmaApiAdditinalInfo(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((link == null) ? 0 : link.hashCode());
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
		AlmaApiAdditinalInfo other = (AlmaApiAdditinalInfo) obj;
		if (link == null) {
			if (other.link != null) {
				return false;
			}
		} else if (!link.equals(other.link)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "AlmaApiAdditinalInfo [link=" + link + "]";
	}
}

package ch.unisg.library.systemlibrarian.jobs;

import java.util.Objects;

public record JobResultInfo(
		String date,
		String status,
		String message,
		String id,
		String name,
		String jobInstanceLink,
		String jobInstanceValue

) {
	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JobResultInfo that = (JobResultInfo) o;

		if (!Objects.equals(date, that.date)) return false;
		if (!Objects.equals(status, that.status)) return false;
		if (!Objects.equals(message, that.message)) return false;
		if (!Objects.equals(id, that.id)) return false;
		if (!Objects.equals(name, that.name)) return false;
		if (!Objects.equals(jobInstanceLink, that.jobInstanceLink))
			return false;
		return Objects.equals(jobInstanceValue, that.jobInstanceValue);
	}

	@Override
	public int hashCode() {
		int result = date != null ? date.hashCode() : 0;
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (message != null ? message.hashCode() : 0);
		result = 31 * result + (id != null ? id.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (jobInstanceLink != null ? jobInstanceLink.hashCode() : 0);
		result = 31 * result + (jobInstanceValue != null ? jobInstanceValue.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "JobResultInfo{" +
				"date='" + date + '\'' +
				", status='" + status + '\'' +
				", message='" + message + '\'' +
				", id='" + id + '\'' +
				", name='" + name + '\'' +
				", jobInstanceLink='" + jobInstanceLink + '\'' +
				", jobInstanceValue='" + jobInstanceValue + '\'' +
				'}';
	}
}

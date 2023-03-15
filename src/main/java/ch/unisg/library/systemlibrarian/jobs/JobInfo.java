package ch.unisg.library.systemlibrarian.jobs;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public record JobInfo(
		String name,
		String cronExpression,
		String cronDescription,
		String nextExecution
) {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JobInfo jobInfo = (JobInfo) o;

		if (!Objects.equals(name, jobInfo.name)) return false;
		return Objects.equals(cronExpression, jobInfo.cronExpression);
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (cronExpression != null ? cronExpression.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "JobInfo{" +
				"name='" + name + '\'' +
				", cronExpression='" + cronExpression + '\'' +
				'}';
	}
}

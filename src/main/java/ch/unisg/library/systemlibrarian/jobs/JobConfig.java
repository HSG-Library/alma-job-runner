package ch.unisg.library.systemlibrarian.jobs;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;

public record JobConfig(
		String name,
		String cronExpression,
		String httpMethod,
		String apiPath,
		Pair<String, String> apiQuery,
		String xmlPayload) {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JobConfig jobConfig = (JobConfig) o;

		if (!Objects.equals(name, jobConfig.name)) return false;
		if (!Objects.equals(cronExpression, jobConfig.cronExpression))
			return false;
		if (!Objects.equals(httpMethod, jobConfig.httpMethod)) return false;
		if (!Objects.equals(apiPath, jobConfig.apiPath)) return false;
		if (!Objects.equals(apiQuery, jobConfig.apiQuery)) return false;
		return Objects.equals(xmlPayload, jobConfig.xmlPayload);
	}

	@Override
	public String toString() {
		return "JobConfig{" +
				"name='" + name + '\'' +
				", cronExpression='" + cronExpression + '\'' +
				", httpMethod='" + httpMethod + '\'' +
				", apiPath='" + apiPath + '\'' +
				", apiQuery=" + apiQuery +
				", xmlPayload='" + xmlPayload + '\'' +
				'}';
	}
}

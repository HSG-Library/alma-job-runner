package ch.unisg.library.systemlibrarian.jobs;

import org.apache.commons.lang3.tuple.Pair;

public class JobConfig {

	private final String name;
	private final String cronExpression;
	private final String httpMethod;
	private final String apiPath;
	private final Pair<String, String> apiQuery;
	private final String xmlPayload;

	public JobConfig(String name, String cronExpression, String httpMethod, String apiPath, Pair<String, String> apiQuery, String xmlPayload) {
		this.name = name;
		this.cronExpression = cronExpression;
		this.httpMethod = httpMethod;
		this.apiPath = apiPath;
		this.apiQuery = apiQuery;
		this.xmlPayload = xmlPayload;
	}

	public String getName() {
		return name;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public String getApiPath() {
		return apiPath;
	}

	public Pair<String, String> getApiQuery() {
		return apiQuery;
	}

	public String getXmlPayload() {
		return xmlPayload;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((cronExpression == null) ? 0 : cronExpression.hashCode());
		result = prime * result + ((httpMethod == null) ? 0 : httpMethod.hashCode());
		result = prime * result + ((apiPath == null) ? 0 : apiPath.hashCode());
		result = prime * result + ((apiQuery == null) ? 0 : apiQuery.hashCode());
		result = prime * result + ((xmlPayload == null) ? 0 : xmlPayload.hashCode());
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
		JobConfig other = (JobConfig) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (cronExpression == null) {
			if (other.cronExpression != null) {
				return false;
			}
		} else if (!cronExpression.equals(other.cronExpression)) {
			return false;
		}
		if (httpMethod == null) {
			if (other.httpMethod != null) {
				return false;
			}
		} else if (!httpMethod.equals(other.httpMethod)) {
			return false;
		}
		if (apiPath == null) {
			if (other.apiPath != null) {
				return false;
			}
		} else if (!apiPath.equals(other.apiPath)) {
			return false;
		}
		if (apiQuery == null) {
			if (other.apiQuery != null) {
				return false;
			}
		} else if (!apiQuery.equals(other.apiQuery)) {
			return false;
		}
		if (xmlPayload == null) {
			if (other.xmlPayload != null) {
				return false;
			}
		} else if (!xmlPayload.equals(other.xmlPayload)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "JobConfig [name=" + name + ", cronExpression=" + cronExpression + ", httpMethod=" + httpMethod + ", apiPath=" + apiPath + ", apiQuery=" + apiQuery + ", xmlPayload=" + xmlPayload + "]";
	}
}

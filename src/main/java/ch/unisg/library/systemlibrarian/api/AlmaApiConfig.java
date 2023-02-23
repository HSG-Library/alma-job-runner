package ch.unisg.library.systemlibrarian.api;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.bind.annotation.Bindable;

@ConfigurationProperties("alma-api")
@Requires(property = "alma-api")
public interface AlmaApiConfig {

	@Bindable
	String getUrl();

	/*
	 * The api key must be provided environment variable: ALMA_API_KEY = "abc"
	 */
	@Bindable
	String getKey();

}

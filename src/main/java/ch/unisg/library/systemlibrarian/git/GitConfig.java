package ch.unisg.library.systemlibrarian.git;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.bind.annotation.Bindable;

@ConfigurationProperties("git")
@Requires(property = "git")
public interface GitConfig {

	@Bindable
	String getRepoSSH();

	@Bindable
	String getTempCloneDirPrefix();

	/*
	 * The private key must be provided as environment variable:
	 * GIT_PRIVATE_KEY_BASE64="VGhpcyBpcyBhIHRlc3Qgc3RyaW5n"
	 *
	 * (provide as one single line, base64 encoded)
	 */
	@Bindable
	String getPrivateKeyBase64();
}

package ch.unisg.library.systemlibrarian.git;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.bind.annotation.Bindable;

@ConfigurationProperties("git")
@Requires(property = "git")
public interface GitConfig {

	@Bindable
	public String getRepoSSH();

	@Bindable
	public String getBranch();

	@Bindable
	public String getTempCloneDirPrefix();

	/*
	 * The private key must be provided environment variable:
	 * GIT_PRIVATE_KEY_BASE64="VGhpcyBpcyBhIHRlc3Qgc3RyaW5n"
	 *
	 * (provide as one single line, base64 encoded)
	 */
	@Bindable
	public String getPrivateKeyBase64();

}

package ch.unisg.library.systemlibrarian.git;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import jakarta.inject.Singleton;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Base64;

@Singleton
public class GitService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final GitConfig gitConfig;

	public GitService(GitConfig gitConfig) {
		this.gitConfig = gitConfig;
	}

	public Git cloneRepo(File cloneDirectory) {
		try {
			LOG.info("Clone repository '{}', to '{}'", gitConfig.getRepoSSH(), cloneDirectory.getAbsolutePath());
			return Git.cloneRepository()
					.setDirectory(cloneDirectory)
					.setURI(gitConfig.getRepoSSH())
					.setTransportConfigCallback(new SshTransportConfigCallback(getPrivateKey()))
					.call();
		} catch (GitAPIException e) {
			LOG.error("Could not clone repository from '{}' to '{}'", gitConfig.getRepoSSH(), cloneDirectory.getAbsolutePath());
			throw new RuntimeException("Unable to clone Git repository", e);
		}
	}

	private String getPrivateKey() {
		byte[] decodedKey = Base64.getDecoder().decode(gitConfig.getPrivateKeyBase64());
		return new String(decodedKey);
	}

	private static class SshTransportConfigCallback implements TransportConfigCallback {

		private final String privateKey;

		private final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
			@Override
			protected void configure(OpenSshConfig.Host hc, Session session) {
				session.setConfig("StrictHostKeyChecking", "no");
				session.setConfig("PreferredAuthentications", "publickey");
			}

			@Override
			protected JSch createDefaultJSch(FS fs) throws JSchException {
				JSch jSch = super.createDefaultJSch(fs);
				jSch.removeAllIdentity();
				jSch.addIdentity("git", privateKey.getBytes(), null, null);
				return jSch;
			}
		};

		public SshTransportConfigCallback(String privateKey) {
			this.privateKey = privateKey;
		}

		@Override
		public void configure(Transport transport) {
			SshTransport sshTransport = (SshTransport) transport;
			sshTransport.setSshSessionFactory(sshSessionFactory);
		}
	}
}

package ch.unisg.library.systemlibrarian.git;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class GitDirectoryService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final GitConfig gitConfig;

	@Inject
	public GitDirectoryService(GitConfig gitConfig) {
		this.gitConfig = gitConfig;
	}

	public File createTempCloneDirectory() {
		try {
			Path tempDir = Files.createTempDirectory(gitConfig.getTempCloneDirPrefix());
			LOG.info("Created temporary directory '{}'", tempDir.toAbsolutePath());
			return tempDir.toFile();
		} catch (IOException e) {
			throw new RuntimeException("could not create local temp directory to clone repo", e);
		}
	}

	public void removeTempCloneDirectory(File directory) {
		if (directory.exists()) {
			directory.delete();
			LOG.info("Deleted directory '{}'", directory.toString());
		} else {
			LOG.warn("The direcory '{}' does not exist.", directory.toString());
		}
	}
}

package ch.unisg.library.systemlibrarian.git;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

@Singleton
public class TempDirectoryService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final GitConfig gitConfig;

	@Inject
	public TempDirectoryService(GitConfig gitConfig) {
		this.gitConfig = gitConfig;
	}

	public File createTempCloneDirectory() {
		try {
			final Path tempDir = Files.createTempDirectory(gitConfig.getTempCloneDirPrefix());
			LOG.info("Created temporary directory '{}'", tempDir.toAbsolutePath());
			return tempDir.toFile();
		} catch (IOException e) {
			throw new RuntimeException("could not create local temp directory to clone repo", e);
		}
	}

	public void removeTempCloneDirectory(File directory) {
		if (directory.exists()) {
			try (Stream<Path> walk = Files.walk(directory.toPath())) {
				walk.sorted(Comparator.reverseOrder())
						.map(Path::toFile)
						.forEach(file -> {
							final boolean status = file.delete();
							LOG.debug("Deleted '{}', status '{}'", file, status);
						});
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			final boolean deleteResult = directory.delete();
			LOG.info("Deleted directory '{}', status '{}'", directory, deleteResult);
		} else {
			LOG.warn("The directory '{}' does not exist.", directory);
		}
	}
}

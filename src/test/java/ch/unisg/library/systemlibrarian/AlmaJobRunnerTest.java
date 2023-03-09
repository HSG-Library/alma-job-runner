package ch.unisg.library.systemlibrarian;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
class AlmaJobRunnerTest {

	@Inject
	EmbeddedApplication<?> application;

	@Test
	public void testItWorks() {
		Assertions.assertTrue(application.isRunning());
	}
}

package ch.unisg.library.systemlibrarian;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class AlmaManualJobRunnerTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    public void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

}

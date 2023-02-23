package ch.unisg.library.systemlibrarian.cron;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class CronValidatorServiceTest {

	@Inject
	private CronValidatorService cronValidatorService;

	@ParameterizedTest
	@ValueSource(strings = {
			"*/30 * * * *", "* * * * *", "2 3 4 5 6", "0 22 * * 1-5", "23 0-20/2 * * *", "5 4 * * sun", "0 0,12 1 */2 *", "* *    * 3 *"
	})
	void testIsValid(String cronExpression) {
		Assertions.assertTrue(cronValidatorService.isValid(cronExpression));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"*/30 * * *", "1/* * * * *", "", "23 0-20/2 * * * *", "0 0,12 1 */2 xy", "78 * * * *",
	})
	@NullAndEmptySource
	void testIsInvalid(String cronExpression) {
		Assertions.assertFalse(cronValidatorService.isValid(cronExpression));
	}

	@ParameterizedTest
	@CsvSource({
			"*     * * * *,* * * * *",
			"/2 * * * *,*/2 * * * *",
			"  * * * * *   ,* * * * *"
	})
	void testNormalize(String cronExpression, String expectedNormalized) {
		final String normalized = cronValidatorService.normalize(cronExpression);
		System.out.println(normalized);
		Assertions.assertEquals(expectedNormalized, normalized);
	}

	@ParameterizedTest
	@CsvSource(value = {
			"*/30 * * * *;every 30 minutes",
			"* * * * *;every minute",
			"2 3 4 5 6;at 03:02 at 4 day at May month at Saturday day",
			"0 22 * * 1-5;at 22:00 every day between Monday and Friday",
			"23 0-20/2 * * *;every 2 hours at minute 23",
			"5 4 * * sun;at 04:05 at Sunday day",
			"0 0,12 1 */2 *;at 0 and 12 hours at 1 day every 2 months",
			"* *    * 3 *;every minute at March month"
	}, delimiter = ';')
	void testDescribe(String cronExpression, String expectedDescription) {
		final String description = cronValidatorService.describe(cronExpression);
		System.out.println(description);
		Assertions.assertEquals(expectedDescription, description);
	}
}

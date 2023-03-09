package ch.unisg.library.systemlibrarian.cron;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import io.micronaut.context.annotation.Prototype;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Prototype
public class CronValidatorService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public boolean isValid(String cronExpression) {
		try {
			Cron cron = parseCronExpression(cronExpression);
			cron.validate();
			return true;
		} catch (RuntimeException e) {
			LOG.error("Cron expression '{}' is not valid: '{}'", cronExpression, e.getMessage());
			return false;
		}
	}

	public String normalize(String cronExpression) {
		return parseCronExpression(cronExpression).asString();
	}

	public String describe(String cronExpression) {
		Cron cron = parseCronExpression(cronExpression);
		CronDescriptor descriptor = CronDescriptor.instance();
		return descriptor.describe(cron);
	}

	public String nextExecution(String cronExpression) {
		Cron cron = parseCronExpression(cronExpression);
		ExecutionTime executionTime = ExecutionTime.forCron(cron);
		return executionTime.nextExecution(ZonedDateTime.now())
				.map(zonedDateTime -> zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + zonedDateTime.format(DateTimeFormatter.ISO_DATE_TIME))
				.orElse(StringUtils.EMPTY);
	}

	private Cron parseCronExpression(String cronExpression) {
		CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));
		return parser.parse(cronExpression);
	}
}

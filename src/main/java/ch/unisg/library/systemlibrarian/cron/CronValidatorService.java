package ch.unisg.library.systemlibrarian.cron;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;

import jakarta.inject.Singleton;

@Singleton
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

	private Cron parseCronExpression(String cronExpression) {
		CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));
		return parser.parse(cronExpression);
	}
}

package ch.unisg.library.systemlibrarian.jobs;

import ch.unisg.library.systemlibrarian.api.models.AlmaApiJobResponse;
import ch.unisg.library.systemlibrarian.api.models.CommonResponse;
import ch.unisg.library.systemlibrarian.cron.CronValidatorService;
import io.micronaut.context.annotation.Prototype;

import java.time.format.DateTimeFormatter;

@Prototype
public class JobDataTransformerService {
	private static final String NO_VALUE = "-";
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private final CronValidatorService cronValidatorService;

	public JobDataTransformerService(
			final CronValidatorService cronValidatorService
	) {
		this.cronValidatorService = cronValidatorService;
	}

	public JobInfo toJobInfo(final JobConfig jobConfig) {
		return new JobInfo(
				jobConfig.name(),
				jobConfig.cronExpression(),
				cronValidatorService.describe(jobConfig.cronExpression()),
				cronValidatorService.nextExecution(jobConfig.cronExpression())
		);
	}

	public JobResultInfo toJobResultInfo(final CommonResponse<AlmaApiJobResponse> commonResponse) {
		return new JobResultInfo(
				commonResponse.date().format(DATE_TIME_FORMATTER),
				commonResponse.responseStatus().name().toLowerCase(),
				commonResponse.message(),
				commonResponse.response().map(AlmaApiJobResponse::getId).orElse(NO_VALUE),
				commonResponse.response().map(AlmaApiJobResponse::getName).orElse(NO_VALUE),
				commonResponse.response().map(almaApiJobResponse -> almaApiJobResponse.getAdditionalInfo().getLink()).orElse(NO_VALUE),
				commonResponse.response().map(almaApiJobResponse -> almaApiJobResponse.getAdditionalInfo().getValue()).orElse(NO_VALUE)
		);
	}
}

package click.yinsb.icmtracing.temporal.workflow;

import click.yinsb.icmtracing.temporal.activities.Activity001;
import click.yinsb.icmtracing.temporal.activities.HeartbeatActivity;
import click.yinsb.icmtracing.temporal.model.Constants;
import click.yinsb.icmtracing.temporal.model.EventMessage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

import java.time.Duration;

@WorkflowImpl(taskQueues = {Constants.ICM_TASK_QUEUE})
public class ChildWorkflow001Impl implements ChildWorkflow001 {

	private static final int DEFAULT_HEARTBEAT_INTERVAL_SECONDS = 60;

	private final Logger log = Workflow.getLogger(ChildWorkflow001.class.getName());

	private final Activity001 activity1 = Workflow.newActivityStub(Activity001.class,
			ActivityOptions.newBuilder()
					.setRetryOptions(
							RetryOptions.newBuilder()
									.setMaximumAttempts(8)
									.setInitialInterval(Duration.ofSeconds(2))
									.build()
					)
					.setStartToCloseTimeout(Duration.ofMinutes(2)).build());

	private final HeartbeatActivity heartbeatActivity = Workflow.newActivityStub(HeartbeatActivity.class,
			ActivityOptions.newBuilder()
					.setStartToCloseTimeout(Duration.ofSeconds(10))
					.build());

	@Override
	public void run(EventMessage eventMessage) {
		log.info("run child workflow 001");

		int intervalSeconds = eventMessage.getHeartbeatIntervalSeconds() != null
				? eventMessage.getHeartbeatIntervalSeconds()
				: DEFAULT_HEARTBEAT_INTERVAL_SECONDS;
		Duration interval = Duration.ofSeconds(intervalSeconds);

		// Capture workflow span context once (for heartbeat child spans)
		String[] spanContext = Workflow.sideEffect(String[].class, () -> {
			try {
				SpanContext ctx = Span.current().getSpanContext();
				if (ctx != null && ctx.isValid()) {
					return new String[]{ctx.getTraceId(), ctx.getSpanId()};
				}
			} catch (Exception ignored) {
			}
			return new String[]{"", ""};
		});
		String traceId = spanContext[0];
		String spanId = spanContext[1];

		// Run main activity and heartbeat loop in parallel
		Promise<Void> activityPromise = Async.procedure(() -> activity1.runActivity(eventMessage));

		while (true) {
			Workflow.sleep(interval);
			if (activityPromise.isCompleted()) {
				break;
			}
			heartbeatActivity.recordHeartbeat(traceId, spanId);
		}

		activityPromise.get();
	}
}

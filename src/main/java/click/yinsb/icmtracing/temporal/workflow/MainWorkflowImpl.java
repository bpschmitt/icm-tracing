
package click.yinsb.icmtracing.temporal.workflow;

import click.yinsb.icmtracing.temporal.model.Constants;
import click.yinsb.icmtracing.temporal.model.EventMessage;
import click.yinsb.icmtracing.temporal.model.Workflow001Result;
import click.yinsb.icmtracing.temporal.model.WorkflowResult;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WorkflowImpl(taskQueues = { Constants.ICM_TASK_QUEUE })
public class MainWorkflowImpl implements MainWorkflow {

	@Override
	// Note: @WithSpan doesn't work on Temporal workflow methods - Temporal creates spans automatically
	// @WithSpan("Run MainWorkflow")
	public WorkflowResult runAsync(EventMessage eventMessage) {
		log.info("run main workflow");
		// Note: Span.current() doesn't work reliably in workflows - use Temporal's built-in tracing

		// create Workflow Stubs
		ChildWorkflow001 stub1 = Workflow.newChildWorkflowStub(ChildWorkflow001.class);
		ChildWorkflow002 stub2 = Workflow.newChildWorkflowStub(ChildWorkflow002.class);

		// start workflows
		Workflow001Result flow1Result = stub1.run(eventMessage);

		return stub2.run(eventMessage, flow1Result);
	}
}

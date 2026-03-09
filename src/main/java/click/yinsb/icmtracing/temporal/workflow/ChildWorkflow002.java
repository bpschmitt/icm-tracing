
package click.yinsb.icmtracing.temporal.workflow;

import click.yinsb.icmtracing.temporal.model.EventMessage;
import click.yinsb.icmtracing.temporal.model.Workflow001Result;
import click.yinsb.icmtracing.temporal.model.WorkflowResult;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ChildWorkflow002 {

	@WorkflowMethod
	WorkflowResult run(EventMessage eventMessage, Workflow001Result workflow001Result);

}

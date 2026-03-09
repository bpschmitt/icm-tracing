package click.yinsb.icmtracing.temporal.workflow;

import click.yinsb.icmtracing.temporal.model.Constants;
import click.yinsb.icmtracing.temporal.model.EventMessage;
import click.yinsb.icmtracing.temporal.model.Workflow001Result;
import click.yinsb.icmtracing.temporal.model.WorkflowResult;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@WorkflowImpl(taskQueues = { Constants.ICM_TASK_QUEUE })
public class ChildWorkflow002Impl implements ChildWorkflow002 {

    @Override
    public WorkflowResult run(EventMessage eventMessage, Workflow001Result workflow001Result) {
        log.info("run child workflow 002");

        WorkflowResult result = WorkflowResult.PASS;

        Workflow.sleep(Duration.ofSeconds(5));

        if (!workflow001Result.isCompleted()) {
            result = WorkflowResult.FAIL;
        }

        return result;
    }

}

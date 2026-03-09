package click.yinsb.icmtracing.temporal.activities;

import click.yinsb.icmtracing.temporal.model.EventMessage;
import click.yinsb.icmtracing.temporal.model.Workflow001Result;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface Activity001 {

	@ActivityMethod
	Workflow001Result runActivity(EventMessage eventMessage);
}

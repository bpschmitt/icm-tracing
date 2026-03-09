package click.yinsb.icmtracing.temporal.activities;

import click.yinsb.icmtracing.temporal.model.Constants;
import click.yinsb.icmtracing.temporal.model.EventMessage;
import click.yinsb.icmtracing.temporal.model.Workflow001Result;
import io.opentelemetry.api.trace.Span;
import io.temporal.failure.ApplicationFailure;
import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@ActivityImpl(taskQueues = {Constants.ICM_TASK_QUEUE})
public class Activity001Impl implements Activity001 {

    @Autowired
    @Qualifier("myRestTemplate")
    private RestTemplate restTemplate;

    @Override
    public Workflow001Result runActivity(EventMessage eventMessage) {
        log.info("runActivity called");
        String id = eventMessage.getId();
        try {
            Span.current().setAttribute("todo.id", id);
            restTemplate.postForObject("http://localhost:8081/run-agent", eventMessage, Object.class);
            return new Workflow001Result();
        } catch (HttpClientErrorException.NotFound e) {
            log.warn(e.getMessage());
            throw ApplicationFailure.newNonRetryableFailure("Todo not found.", "TodoNotFound");
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}

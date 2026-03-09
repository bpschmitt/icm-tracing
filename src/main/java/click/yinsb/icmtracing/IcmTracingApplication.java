package click.yinsb.icmtracing;

import click.yinsb.icmtracing.temporal.model.Constants;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class IcmTracingApplication {

    static void main(String[] args) {
        SpringApplication.run(IcmTracingApplication.class, args);
    }

    @Bean("myRestTemplate")
    public RestTemplate creditInfoRestTemplate(RestTemplateBuilder builder) {
        return builder.additionalInterceptors(mdcCorrelationInterceptor()).build();
    }

    private ClientHttpRequestInterceptor mdcCorrelationInterceptor() {
        return (request, body, execution) -> {
            String correlationId = MDC.get("correlationId");
            if (correlationId != null && !correlationId.isEmpty()) {
                request.getHeaders().add(Constants.HEADER_CORRELATION_ID, correlationId);
            }
            return execution.execute(request, body);
        };
    }
}

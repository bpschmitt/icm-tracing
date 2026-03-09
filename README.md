
## Up & Running

```shell
# keys
export NR_ENDPOINT=https://otlp.nr-data.net:4317
export MY_NEW_RELIC_API_KEY=91xxxxxxxFFFFNRAL

# start temporal server, otel collector and jaeger
./scripts/startup.sh

# run app (Or start from your IDE)
mvn clean spring-boot:run

# trigger workflow
./scripts/01normal.sh
```
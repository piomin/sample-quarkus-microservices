## Quick Guide to Microservices with Quarkus on Openshift  [![Twitter](https://img.shields.io/twitter/follow/piotr_minkowski.svg?style=social&logo=twitter&label=Follow%20Me)](https://twitter.com/piotr_minkowski)

[![CircleCI](https://circleci.com/gh/piomin/sample-quarkus-microservices.svg?style=svg)](https://circleci.com/gh/piomin/sample-quarkus-microservices)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=piomin_sample-quarkus-microservices)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=piomin_sample-quarkus-microservices&metric=bugs)](https://sonarcloud.io/dashboard?id=piomin_sample-spring-microservices-new)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=piomin_sample-quarkus-microservices&metric=coverage)](https://sonarcloud.io/dashboard?id=piomin_sample-quarkus-microservices)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=piomin_sample-quarkus-microservices&metric=ncloc)](https://sonarcloud.io/dashboard?id=piomin_sample-quarkus-microservices)

Detailed description can be found here: [Quick Guide to Microservices with Quarkus on Openshift](https://piotrminkowski.com/2020/08/18/quick-guide-to-microservices-with-quarkus-on-openshift/)

## What's in this repo

This repository has grown well beyond the original 2020 article. Here is a summary of what it contains today.

### Services

Three independent Quarkus microservices, each a Maven module:

| Service | Port | Description |
|---|---|---|
| `employee-service` | 8080 | Base service; no dependencies on the other services |
| `department-service` | 8090 | Calls `employee-service` via a Quarkus REST client |
| `organization-service` | 8100 | Calls both `department-service` and `employee-service` via REST clients |

### Contract Testing

- **Pact** — consumer/provider contract tests (`*ContractTests.java`) use `quarkus-pact-consumer` and `quarkus-pact-provider`. A local Pact Broker can be started with `docker-compose up pact-broker` (depends on the `postgres` service). The broker URL is `http://localhost:9292`.
- **Microcks** — `department-service` includes a `microcks` Maven profile that runs `DepartmentExternalContractTests` against an external Microcks instance instead of the default WireMock stubs.
- **WireMock** — consumer-side contract tests use WireMock helpers (in the `wiremock` test package) to stub out the services they depend on.

### Observability

- **OpenTelemetry** — all three services have `quarkus-opentelemetry` enabled, exporting traces, metrics, and logs via OTLP.
- **Jaeger** — `docker-compose.yml` includes a Jaeger instance that receives the OTLP data. Bring up the full local stack with `docker-compose up`.

### Running Locally

Start the full environment (Postgres, Pact Broker, Jaeger, and all three services) with:

```bash
docker-compose up
```

To run a single service in Quarkus dev mode:

```bash
mvn -pl <service-directory> quarkus:dev
```

To build everything:

```bash
mvn clean install
```

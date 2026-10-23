# AGENTS.md

Guidance for AI coding agents working in this repository.

## Project Overview

A sample Quarkus microservices system demonstrating three services deployed together (originally targeting OpenShift):

- **employee-service** (port 8080) — base service, no dependencies on other services.
- **department-service** (port 8090) — calls `employee-service` via a REST client (`EmployeeClient`).
- **organization-service** (port 8100) — calls both `department-service` and `employee-service` via REST clients (`DepartmentClient`, `EmployeeClient`).

Each service is an independent Maven module under its own directory, built with the Quarkus Maven plugin. The root `pom.xml` is a parent POM (`pl.piomin.services:sample-quarkus-microservices`) that only declares the module list and shared build config (Quarkus BOM, Jacoco, Pact provider plugin).

## Architecture & Conventions

- **Language/runtime:** Java 21, Quarkus 3.x (see `quarkus.version` in root `pom.xml` for the exact pinned version — update via Renovate PRs, don't hand-edit casually).
- **Package base:** `pl.piomin.services.<service-name>` (e.g. `pl.piomin.services.department`), with `controller`, `model`, `repository`, `client` sub-packages per service.
- **REST layer:** JAX-RS resources in `controller` (RESTEasy + Jackson, e.g. `DepartmentController`, `EmployeeController`, `OrganizationController`). Validation via `quarkus-hibernate-validator`.
- **Inter-service calls:** Quarkus REST Client (`quarkus-resteasy-client-jackson`), interfaces in `client` package, configured via `quarkus.rest-client.<name>.url` in `application.properties` (with `%dev.` and `%openshift.` profile overrides for local/OpenShift URLs).
- **Persistence:** repository classes in `repository` package per service (in-memory/simple; no Hibernate ORM/Panache in this sample — check the actual repository class before assuming a persistence strategy).
- **Observability:** `quarkus-opentelemetry` enabled for traces/metrics/logs, exported to Jaeger (see `docker-compose.yml`). Log format includes `traceId`/`spanId`/`parentId`.
- **API docs:** `quarkus-smallrye-openapi` + Swagger UI always enabled (`quarkus.swagger-ui.always-include = true`).
- **Health:** `quarkus-smallrye-health`.
- **Container images:** built via `quarkus-container-image-jib`, image group `quarkus` (OpenShift profile uses a different registry/group — see `%openshift.` properties).

When adding a new endpoint or model field, mirror the existing pattern in the target service's `controller`/`model`/`repository` package rather than introducing a new style (e.g. don't introduce Panache or a different REST framework without discussion).

## Build & Test

- Build everything from the repo root: `mvn clean install` (or `mvn clean compile` for a quick syntax/compile check).
- Build/test a single module: `mvn -pl employee-service -am clean install` (`-am` also builds its dependencies, i.e. the parent POM).
- Run a service locally in dev mode: `mvn -pl <service> quarkus:dev` (dev-mode ports/URLs come from the `%dev.` profile properties in that service's `application.properties`).
- Unit/integration tests use JUnit 5, `quarkus-junit`, and `rest-assured`. Integration tests (`*IT.java`, e.g. `EmployeeControllerIT`) run via `maven-failsafe-plugin`, typically under the `native` profile against a native image.
- **Contract testing (Pact):** consumer/provider contract tests (`*ContractTests.java`) use `quarkus-pact-consumer`/`quarkus-pact-provider` plus WireMock helpers in the `wiremock` test package. A local Pact Broker is available via `docker-compose up pact-broker` (depends on `postgres`); the broker URL is configured in the root `pom.xml` (`pactBrokerUrl = http://localhost:9292`).
- **Microcks:** `department-service` has a `microcks` Maven profile that runs `DepartmentExternalContractTests` against an external Microcks instance instead of the default WireMock-based contract tests.
- Code coverage via Jacoco (`jacoco-maven-plugin`, configured in root `pom.xml`) — runs automatically with `mvn test`/`install`.
- Native builds: activate the `native` Maven profile (`-Dnative`) in a given service module; this also switches failsafe to run against the compiled native binary.
- OpenShift deployment: activate the `openshift` Maven profile (`-Dopenshift`), which pulls in `quarkus-openshift` and sets `quarkus.kubernetes.deploy=true`.

Always verify a change compiles/builds with `mvn -pl <changed-service> -am clean install` (or the full `mvn clean install` for cross-module changes) before considering the task done.

## Local Multi-Service Environment

`docker-compose.yml` wires up `postgres`, `pact-broker`, `jaeger`, and the three services (`employee`, `department`, `organization`) with the correct `links` and OTLP endpoint env vars, using pre-built images (`quarkus/<service>:<version>`). Rebuild images (e.g. via the Jib plugin: `mvn -pl <service> package -Dquarkus.container-image.build=true`) before `docker-compose up` if you've changed service code.

## CI/CD

- **CircleCI** is the primary CI (`circleci` badge in `readme.md`); check `.circleci/config.yml` if present for the actual pipeline steps (build, Docker Compose setup, Sonar analysis).
- **SonarCloud** (`piomin_sample-quarkus-microservices` project) analyzes code quality/coverage/bugs on each build — keep changes warning-clean (see recent commit `fix warnings + Sonar`).
- **Renovate** (`renovate.json`) manages dependency version bumps (Quarkus, Surefire/Failsafe, Maven plugins) via automated PRs — prefer letting Renovate handle routine version bumps rather than hand-editing `<*.version>` properties, unless specifically asked to upgrade something.
- GitHub Actions workflows under `.github/workflows/` (`opencode.yml`, `opencode-review.yml`, `opencode-review-trigger.yml`) integrate OpenCode-based automation (e.g. AI-assisted PR review) — see those files for the exact triggers/steps.

## Critical Restrictions

- **Never commit `target/` directories** or any other build output — they're Maven-generated and must stay untracked.
- Don't commit secrets/credentials (e.g. Pact Broker, registry credentials) — use environment variables or CI secrets as already configured.
- Don't change the module structure (service boundaries, package roots) without explicit instruction — the contract tests and inter-service REST clients depend on the current package/URL layout.

## Working With AI Agents Here

- Be specific about which service a change targets (`employee-service`, `department-service`, or `organization-service`) — they have similar but not identical package structures and dependencies (e.g. only `department-service` and `organization-service` have REST clients to other services; only `employee-service` lacks one).
- When adding a new REST endpoint, follow the existing controller in that service as the template for style, validation, and error handling.
- When a change affects a REST client's contract (request/response shape) between services, update the corresponding Pact contract tests (`*ContractTests.java`) in both the consumer and provider services, not just the code.
- After generating code, build the affected module(s) with Maven to confirm correctness — don't rely on inspection alone.

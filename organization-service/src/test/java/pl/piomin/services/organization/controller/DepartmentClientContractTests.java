package pl.piomin.services.organization.controller;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.MockServerConfig;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.piomin.services.organization.client.DepartmentClient;
import pl.piomin.services.organization.model.Department;
import pl.piomin.services.organization.wiremock.PactMockServer;
import pl.piomin.services.organization.wiremock.PactMockServerWorkaround;
import pl.piomin.services.organization.wiremock.WireMockQuarkusTestResource;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(PactMockServerWorkaround.class)
@MockServerConfig(port = "0")
@QuarkusTestResource(WireMockQuarkusTestResource.class)
public class DepartmentClientContractTests {

    @Pact(provider = "department-service", consumer = "organization-service")
    public V4Pact callFindDepartment(PactDslWithProvider builder) {
        DslPart body = PactDslJsonArray.arrayEachLike()
                .integerType("id")
                .stringType("name")
                .closeObject();
        DslPart body2 = PactDslJsonArray.arrayEachLike()
                .integerType("id")
                .stringType("name")
                .array("employees")
                   .object()
                      .integerType("id")
                      .stringType("name")
                      .stringType("position")
                      .integerType("age")
                    .closeObject()
                .closeArray();
        return builder
                .given("findByOrganization")
                   .uponReceiving("findByOrganization")
                       .path("/departments/organization/1")
                       .method("GET")
                   .willRespondWith()
                      .status(200)
                      .body(body)
                .given("findByOrganizationWithEmployees")
                   .uponReceiving("findByOrganizationWithEmployees")
                      .path("/departments/organization/1/with-employees")
                      .method("GET")
                   .willRespondWith()
                      .status(200)
                      .body(body2)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(providerName = "department-service", pactVersion = PactSpecVersion.V4)
    public void verifyFindByOrganizationPact(final PactMockServer mockServer) {
        DepartmentClient client = RestClientBuilder.newBuilder()
                .baseUri(URI.create(mockServer.getUrl()))
                .build(DepartmentClient.class);
        List<Department> departments = client.findByOrganization(1L);
        assertNotNull(departments);
        assertTrue(departments.size() > 0);
        assertNotNull(departments.get(0).getId());

        departments = client.findByOrganizationWithEmployees(1L);
        assertNotNull(departments);
        assertTrue(departments.size() > 0);
        assertNotNull(departments.get(0).getId());
        assertFalse(departments.get(0).getEmployees().isEmpty());
    }

}

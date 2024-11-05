package pl.piomin.services.department.controller;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.target.TestTarget;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.piomin.services.department.client.EmployeeClient;

@QuarkusTest
@Provider("department-service")
@PactBroker(url = "http://localhost:9292")
public class DepartmentContractTests {

    @ConfigProperty(name = "quarkus.http.test-port")
    int quarkusPort;

    @TestTarget
    HttpTestTarget target = new HttpTestTarget("localhost", this.quarkusPort);

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
        System.setProperty("pact.provider.version", "1.3");
        System.setProperty("pact.verifier.publishResults", "true");
    }

    @BeforeEach
    void beforeEach(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", this.quarkusPort));
    }

    @State("findByOrganization")
    void findByOrganization() {

    }

    @RestClient
    @InjectMock
    EmployeeClient employeeClient;

    @State("findByOrganizationWithEmployees")
    void findByOrganizationWithEmployees() {

    }

}

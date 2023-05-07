package pl.piomin.services.employee.controller;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.target.TestTarget;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.piomin.services.employee.repository.EmployeeRepository;

import javax.inject.Inject;

@QuarkusTest
@Provider("employee-service")
@PactBroker(url = "http://localhost:9080")
public class EmployeeContractTests {

    @ConfigProperty(name = "quarkus.http.test-port")
    int quarkusPort;

    @Inject
    EmployeeRepository repository;
    @TestTarget
    HttpTestTarget target = new HttpTestTarget("localhost", this.quarkusPort);

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void beforeEach(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", this.quarkusPort));
    }

    @State("findByDepartment")
    void findByDepartment() {

    }
}

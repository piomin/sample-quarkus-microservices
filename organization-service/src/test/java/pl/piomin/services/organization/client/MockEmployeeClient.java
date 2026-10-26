package pl.piomin.services.organization.client;

import io.quarkus.test.Mock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.piomin.services.organization.model.Employee;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.List;

@Mock
@ApplicationScoped
@RestClient
public class MockEmployeeClient implements EmployeeClient {

    @Override
    public List<Employee> findByOrganization(Long organizationId) {
        return Collections.singletonList(new Employee(1L, "Test1", 30, "Developer"));
    }

}

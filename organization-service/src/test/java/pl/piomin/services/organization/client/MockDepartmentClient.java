package pl.piomin.services.organization.client;

import io.quarkus.test.Mock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.piomin.services.organization.model.Department;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.List;

@Mock
@ApplicationScoped
@RestClient
public class MockDepartmentClient implements DepartmentClient {

    @Override
    public List<Department> findByOrganization(Long organizationId) {
        return Collections.singletonList(new Department("Test1"));
    }

    @Override
    public List<Department> findByOrganizationWithEmployees(Long organizationId) {
        return null;
    }

}

package pl.piomin.services.employee.controller;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.piomin.services.employee.model.Employee;

import java.net.URL;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusIntegrationTest
public class EmployeeControllerIT {

    @TestHTTPEndpoint(EmployeeController.class)
    @TestHTTPResource
    URL url;

    @Test
    void add() {
        EmployeeService service = RestClientBuilder.newBuilder()
                .baseUrl(url)
                .build(EmployeeService.class);
        Employee employee = new Employee(1L, 1L, "Josh Stevens", 23, "Developer");
        employee = service.add(employee);
        assertNotNull(employee.getId());
    }

    @Test
    public void findAll() {
        EmployeeService service = RestClientBuilder.newBuilder()
                .baseUrl(url)
                .build(EmployeeService.class);
        Set<Employee> employees = service.findAll();
        assertTrue(employees.size() >= 3);
    }

    @Test
    public void findById() {
        EmployeeService service = RestClientBuilder.newBuilder()
                .baseUrl(url)
                .build(EmployeeService.class);
        Employee employee = service.findById(1L);
        assertNotNull(employee.getId());
    }
}

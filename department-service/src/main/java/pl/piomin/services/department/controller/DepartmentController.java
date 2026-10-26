package pl.piomin.services.department.controller;

import io.smallrye.common.annotation.RunOnVirtualThread;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.quarkus.logging.Log;
import pl.piomin.services.department.client.EmployeeClient;
import pl.piomin.services.department.model.Department;
import pl.piomin.services.department.model.Employee;
import pl.piomin.services.department.repository.DepartmentRepository;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/departments")
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentController {

    @Inject
    DepartmentRepository repository;
    @Inject
    @RestClient
    EmployeeClient employeeClient;

    @POST
    public Department add(@Valid Department department) {
        Log.infof("Department add: %s", department);
        return repository.add(department);
    }

    @Path("/{id}")
    @GET
    public Department findById(@PathParam("id") Long id) {
        Log.infof("Department find: id=%d", id);
        return repository.findById(id);
    }

    @GET
    public Set<Department> findAll() {
        Log.info("Department find");
        return repository.findAll();
    }

    @Path("/organization/{organizationId}")
    @GET
    public Set<Department> findByOrganization(@PathParam("organizationId") Long organizationId) {
        Log.infof("Department find: organizationId=%d", organizationId);
        return repository.findByOrganization(organizationId);
    }

    @Path("/organization/{organizationId}/with-employees")
    @GET
    @RunOnVirtualThread
    public Set<Department> findByOrganizationWithEmployees(@PathParam("organizationId") Long organizationId) {
        Log.infof("Department find: organizationId=%d", organizationId);
        Set<Department> departments = repository.findByOrganization(organizationId);
        if (departments.isEmpty()) {
            return departments;
        }
        String ids = departments.stream()
                .map(d -> d.getId().toString())
                .collect(Collectors.joining(","));
        List<Employee> employees = employeeClient.findByDepartments(ids);
        Map<Long, List<Employee>> byDepartment = employees.stream()
                .filter(e -> e.departmentId() != null)
                .collect(Collectors.groupingBy(Employee::departmentId));
        departments.forEach(d -> d.setEmployees(byDepartment.getOrDefault(d.getId(), List.of())));
        return departments;
    }

}

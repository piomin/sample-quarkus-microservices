package pl.piomin.services.department.controller;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.quarkus.logging.Log;
import pl.piomin.services.department.client.EmployeeClient;
import pl.piomin.services.department.model.Department;
import pl.piomin.services.department.repository.DepartmentRepository;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Set;

@Path("/departments")
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentController {

    @Inject
    DepartmentRepository repository;
    @Inject
    @RestClient
    EmployeeClient employeeClient;

    @Path("/")
    @POST
    public Department add(@Valid Department department) {
        Log.infof("Department add: %s", department);
        return repository.add(department);
    }

    @Path("/{id}")
    @GET
    public Department findById(@PathParam("id") Long id) {
        Log.infof("Department find: id=%s", id);
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
        Log.infof("Department find: organizationId=%s", organizationId);
        return repository.findByOrganization(organizationId);
    }

    @Path("/organization/{organizationId}/with-employees")
    @GET
    public Set<Department> findByOrganizationWithEmployees(@PathParam("organizationId") Long organizationId) {
        Log.infof("Department find: organizationId=%s", organizationId);
        Set<Department> departments = repository.findByOrganization(organizationId);
        departments.forEach(d -> d.setEmployees(employeeClient.findByDepartment(d.getId())));
        return departments;
    }

}

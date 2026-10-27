package pl.piomin.services.department.controller;

import io.smallrye.common.annotation.RunOnVirtualThread;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.quarkus.logging.Log;
import pl.piomin.services.department.client.EmployeeClient;
import pl.piomin.services.department.model.Department;
import pl.piomin.services.department.repository.DepartmentRepository;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Collection;

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
    public Collection<Department> findAll() {
        Log.info("Department find");
        return repository.findAll();
    }

    @Path("/organization/{organizationId}")
    @GET
    public Collection<Department> findByOrganization(@PathParam("organizationId") Long organizationId) {
        Log.infof("Department find: organizationId=%d", organizationId);
        return repository.findByOrganization(organizationId);
    }

    @Path("/organization/{organizationId}/with-employees")
    @GET
    @RunOnVirtualThread
    public Collection<Department> findByOrganizationWithEmployees(@PathParam("organizationId") Long organizationId) {
        Log.infof("Department find: organizationId=%d", organizationId);
        Collection<Department> departments = repository.findByOrganization(organizationId);
        departments.forEach(d -> d.setEmployees(employeeClient.findByDepartment(d.getId())));
        return departments;
    }

}

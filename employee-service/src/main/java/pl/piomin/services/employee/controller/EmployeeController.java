package pl.piomin.services.employee.controller;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import io.quarkus.logging.Log;
import pl.piomin.services.employee.model.Employee;
import pl.piomin.services.employee.repository.EmployeeRepository;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Collection;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeController {

    @Inject
    EmployeeRepository repository;

    @POST
    @APIResponses(value = {
          @APIResponse(responseCode = "200", content = {
                @Content(mediaType = "application/json", examples = {
                      @ExampleObject(name = "add_employee", value = "{\"id\": 4, \"name\": \"Test User 4\", \"age\": 50, \"organizationId\": 2, \"departmentId\": 3, \"position\": \"tester\"}")
                })
          })
    })
    public Employee add(
          @RequestBody(content = {
                @Content(mediaType = "application/json", examples = {
                      @ExampleObject(name = "add_employee", summary = "Hire a new employee", description = "Should return 200",
                            value = "{\"id\": 4, \"name\": \"Test User 4\", \"age\": 50, \"organizationId\": 2, \"departmentId\": 3, \"position\": \"tester\"}")
                })
          })
          @Valid Employee employee) {
        Log.infof("Employee add: %s", employee);
        return repository.add(employee);
    }

    @Path("/{id}")
    @GET
    public Employee findById(@PathParam("id") Long id) {
        Log.infof("Employee find: id=%d", id);
        return repository.findById(id);
    }

    @GET
    @APIResponses(value = {
          @APIResponse(responseCode = "200", content = {
                @Content(mediaType = "application/json", examples = {
                      @ExampleObject(name = "all_persons", value = "[\n" +
                            "{\"id\": 1, \"name\": \"Test User 1\", \"age\": 20, \"organizationId\": 1, \"departmentId\": 1, \"position\": \"developer\"},\n" +
                            "{\"id\": 2, \"name\": \"Test User 2\", \"age\": 30, \"organizationId\": 1, \"departmentId\": 2, \"position\": \"architect\"},\n" +
                            "{\"id\": 3, \"name\": \"Test User 3\", \"age\": 40, \"organizationId\": 2, \"departmentId\": 3, \"position\": \"developer\"},\n" +
                            "]")
                })
          })
    })

    public Collection<Employee> findAll() {
        Log.info("Employee find");
        return repository.findAll();
    }

    @Path("/department/{departmentId}")
    @GET
    @APIResponses(value = {
          @APIResponse(responseCode = "200", content = {
                @Content(mediaType = "application/json", examples = {
                      @ExampleObject(name = "find_by_dep_1", value = "[\n" +
                            "{ \"id\": 1, \"name\": \"Test User 1\", \"age\": 20, \"organizationId\": 1, \"departmentId\": 1, \"position\": \"developer\" }\n" +
                            "]")
                })
          })
    })
    public Collection<Employee> findByDepartment(
          @Parameter(examples = { @ExampleObject(name = "find_by_dep_1", summary = "Main id of department", value = "1") })
          @PathParam("departmentId") Long departmentId) {
        Log.infof("Employee find: departmentId=%d", departmentId);
        return repository.findByDepartment(departmentId);
    }

    @Path("/organization/{organizationId}")
    @GET
    public Collection<Employee> findByOrganization(@PathParam("organizationId") Long organizationId) {
        Log.infof("Employee find: organizationId=%d", organizationId);
        return repository.findByOrganization(organizationId);
    }

}

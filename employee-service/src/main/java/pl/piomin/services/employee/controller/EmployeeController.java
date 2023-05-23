package pl.piomin.services.employee.controller;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.employee.model.Employee;
import pl.piomin.services.employee.repository.EmployeeRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

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
        LOGGER.info("Employee add: {}", employee);
        return repository.add(employee);
    }

    @Path("/{id}")
    @GET
    public Employee findById(@PathParam("id") Long id) {
        LOGGER.info("Employee find: id={}", id);
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
    public Set<Employee> findAll() {
        LOGGER.info("Employee find");
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
    public Set<Employee> findByDepartment(
          @Parameter(examples = { @ExampleObject(name = "find_by_dep_1", summary = "Main id of department", value = "1") })
          @PathParam("departmentId") Long departmentId) {
        LOGGER.info("Employee find: departmentId={}", departmentId);
        return repository.findByDepartment(departmentId);
    }

    @Path("/organization/{organizationId}")
    @GET
    public Set<Employee> findByOrganization(@PathParam("organizationId") Long organizationId) {
        LOGGER.info("Employee find: organizationId={}", organizationId);
        return repository.findByOrganization(organizationId);
    }

}

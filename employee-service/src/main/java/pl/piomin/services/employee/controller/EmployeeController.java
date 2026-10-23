package pl.piomin.services.employee.controller;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.employee.client.DepartmentClient;
import pl.piomin.services.employee.client.OrganizationClient;
import pl.piomin.services.employee.model.Employee;
import pl.piomin.services.employee.repository.EmployeeRepository;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Set;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Inject
    EmployeeRepository repository;

    @Inject
    @RestClient
    DepartmentClient departmentClient;

    @Inject
    @RestClient
    OrganizationClient organizationClient;

    @POST
    @APIResponses(value = {
          @APIResponse(responseCode = "200", content = {
                @Content(mediaType = "application/json", examples = {
                      @ExampleObject(name = "add_employee", value = "{\"id\": 4, \"name\": \"Test User 4\", \"age\": 50, \"organizationId\": 2, \"departmentId\": 3, \"position\": \"tester\"}")
                })
          }),
          @APIResponse(responseCode = "404", description = "Department or organization not found")
    })
    public Response add(
          @RequestBody(content = {
                @Content(mediaType = "application/json", examples = {
                      @ExampleObject(name = "add_employee", summary = "Hire a new employee", description = "Should return 200",
                            value = "{\"id\": 4, \"name\": \"Test User 4\", \"age\": 50, \"organizationId\": 2, \"departmentId\": 3, \"position\": \"tester\"}")
                })
          })
          @Valid Employee employee) {
        LOGGER.info("Employee add: {}", employee);
        try {
            departmentClient.findById(employee.getDepartmentId());
        } catch (Exception e) {
            LOGGER.warn("Department not found: id={}", employee.getDepartmentId());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Department not found: " + employee.getDepartmentId())
                    .build();
        }
        try {
            organizationClient.findById(employee.getOrganizationId());
        } catch (Exception e) {
            LOGGER.warn("Organization not found: id={}", employee.getOrganizationId());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Organization not found: " + employee.getOrganizationId())
                    .build();
        }
        return Response.ok(repository.add(employee)).build();
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

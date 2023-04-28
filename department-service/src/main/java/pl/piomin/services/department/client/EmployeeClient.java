package pl.piomin.services.department.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.piomin.services.department.model.Employee;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path("/employees")
@RegisterRestClient
public interface EmployeeClient {

    @GET
    @Path("/department/{departmentId}")
    @Produces(MediaType.APPLICATION_JSON)
    List<Employee> findByDepartment(@PathParam("departmentId") Long departmentId);

}

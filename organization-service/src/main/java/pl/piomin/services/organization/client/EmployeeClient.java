package pl.piomin.services.organization.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.piomin.services.organization.model.Employee;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/employees")
@RegisterRestClient
public interface EmployeeClient {

    @GET
    @Path("/organization/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    List<Employee> findByOrganization(@PathParam("organizationId") Long organizationId);

}

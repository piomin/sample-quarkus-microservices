package pl.piomin.services.organization.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.piomin.services.organization.model.Department;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path("/departments")
@RegisterRestClient(configKey = "department")
public interface DepartmentClient {

    @GET
    @Path("/organization/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    List<Department> findByOrganization(@PathParam("organizationId") Long organizationId);

    @GET
    @Path("/organization/{organizationId}/with-employees")
    @Produces(MediaType.APPLICATION_JSON)
    List<Department> findByOrganizationWithEmployees(@PathParam("organizationId") Long organizationId);

}

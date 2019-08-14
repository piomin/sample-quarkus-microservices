package pl.piomin.services.organization.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.piomin.services.organization.model.Department;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/departments")
@RegisterRestClient
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

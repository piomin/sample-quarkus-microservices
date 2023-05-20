package com.example.ms.services.organization.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import com.example.ms.services.organization.model.Department;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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

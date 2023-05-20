package com.example.ms.services.department.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.example.ms.services.department.model.Employee;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path("/employees")
@RegisterRestClient(configKey = "employee")
public interface EmployeeClient {

    @GET
    @Path("/department/{departmentId}")
    @Produces(MediaType.APPLICATION_JSON)
    List<Employee> findByDepartment(@PathParam("departmentId") Long departmentId);

}

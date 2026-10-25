package pl.piomin.services.organization.controller;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.quarkus.logging.Log;
import pl.piomin.services.organization.client.DepartmentClient;
import pl.piomin.services.organization.client.EmployeeClient;
import pl.piomin.services.organization.model.Organization;
import pl.piomin.services.organization.repository.OrganizationRepository;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Collection;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
public class OrganizationController {

    @Inject
    OrganizationRepository repository;
    @Inject
    @RestClient
    DepartmentClient departmentClient;
    @Inject
    @RestClient
    EmployeeClient employeeClient;

    @POST
    public Organization add(@Valid Organization organization) {
        Log.infof("Organization add: %s", organization);
        return repository.add(organization);
    }

    @GET
    public Collection<Organization> findAll() {
        Log.info("Organization find");
        return repository.findAll();
    }

    @Path("/{id}")
    @GET
    public Organization findById(@PathParam("id") Long id) {
        Log.infof("Organization find: id=%d", id);
        return repository.findById(id);
    }

    @Path("/{id}/with-departments")
    @GET
    public Organization findByIdWithDepartments(@PathParam("id") Long id) {
        Log.infof("Organization find: id=%d", id);
        Organization organization = repository.findById(id);
        organization.setDepartments(departmentClient.findByOrganization(organization.getId()));
        return organization;
    }

    @Path("/{id}/with-departments-and-employees")
    @GET
    public Organization findByIdWithDepartmentsAndEmployees(@PathParam("id") Long id) {
        Log.infof("Organization find: id=%d", id);
        Organization organization = repository.findById(id);
        organization.setDepartments(departmentClient.findByOrganizationWithEmployees(organization.getId()));
        return organization;
    }

    @Path("/{id}/with-employees")
    @GET
    public Organization findByIdWithEmployees(@PathParam("id") Long id) {
        Log.infof("Organization find: id=%d", id);
        Organization organization = repository.findById(id);
        organization.setEmployees(employeeClient.findByOrganization(organization.getId()));
        return organization;
    }

}

package pl.piomin.services.employee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.employee.model.Employee;
import pl.piomin.services.employee.repository.EmployeeRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
	
	@Inject
	EmployeeRepository repository;
	
	@POST
	public Employee add(@Valid Employee employee) {
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
	public Set<Employee> findAll() {
		LOGGER.info("Employee find");
		return repository.findAll();
	}
	
	@Path("/department/{departmentId}")
    @GET
	public Set<Employee> findByDepartment(@PathParam("departmentId") Long departmentId) {
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

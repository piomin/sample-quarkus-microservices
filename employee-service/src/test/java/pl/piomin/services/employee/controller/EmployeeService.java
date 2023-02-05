package pl.piomin.services.employee.controller;

import pl.piomin.services.employee.model.Employee;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Set;

public interface EmployeeService {
    @POST
    Employee add(@Valid Employee employee);
    @GET
    Set<Employee> findAll();
    @Path("/{id}")
    @GET
    Employee findById(@PathParam("id") Long id);
}

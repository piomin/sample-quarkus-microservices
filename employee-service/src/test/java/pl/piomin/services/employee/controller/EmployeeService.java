package pl.piomin.services.employee.controller;

import pl.piomin.services.employee.model.Employee;

import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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

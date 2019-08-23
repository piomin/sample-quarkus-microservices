package pl.piomin.services.employee.repository;

import java.util.*;
import java.util.stream.Collectors;

import pl.piomin.services.employee.model.Employee;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeRepository {

    private Set<Employee> employees = new HashSet<>();

    public EmployeeRepository() {
        add(new Employee(1L, 1L, "John Smith", 30, "Developer"));
        add(new Employee(1L, 1L, "Paul Walker", 40, "Architect"));
    }

    public Employee add(Employee employee) {
        employee.setId((long) (employees.size() + 1));
        employees.add(employee);
        return employee;
    }

    public Employee findById(Long id) {
        Optional<Employee> employee = employees.stream().filter(a -> a.getId().equals(id)).findFirst();
        if (employee.isPresent())
            return employee.get();
        else
            return null;
    }

    public Set<Employee> findAll() {
        return employees;
    }

    public Set<Employee> findByDepartment(Long departmentId) {
        return employees.stream().filter(a -> a.getDepartmentId().equals(departmentId)).collect(Collectors.toSet());
    }

    public Set<Employee> findByOrganization(Long organizationId) {
        return employees.stream().filter(a -> a.getOrganizationId().equals(organizationId)).collect(Collectors.toSet());
    }

}

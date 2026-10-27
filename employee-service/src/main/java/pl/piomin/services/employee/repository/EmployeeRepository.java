package pl.piomin.services.employee.repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import pl.piomin.services.employee.model.Employee;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class EmployeeRepository {

    private final Map<Long, Employee> employees = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public EmployeeRepository() {
        add(new Employee(1L, 1L, "John Smith", 30, "Developer"));
        add(new Employee(1L, 1L, "Paul Walker", 40, "Architect"));
        add(new Employee(1L, 1L, "Monica Hamilton", 50, "Director"));
    }

    public Employee add(Employee employee) {
        employee.setId(sequence.incrementAndGet());
        employees.put(employee.getId(), employee);
        return employee;
    }

    public Employee findById(Long id) {
        Employee employee = employees.get(id);
        if (employee == null) {
            throw new NotFoundException("Employee not found: " + id);
        }
        return employee;
    }

    public Collection<Employee> findAll() {
        return employees.values();
    }

    public Collection<Employee> findByDepartment(Long departmentId) {
        return employees.values().stream()
                .filter(a -> a.getDepartmentId().equals(departmentId))
                .collect(Collectors.toList());
    }

    public Collection<Employee> findByOrganization(Long organizationId) {
        return employees.values().stream()
                .filter(a -> a.getOrganizationId().equals(organizationId))
                .collect(Collectors.toList());
    }

}

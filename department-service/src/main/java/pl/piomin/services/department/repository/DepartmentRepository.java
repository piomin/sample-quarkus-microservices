package pl.piomin.services.department.repository;

import pl.piomin.services.department.model.Department;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class DepartmentRepository {

    private final Set<Department> departments = new HashSet<>();

    public DepartmentRepository() {
        add(new Department(1L, "Test1"));
        add(new Department(1L, "Test2"));
        add(new Department(2L, "Test3"));
    }

    public Department add(Department department) {
        department.setId((long) (departments.size() + 1));
        departments.add(department);
        return department;
    }

    public Department findById(Long id) {
        return departments.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public Set<Department> findAll() {
        return departments;
    }

    public Set<Department> findByOrganization(Long organizationId) {
        return departments.stream().filter(a -> a.getOrganizationId().equals(organizationId)).collect(Collectors.toSet());
    }

}

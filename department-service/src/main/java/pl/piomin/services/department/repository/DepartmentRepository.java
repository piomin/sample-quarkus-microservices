package pl.piomin.services.department.repository;

import pl.piomin.services.department.model.Department;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class DepartmentRepository {

    private final Map<Long, Department> departments = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public DepartmentRepository() {
        add(new Department(1L, "Test1"));
        add(new Department(1L, "Test2"));
        add(new Department(2L, "Test3"));
    }

    public Department add(Department department) {
        department.setId(sequence.incrementAndGet());
        departments.put(department.getId(), department);
        return department;
    }

    public Department findById(Long id) {
        Department department = departments.get(id);
        if (department == null) {
            throw new NoSuchElementException("Department not found: " + id);
        }
        return department;
    }

    public Collection<Department> findAll() {
        return departments.values();
    }

    public Collection<Department> findByOrganization(Long organizationId) {
        return departments.values().stream()
                .filter(a -> a.getOrganizationId().equals(organizationId))
                .collect(Collectors.toList());
    }

}

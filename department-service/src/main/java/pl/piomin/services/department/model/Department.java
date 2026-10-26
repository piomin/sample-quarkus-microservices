package pl.piomin.services.department.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Department {

    private Long id;
    @NotNull
    private Long organizationId;
    @NotBlank
    private String name;
    // Populated server-side only (e.g. via the with-employees endpoint).
    // READ_ONLY access prevents clients from over-posting fabricated
    // employees on create/update requests, while still returning the
    // field on responses.
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Employee> employees = new ArrayList<>();

    public Department() {

    }

    public Department(Long organizationId, String name) {
        super();
        this.organizationId = organizationId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department department = (Department) o;
        return java.util.Objects.equals(id, department.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Department [id=" + id + ", organizationId=" + organizationId + ", name=" + name + "]";
    }

}

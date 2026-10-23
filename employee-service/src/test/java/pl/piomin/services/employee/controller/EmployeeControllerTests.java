package pl.piomin.services.employee.controller;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.piomin.services.employee.client.DepartmentClient;
import pl.piomin.services.employee.client.OrganizationClient;
import pl.piomin.services.employee.model.Department;
import pl.piomin.services.employee.model.Employee;
import pl.piomin.services.employee.model.Organization;
import pl.piomin.services.employee.repository.EmployeeRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class EmployeeControllerTests {

    @Inject
    EmployeeRepository repository;

    @InjectMock
    @RestClient
    DepartmentClient departmentClient;

    @InjectMock
    @RestClient
    OrganizationClient organizationClient;

    @BeforeEach
    public void setUp() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Test1");
        Mockito.when(departmentClient.findById(1L)).thenReturn(department);
        Mockito.when(departmentClient.findById(Mockito.argThat(id -> id != null && id != 1L)))
                .thenThrow(new WebApplicationException(404));

        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("TestOrg");
        Mockito.when(organizationClient.findById(1L)).thenReturn(organization);
        Mockito.when(organizationClient.findById(Mockito.argThat(id -> id != null && id != 1L)))
                .thenThrow(new WebApplicationException(404));
    }

    @Test
    public void testFindAll() {
        given().when().get("/employees").then().statusCode(200).body(notNullValue());
    }

    @Test
    public void testFindById() {
        Employee employee = new Employee(1L, 1L, "Adrien Hamilton", 33, "Developer");
        employee = repository.add(employee);
        given().when().get("/employees/{id}", employee.getId()).then().statusCode(200)
                .body("id", equalTo(employee.getId().intValue()))
                .body("name", equalTo(employee.getName()))
                .body("position", equalTo(employee.getPosition()));
    }

    @Test
    public void testFindByDepartment() {
        given().when().get("/employees/department/{departmentId}", 1L).then().statusCode(200).body(notNullValue());
    }

    @Test
    public void testAdd() {
        Employee employee = new Employee(1L, 1L, "Josh Stevens", 23, "Developer");
        given().contentType("application/json").body(employee)
                .when().post("/employees").then().statusCode(200)
                    .body("id", notNullValue())
                    .body("name", equalTo(employee.getName()))
                    .body("position", equalTo(employee.getPosition()));
    }

    @Test
    public void testInvalidAdd() {
        Employee employee = new Employee();
        given().contentType("application/json").body(employee).when().post("/employees").then().statusCode(400);
    }

    @Test
    public void testAddWithNonExistentDepartment() {
        Employee employee = new Employee(1L, 9999L, "Ghost Employee", 25, "Developer");
        given().contentType("application/json").body(employee)
                .when().post("/employees").then().statusCode(404);
    }

    @Test
    public void testAddWithNonExistentOrganization() {
        Employee employee = new Employee(9999L, 1L, "Ghost Employee", 25, "Developer");
        given().contentType("application/json").body(employee)
                .when().post("/employees").then().statusCode(404);
    }

}

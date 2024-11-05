package pl.piomin.services.department.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.instancio.Instancio;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import pl.piomin.services.department.client.EmployeeClient;
import pl.piomin.services.department.model.Department;
import pl.piomin.services.department.model.Employee;
import pl.piomin.services.department.repository.DepartmentRepository;

import jakarta.inject.Inject;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentControllerTests {

    @Inject
    DepartmentRepository repository;
    @InjectMock
    @RestClient
    EmployeeClient employeeClient;

    @Test
    public void testFindAll() {
        given().when().get("/departments").then().statusCode(200).body(notNullValue());
    }

    @Test
    public void testFindById() {
        Department department = new Department(1L, "Test4");
        department = repository.add(department);
        given().when().get("/departments/{id}", department.getId()).then().statusCode(200)
                .body("id", equalTo(department.getId().intValue()))
                .body("name", equalTo(department.getName()));
    }

    @Test
    public void testFindByOrganization() {
        when().get("/departments/organization/{organizationId}", 1L).then()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    @Order(1)
    public void testAdd() {
        Department department = new Department(2L, "Test5");
        given().contentType("application/json").body(department)
                .when().post("/departments").then().statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo(department.getName()));
    }

    @Test
    public void testInvalidAdd() {
        Department department = new Department(null, "Test5");
        given().contentType("application/json").body(department)
                .when().post("/departments").then()
                .statusCode(400);
    }

    @Test
    void findByOrganizationWithEmployees() {
        Mockito.when(employeeClient.findByDepartment(anyLong()))
                .thenReturn(Instancio.ofList(Employee.class).size(10).create());
        when().get("/departments/organization/{organizationId}/with-employees", 1L).then()
                .statusCode(200)
                .body("size()", notNullValue())
                .body("employees[0].size()", is(10));
    }

}

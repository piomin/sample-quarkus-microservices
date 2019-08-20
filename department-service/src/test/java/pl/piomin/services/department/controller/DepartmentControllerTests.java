package pl.piomin.services.department.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import pl.piomin.services.department.model.Department;
import pl.piomin.services.department.repository.DepartmentRepository;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class DepartmentControllerTests {

    @Inject
    DepartmentRepository repository;

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
        given().when().get("/departments/organization/{organizationId}", 1L).then().statusCode(200).body(notNullValue());
    }

    @Test
    public void testAdd() {
        Department department = new Department(2L, "Test5");
        given().contentType("application/json").body(department)
                .when().post("/departments").then().statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo(department.getName()));
    }

}

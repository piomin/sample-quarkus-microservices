package pl.piomin.services.department.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void testFindByIdNotFound() {
        given().when().get("/departments/{id}", 999999L).then().statusCode(404);
    }

    @Test
    public void testAddIgnoresClientSuppliedEmployees() {
        Department department = new Department(1L, "Test6");
        department.setEmployees(List.of(new Employee(999L, "fabricated", 30, "Fake Position")));

        Response response = given().contentType(ContentType.JSON).body(department)
                .when().post("/departments");

        response.then().statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo(department.getName()))
                .body("employees.size()", is(0));

        Long id = response.jsonPath().getLong("id");
        Department stored = repository.findById(id);
        assertEquals(0, stored.getEmployees().size(), "Server-populated employees must not be settable by clients");
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

    @Test
    public void testConcurrentAddProducesUniqueIds() throws InterruptedException {
        int threadCount = 10;
        List<Long> ids = new CopyOnWriteArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                Department department = new Department(1L, "Concurrent Dept " + index);
                Response response = given().contentType(ContentType.JSON).body(department)
                        .when().post("/departments");
                long id = response.jsonPath().getLong("id");
                ids.add(id);
            });
        }
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        assertEquals(threadCount, ids.size(), "All requests should have completed");
        Set<Long> uniqueIds = ids.stream().collect(Collectors.toSet());
        assertEquals(threadCount, uniqueIds.size(), "All assigned IDs must be unique");
    }

}

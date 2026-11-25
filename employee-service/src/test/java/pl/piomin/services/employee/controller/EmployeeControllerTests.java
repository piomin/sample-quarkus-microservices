package pl.piomin.services.employee.controller;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void testFindByIdNotFound() {
        given().when().get("/employees/{id}", 999999L).then().statusCode(404);
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
    public void testConcurrentAddProducesUniqueIds() throws InterruptedException {
        int threadCount = 10;
        List<Long> ids = new CopyOnWriteArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                Employee employee = new Employee(1L, 1L, "Concurrent User " + index, 25, "Tester");
                Response response = given().contentType(ContentType.JSON).body(employee)
                        .when().post("/employees");
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

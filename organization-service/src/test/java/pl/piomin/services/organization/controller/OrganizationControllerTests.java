package pl.piomin.services.organization.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import pl.piomin.services.organization.model.Organization;
import pl.piomin.services.organization.repository.OrganizationRepository;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class OrganizationControllerTests {

    @Inject
    OrganizationRepository repository;

    @Test
    public void testFindAll() {
        given().when().get("/organizations").then().statusCode(200).body(notNullValue());
    }

    @Test
    public void testFindById() {
        Organization organization = new Organization("Test3", "Address3");
        organization = repository.add(organization);
        given().when().get("/organizations/{id}", organization.getId()).then().statusCode(200)
                .body("id", equalTo(organization.getId().intValue()))
                .body("name", equalTo(organization.getName()));
    }

    @Test
    public void testFindByIdWithDepartments() {
        given().when().get("/organizations/{id}/with-departments", 1L).then().statusCode(200)
                .body(notNullValue())
                .body("departments.size()", is(1));
    }

    @Test
    public void testAdd() {
        Organization organization = new Organization("Test5", "Address5");
        given().contentType("application/json").body(organization)
                .when().post("/organizations").then().statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo(organization.getName()));
    }

    @Test
    public void testInvalidAdd() {
        Organization organization = new Organization();
        given().contentType("application/json").body(organization).when().post("/organizations").then().statusCode(400);
    }

    @Test
    public void testConcurrentAddProducesUniqueIds() throws InterruptedException {
        int threadCount = 10;
        List<Long> ids = new CopyOnWriteArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                Organization org = new Organization("Concurrent Org " + index, "Address " + index);
                Response response = given().contentType(ContentType.JSON).body(org)
                        .when().post("/organizations");
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

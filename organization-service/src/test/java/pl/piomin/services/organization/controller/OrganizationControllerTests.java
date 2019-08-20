package pl.piomin.services.organization.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import pl.piomin.services.organization.model.Organization;
import pl.piomin.services.organization.repository.OrganizationRepository;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;

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

}

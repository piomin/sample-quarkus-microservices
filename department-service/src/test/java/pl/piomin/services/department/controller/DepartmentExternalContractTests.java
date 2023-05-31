package pl.piomin.services.department.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class DepartmentExternalContractTests {

    @Test
    void findByOrganizationWithEmployees() {
        when().get("/departments/organization/{organizationId}/with-employees", 1L).then()
                .statusCode(200)
                .body("size()", notNullValue());
    }

}

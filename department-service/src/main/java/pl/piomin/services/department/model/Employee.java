package pl.piomin.services.department.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Employee(Long id, Long departmentId, String name, int age, String position) {

}

package pl.piomin.services.organization.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Department(Long id, String name, List<Employee> employees) {

}

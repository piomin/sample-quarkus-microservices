package pl.piomin.services.organization.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Employee(Long id, String name, int age, String position) {

}

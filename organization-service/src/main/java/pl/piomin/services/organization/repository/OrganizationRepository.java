package pl.piomin.services.organization.repository;

import java.util.*;

import pl.piomin.services.organization.model.Organization;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrganizationRepository {

	private final Set<Organization> organizations = new HashSet<>();

	public OrganizationRepository() {
		add(new Organization("Test1", "Address1"));
		add(new Organization("Test2", "Address2"));
	}

	public Organization add(Organization organization) {
		organization.setId((long) (organizations.size()+1));
		organizations.add(organization);
		return organization;
	}
	
	public Organization findById(Long id) {
		return organizations.stream()
				.filter(a -> a.getId().equals(id))
				.findFirst()
				.orElseThrow();
	}

	public Set<Organization> findAll() {
		return organizations;
	}
	
}

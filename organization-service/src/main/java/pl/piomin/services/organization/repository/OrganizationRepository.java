package pl.piomin.services.organization.repository;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import pl.piomin.services.organization.model.Organization;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrganizationRepository {

	private final Map<Long, Organization> organizations = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong();

	public OrganizationRepository() {
		add(new Organization("Test1", "Address1"));
		add(new Organization("Test2", "Address2"));
	}

	public Organization add(Organization organization) {
		organization.setId(sequence.incrementAndGet());
		organizations.put(organization.getId(), organization);
		return organization;
	}

	public Organization findById(Long id) {
		Organization organization = organizations.get(id);
		if (organization == null) {
			throw new NoSuchElementException("Organization not found: " + id);
		}
		return organization;
	}

	public Collection<Organization> findAll() {
		return organizations.values();
	}

}

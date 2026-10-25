package pl.piomin.services.organization.repository;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import pl.piomin.services.organization.model.Organization;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

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
		return organizations.stream()
				.filter(a -> a.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new NotFoundException("Organization not found: " + id));
	}

	public Collection<Organization> findAll() {
		return organizations.values();
	}

}

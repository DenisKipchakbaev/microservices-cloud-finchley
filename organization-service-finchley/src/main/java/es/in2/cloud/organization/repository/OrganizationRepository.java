package es.in2.cloud.organization.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.in2.cloud.organization.model.Organization;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, String>  {
    public Optional<Organization> findById(String organizationId);
}

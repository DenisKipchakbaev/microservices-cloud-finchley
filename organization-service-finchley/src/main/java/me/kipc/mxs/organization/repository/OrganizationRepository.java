package me.kipc.mxs.organization.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.kipc.mxs.organization.model.Organization;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, String>  {
    public Optional<Organization> findById(String organizationId);
}

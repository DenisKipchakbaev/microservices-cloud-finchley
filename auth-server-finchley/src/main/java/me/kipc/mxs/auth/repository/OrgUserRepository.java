package me.kipc.mxs.auth.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.kipc.mxs.auth.model.UserOrganization;

@Repository
public interface OrgUserRepository extends CrudRepository<UserOrganization,String>  {
    public UserOrganization findByUserName(String userName);
}

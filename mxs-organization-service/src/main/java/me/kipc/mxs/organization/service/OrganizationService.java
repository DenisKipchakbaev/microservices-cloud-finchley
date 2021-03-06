package me.kipc.mxs.organization.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import me.kipc.mxs.organization.events.source.SimpleSourceBean;
import me.kipc.mxs.organization.model.Organization;
import me.kipc.mxs.organization.repository.OrganizationRepository;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepository;

    @Autowired
    SimpleSourceBean simpleSourceBean;

    @HystrixCommand
    public Organization getOrg(String organizationId) {
        return orgRepository.findById(organizationId).orElse(new Organization());
    }

    public void saveOrg(Organization org){
        org.setId( UUID.randomUUID().toString());

        orgRepository.save(org);
        simpleSourceBean.publishOrgChange("SAVE", org.getId());
    }

    public void updateOrg(Organization org){
        orgRepository.save(org);
        simpleSourceBean.publishOrgChange("UPDATE", org.getId());

    }

    public void deleteOrg(Organization org){
        orgRepository.deleteById(org.getId());
        simpleSourceBean.publishOrgChange("DELETE", org.getId());
    }
}

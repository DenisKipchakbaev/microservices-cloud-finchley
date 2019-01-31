package me.kipc.mxs.licensing.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.kipc.mxs.licensing.model.Organization;

@FeignClient("organizationservice")
public interface OrganizationFeignClient {
    @RequestMapping(
            method= RequestMethod.GET,
            value="/v1/organizations/{organizationId}",
            consumes="application/json")
    Organization getOrganization(
    		@RequestHeader("Authorization") String bearerToken,
    		@PathVariable("organizationId") String organizationId);
}

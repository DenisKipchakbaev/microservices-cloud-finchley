package me.kipc.mxs.licensing.client;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import me.kipc.mxs.licensing.model.Organization;

@Component
public class OrganizationDiscoveryClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    
// The autowired RestTemplate is already connected to Ribbon. 
// Do lookup by hand using DiscoveryClient instead (For demonstration purposes) 
    
//	@Autowired
//    OAuth2RestOperations restTemplate;

    public Organization getOrganization(String organizationId) {
        
    	RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("organizationservice");
        
        if (instances.size()==0) return null;
        
        instances.forEach(i -> System.out.format("discovered serviceId: %s%n", i.getUri().toString()));
                
//        String serviceUrl = String.format("%s%s/v1/organizations/%s", 
//        		instances.get(0).isSecure() ? "https://" : "http://",
//        		instances.get(0).getServiceId(), 
//        		organizationId);
        
        String serviceUrl = String.format("%s/v1/organizations/%s", 
        		instances.get(0).getUri(), 
        		organizationId);
        
        System.out.format("using service URL: %s%n", serviceUrl);
        
        
        //pass authorization token to REST call
        
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
        		RequestContextHolder
        			.getRequestAttributes()).getRequest();
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", httpServletRequest.getHeader("authorization"));
        
        HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", headers);
        
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        serviceUrl,
                        HttpMethod.GET,
                        requestEntity, Organization.class, organizationId);

        return restExchange.getBody();
    }
}

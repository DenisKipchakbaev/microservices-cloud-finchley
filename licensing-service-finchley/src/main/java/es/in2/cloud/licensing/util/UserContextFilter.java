package es.in2.cloud.licensing.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class UserContextFilter implements Filter {
	
	private static final Logger log = LoggerFactory.getLogger(UserContextFilter.class);
	
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {


        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        
        log.debug("servletRequest Header Names:");
        Enumeration<String> headers = httpServletRequest.getHeaderNames();
        if(headers.hasMoreElements()) {
        	String headerName = headers.nextElement();
        	String headerValue = httpServletRequest.getHeader(headerName);
        	log.debug("{}:{}", headerName, headerValue);
        }
        
        Enumeration<String> attributes = httpServletRequest.getAttributeNames();
        log.debug("servletRequest Attribute Names:");
        if(attributes.hasMoreElements()) {
        	String attrName = attributes.nextElement();
        	Object attrValue = httpServletRequest.getAttribute(attrName);
        	log.debug("{}:{}", attrName, attrValue.toString());
        }
        
        log.debug("servletRequest Parameter Map:");
        httpServletRequest.getParameterMap().forEach((k, v) -> log.debug("{}:{}", k, v));

//        log.debug("Correlation_id: {}", httpServletRequest.getHeader(UserContext.CORRELATION_ID));
//        UserContext.setCorrelationId(  httpServletRequest.getHeader(UserContext.CORRELATION_ID) );
//        
//        log.debug("User_id: {}", httpServletRequest.getHeader(UserContext.USER_ID));
//        UserContext.setUserId( httpServletRequest.getHeader(UserContext.USER_ID) );
//        
//        log.debug("Auth_token: {}", httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
//        UserContext.setAuthToken( httpServletRequest.getHeader(UserContext.AUTH_TOKEN) );
//        
//        log.debug("Org_id: {}", httpServletRequest.getHeader(UserContext.ORG_ID));
//        UserContext.setOrgId( httpServletRequest.getHeader(UserContext.ORG_ID) );

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
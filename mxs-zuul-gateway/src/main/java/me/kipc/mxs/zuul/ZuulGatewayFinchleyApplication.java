package me.kipc.mxs.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
//@Configuration
@EnableZuulProxy
@EnableDiscoveryClient
public class ZuulGatewayFinchleyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulGatewayFinchleyApplication.class, args);
	}
}

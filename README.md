Microservices on Spring Cloud 2.0 sample project.

Based on Spring Cloud Netflix stack.

As a base was taken the examples of the Microservices in Action book by John Carnell: 
https://github.com/carnellj/spmia_overview
Migrated to Spring Cloud Finchley release (2.0.x) and enhanced with additional monitoring and management modules.

Project modules configured to run as docker containers as well as separate Spring Boot applications.

# Introduction
This example project includes:

Architectural nodes:

1.  **Spring Cloud Configuration Server** that is deployed as Docker container and can manage a services configuration information using a file system or GitHub-based repository.
2.  **Eureka** Server running as a Spring-Cloud based service.  This service will allow multiple service instances to register with it.  Clients that need to call a service will use Eureka to lookup the physical location of the target service.
3.  **Zuul API Gateway**.  All of our microservices can be routed through the gateway and have pre, response and post policies enforced on the calls.
4.  Spring Cloud based **OAuth2 Authentication Server** that can issue and validate OAuth2 tokens.
5.  **Kafka Message Bus** to transport messages between services.
6.  **Zookeeper** Server utilized by Apache Kafka. ZooKeeper is a centralized service for maintaining configuration information, naming, providing distributed synchronization, and providing group services.
7.  **PostgreSQL** database used to hold the business data.
8.  **Redis** service to act as a distributed cache.


Monitoring and management nodes:

9.  **Zipkin** distributed tracing platform.
10. **logspout** module to aggregate services logs and redirect them to Papertrail service.
11. **Adminer** tool to manage database schema.
12. **Portainer** management tool. Portainer is an open-source lightweight management UI which allows you to easily manage your Docker hosts or Swarm clusters.
13. **Turbine Stream** server used to aggreagate hystrix streams of the business services to provide joint information in Hystrix Dashboard.
14. **Hystrix Dashboard**. Monitoring of Hystrix metrics in real time
15. **Spring Boot Admin**. UI tool for monitoring and managing Spring Boot microservices registered with Eureka.


Custom sample business modules:

16.  A licensing service that will manage licensing data used within a company.
17.  An organization service that will manage client organization data used within a company.
18.  [TODO] A new version of the organization service.  This service is used to demonstrate how to use the Zuul API gateway to route to different versions of a service.
19.  [TODO] A special routes services that the the API gateway will call to determine whether or not it should route a user's service call to a different service then the one they were originally calling.  This service is used in conjunction with the orgservice-new service to determine whether a call to the organization service gets routed to an old version of the organization service vs. a new version of the service.


# Software requirements
0.  [Linux Host]
1.  [Java 8]
2.	[Apache Maven] (http://maven.apache.org).
3.	[Docker] (http://docker.com).
4.	[Git Client] (http://git-scm.com).

# Configuration
Configuration server module uses Git repo to pull configuration parameters of service modules it is responsible for. Git configuration repo is: 
https://github.com/DenisKipchakbaev/cloud-finchley-config
Fork it for your own projects.

# Building the Docker Images
To build the code examples as a docker image, open a command-line window, change to the directory where you have downloaded the source code.

Run the following maven command.  This command will execute the compiling and packaging of the Spring Boot powered modules as well as create docker images harnessing [Spotify Dockerfile maven plugin] (https://github.com/spotify/dockerfile-maven) defined in the pom.xml file.  
   
   **mvn clean package**

If everything builds successfully you should see a message indicating that the build was successful.

# Running the services in Docker

Now we are going to use docker-compose to start the actual image.  To start the docker image,
change to the directory containing  your source code. 
Issue the following docker-compose command:

   **export BUILD_NAME=0.0.1 && docker-compose -f docker/common/docker-compose.yml up**

If everything starts correctly you should see a bunch of Spring Boot information fly by on standard out.  At this point all of the services will be running.

# Running the services as Spring Boot applications

To run in STS:
- Import as Maven projects

- To work with encrypted passwords Oracle JCE java encryption libs required.


note: OpenJDK comes with preinstalled JCE libs
A quick test to see if you have the JCE Unlimited Strength Jurisdiction Policy files installed: 

  **$JAVA_HOME/bin/jrunscript -e 'print (javax.crypto.Cipher.getMaxAllowedKeyLength("RC5") >= 256);'**

Installing JCE libs for oracle JDK (adjust for your JDK dir):

cd /tmp/ && \
curl -k -LO "http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip" -H 'Cookie: oraclelicense=accept-securebackup-cookie' && \
unzip jce_policy-8.zip && \
rm jce_policy-8.zip && \
yes | sudo cp -v /tmp/UnlimitedJCEPolicyJDK8/*.jar /usr/lib/jvm/oracle-jdk-8/jre/lib/security/


- In 'Run Configurations' of Spring Boot apps: 
	auth-server 
	licensing-service
	organisation-service
	spring-boot-admin
	eureka-server

set profile: local (to use *-local.yml file from config git repo)

- In 'Run Configurations' of Spring Boot apps: 
	auth-server 
	licensing-service
	organisation-service
	
set Environment variables:
ENCRYPT_KEY=IMSYMMETRIC
(The key used to encrypt passwords. All services with encrypted properties in config require ENCRYPT_KEY environment variable)


- Start postgres, zookeeper, kafka, redis, and other auxiliary modules as docker containers:

   **docker-compose -f docker/local/docker-compose.yml up**
   
- Start Spring Boot apps, starting from eureka and config-server.

# Postman

There is a collection of API calls in the directory /postman for Postman app

# Push to Docker Hub

To push to docker hub should specify goal dockerfile:push and dockerfile.username, dockerfile.password parameters in maven build properties. More info https://github.com/spotify/dockerfile-maven#authenticating-with-maven-pomxml

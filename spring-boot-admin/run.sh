#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "Waiting for the eureka server to start on port $(getPort $EUREKASERVER_PORT)"
echo "********************************************************"
while ! `nc -z eurekaserver  $(getPort $EUREKASERVER_PORT)`; do sleep 3; done
echo "******* Eureka Server has started"

echo "Starting Spring Boot Admin Server on port $SERVER_PORT  "
echo "Using profile: $PROFILE				      "

java -Djava.security.egd=file:/dev/./urandom 				  \
     -Dserver.port=$SERVER_PORT   					  \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI             \
     -Dspring.profiles.active=$PROFILE                                    \
     -jar /usr/local/@project.name@/@project.build.finalName@.jar

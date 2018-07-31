#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "Waiting for the eureka server to start on port $(getPort $EUREKASERVER_PORT)"
while ! `nc -z eurekaserver  $(getPort $EUREKASERVER_PORT)`; do sleep 3; done
echo "******* Eureka Server has started"

echo "Waiting for the configuration server to start on port $(getPort $CONFIGSERVER_PORT)"
while ! `nc -z configserver $(getPort $CONFIGSERVER_PORT)`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "Starting Hystrix Dashboard"

java -Djava.security.egd=file:/dev/./urandom			  \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI     \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                  \
     -jar /usr/local/@project.name@/@project.build.finalName@.jar

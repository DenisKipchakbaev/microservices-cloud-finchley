#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "********************************************************"
echo "Waiting for the eureka server to start on port $(getPort $EUREKASERVER_PORT)"
echo "********************************************************"
while ! `nc -z eurekaserver  $(getPort $EUREKASERVER_PORT)`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Starting Configuration Service with Eureka Endpoint:  $EUREKASERVER_URI";
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom 							\
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI 					\
     -Dhttp.proxyHost=@http.proxy.host@ -Dhttp.proxyPort=@http.proxy.port@			\
     -Dhttps.proxyHost=@https.proxy.host@ -Dhttps.proxyPort=@https.proxy.port@ 			\
     -Dhttp.proxyUser=@http.proxy.username@ -Dhttp.proxyPassword=@http.proxy.password@		\
     -Dhttps.proxyUser=@https.proxy.username@ -Dhttps.proxyPassword=@https.proxy.password@	\
     -Dhttps.proxySet=true -Dhttp.proxySet=true							\
     -jar /usr/local/@project.name@/@project.build.finalName@.jar

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
echo "Waiting for the configuration server to start on port $(getPort $CONFIGSERVER_PORT)"
echo "********************************************************"
while ! `nc -z configserver $(getPort $CONFIGSERVER_PORT)`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Waiting for the kafka server to start on port  $(getPort $KAFKASERVER_PORT)"
echo "********************************************************"
while ! `nc -z kafka $(getPort $KAFKASERVER_PORT)`; do sleep 10; done
echo "******* Kafka Server has started"

#echo "********************************************************"
#echo "Waiting for the ZIPKIN server to start  on port $ZIPKIN_PORT"
#echo "********************************************************"
#while ! `nc -z zipkin $ZIPKIN_PORT`; do sleep 10; done
#echo "******* ZIPKIN has started"

echo "********************************************************"
echo "Using profile: $PROFILE				      "
echo "Starting License Server on port $SERVER_PORT 	      "
echo "Using Configuration server $CONFIGSERVER_URI	      "
echo "Using Eureka :  $EUREKASERVER_URI			      "
echo "Using Kafka Server: $KAFKASERVER_URI:$KAFKASERVER_PORT  "
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom                              \
     -Dserver.port=$SERVER_PORT   					  \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI             \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                          \
     -Dspring.cloud.stream.kafka.binder.brokers=$KAFKASERVER_URI          \
     -Dspring.zipkin.baseUrl=$ZIPKIN_URI                                  \
     -Dsecurity.oauth2.resource.userInfoUri=$AUTHSERVER_URI               \
     -Dspring.profiles.active=$PROFILE 					  \
     -jar /usr/local/@project.name@/@project.build.finalName@.jar

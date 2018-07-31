#!/bin/sh
echo "Starting the Eureka Server"
java -Djava.security.egd=file:/dev/./urandom			  \
     -Dserver.port=$SERVER_PORT   				  \
     -jar /usr/local/@project.name@/@project.build.finalName@.jar

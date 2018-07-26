FROM openjdk:8-jdk-alpine
ARG DEVELOPER_NAME
ARG JAR_FILE
ARG PROJECT_NAME
LABEL maintainer="${DEVELOPER_NAME}"
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/${PROJECT_NAME}
ADD target/${JAR_FILE} /usr/local/${PROJECT_NAME}/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh

FROM openjdk:11
#VOLUME /opt/data
COPY target/*.jar /srv/actuator.jar
EXPOSE 8769
WORKDIR /srv
ENTRYPOINT ["java","-jar","actuator.jar","--spring.profiles.active=docker"]
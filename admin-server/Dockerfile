FROM openjdk:11
#VOLUME /opt/data
COPY target/*.jar /opt/jar/app.jar
EXPOSE 8769
WORKDIR /opt/data
ENTRYPOINT ["java","-jar","../jar/app.jar","--spring.profiles.active=docker"]
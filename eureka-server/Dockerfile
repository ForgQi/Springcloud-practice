FROM openjdk:11
#VOLUME /opt/data
#ARG PROFILE
COPY target/*.jar /opt/jar/app.jar
EXPOSE 8761
WORKDIR /opt/data
ENTRYPOINT ["java","-jar","../jar/app.jar","--spring.profiles.active=peer2"]
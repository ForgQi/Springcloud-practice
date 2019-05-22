FROM openjdk:11
VOLUME /opt/data
COPY target/*.jar /opt/jar/app.jar
EXPOSE 80
WORKDIR /opt/data
ENTRYPOINT ["java","-jar","../jar/app.jar"]
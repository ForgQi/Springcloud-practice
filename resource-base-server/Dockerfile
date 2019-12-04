FROM openjdk:11
#RUN apk add --no-cache tzdata
#设置这个环境变量需要有tzdata，如果没有需要安装
ENV TZ=Asia/Shanghai
#RUN adduser lzu
VOLUME /opt/data/static/upload
COPY target/*.jar /opt/jar/app.jar
EXPOSE 8080
WORKDIR /opt/data
#USER lzu
ENTRYPOINT ["java","-jar","../jar/app.jar","--spring.profiles.active=docker"]
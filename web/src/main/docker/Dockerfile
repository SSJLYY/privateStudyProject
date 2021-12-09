FROM openjdk:8-jdk-alpine
MAINTAINER dcy "1540770111@qq.com"
COPY target/web.jar /app.jar
RUN sh -c 'touch /app.jar'
VOLUME /tmp
EXPOSE 8999

# 修改spring环境变量
ENV JAVA_OPTS=""
#ENV JAVA_OPTS="-Dspring.profiles.active=test"

# flowable要求字体文件不存在，需要增加配置
RUN apk add --update ttf-dejavu fontconfig

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
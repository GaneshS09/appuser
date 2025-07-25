FROM openjdk:24
EXPOSE 8090
ADD build/libs/docker_app.jar docker_appuser.jar
ENTRYPOINT ["java","-jar","/docker_appuser.jar"]
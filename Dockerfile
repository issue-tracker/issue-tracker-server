FROM openjdk:11-jdk
COPY ./build/libs/issue-tracker.jar issue-tracker.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","/issue-tracker.jar"]

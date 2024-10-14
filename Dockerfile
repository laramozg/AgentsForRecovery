FROM amazoncorretto:17-alpine

WORKDIR /app

COPY ./target/sports.jar /app/sports.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "sports.jar"]
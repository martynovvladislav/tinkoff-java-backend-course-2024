FROM openjdk:21
COPY ./target/bot.jar /app/bot.jar
WORKDIR app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bot.jar"]

FROM openjdk:17-jdk-slim
LABEL authors="amirrahmani"
COPY ./target/user-transaction-management-system-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app
EXPOSE 8083
ENTRYPOINT ["java","-jar","app.jar"]
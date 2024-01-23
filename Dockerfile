FROM eclipse-temurin:17

LABEL mentainer="fedispato@gmail.com"

WORKDIR /app

COPY target/financas-0.0.1-SNAPSHOT.jar /app/financas.jar

ENTRYPOINT ["java", "-jar", "financas.jar"]
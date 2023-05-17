FROM openjdk:18-slim-buster

COPY target/candlestick-service-0.0.1-SNAPSHOT.jar app.jar
#ENV JAVA_OPTS = ""
#ENTRYPOINT java ${JAVA_OPTS} -XshowSettings:vm -jar /app.jar
ENTRYPOINT ["java","-Xmx128m","-XshowSettings:vm","-jar","/app.jar"]
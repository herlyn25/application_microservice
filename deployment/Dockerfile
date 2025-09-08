FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY ../applications/app-service/build/libs/*.jar applicationservices.jar
ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
# Replace with a non-root user to avoid running the container with excessive privileges
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
RUN chown appuser:appgroup applicationservices.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar applicationservices.jar" ]

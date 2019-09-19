FROM maven:3.6-jdk-11-slim as compile

WORKDIR /app
COPY src/ /app/src
COPY media/ /app/media
COPY music/ /app/music
COPY fonts/ /app/fonts
COPY pom.xml /app
RUN ls
RUN ls /app/src
RUN mvn install

FROM openjdk:12-alpine

# Java App
COPY media/ /opt/app/media
COPY music/ /opt/app/music
COPY fonts/ /opt/app/fonts
COPY --from=compile /app/target/app.jar /opt/app

# FFMpeg
RUN sed -i -e 's/v[[:digit:]]\.[[:digit:]]/edge/g' /etc/apk/repositories && apk upgrade --update-cache --available 
RUN apk add --no-cache ffmpeg

# Font BS
RUN apk add --no-cache ttf-dejavu

# Cleanup
RUN rm -rf /var/cache/apk/* \
      && apk del --no-cache make gcc g++ python binutils-gold gnupg libstdc++ \
      && rm -rf /usr/include \
      && rm -rf /var/cache/apk/* /root/.node-gyp /usr/share/man /tmp/*

# Startup
WORKDIR /opt/app
CMD ["java", "-Djava.awt.headless=true", "-jar", "/opt/app/app.jar", "genup"]

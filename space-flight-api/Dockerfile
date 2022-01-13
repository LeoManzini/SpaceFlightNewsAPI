FROM openjdk:11

WORKDIR /apps/artifacts/api

COPY target/SpaceFlightNews.jar /apps/artifacts/api/space-flights-api.jar

ENV APP_ENV=local \
    APP_DEBUG=false \
    APP_KEY=key \
    APP_TIMEZONE=UTC \
    DB_CONNECTION=pgsql \
    DB_HOST=ec2-44-199-52-133.compute-1.amazonaws.com \
    DB_PORT=5432 \
    DB_DATABASE=d3hg5iitdr6m08 \
    DB_USERNAME=dlprzhhxbdbzfm \
    DB_PASSWORD=f3526042262669b897885216eee918b009211db2325e47c71441e3631a13dabe

ENTRYPOINT ["java", "-jar", "space-flights-api.jar"]

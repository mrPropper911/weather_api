CREATE SEQUENCE locations_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE weathers_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS locations
(
    id   BIGINT    NOT NULL,
    lon  FLOAT(53) NOT NULL,
    lat  FLOAT(53) NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS weathers
(
    id                  BIGINT NOT NULL,
    location_id         BIGINT,
    temp                FLOAT(53),
    wind_speed          FLOAT(53),
    pressure            FLOAT(53),
    humidity            FLOAT(53),
    weather_description VARCHAR(255),
    time                DATE,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS weathers
    add constraint fk_weather FOREIGN KEY (location_id) REFERENCES locations;





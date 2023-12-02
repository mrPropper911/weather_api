package by.belyahovich.demo.controller;

import by.belyahovich.demo.domain.Location;
import by.belyahovich.demo.domain.Weather;
import by.belyahovich.demo.repository.LocationRepositoryJpa;
import by.belyahovich.demo.repository.WeatherRepositoryJpa;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("WeatherController integration test")
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

    @Container
    private final static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    private static final String WEATHER_DESCRIPTION_0 = "SUNNY";
    private static final String WEATHER_DESCRIPTION_1 = "RAIN";
    private static final String LOCATION_NAME_0 = "Minsk";
    private static final String DATE_0 = "2023-12-1";
    private static final String DATE_1 = "2023-12-2";
    private static final String DATE_2 = "2023-12-3";
    @Autowired
    private WeatherRepositoryJpa weatherRepositoryJpa;
    @Autowired
    private LocationRepositoryJpa locationRepositoryJpa;
    @Autowired
    private MockMvc mvc;
    @Value("${apiUrl}")
    private String apiUrl;

    @BeforeEach
    void generateTestDataBase() {
        Location location = locationRepositoryJpa.save(Location.builder()
                .lat(anyDouble())
                .lon(anyDouble())
                .name(LOCATION_NAME_0)
                .build());
        weatherRepositoryJpa.saveAll(List.of(
                Weather.builder()
                        .temp(5.0)
                        .windSpeed(anyDouble())
                        .humidity(anyDouble())
                        .pressure(anyDouble())
                        .time((Date.valueOf(DATE_0)))
                        .weatherDescription(WEATHER_DESCRIPTION_0)
                        .location(location)
                        .build(),
                Weather.builder()
                        .temp(10.0)
                        .windSpeed(anyDouble())
                        .humidity(anyDouble())
                        .pressure(anyDouble())
                        .time((Date.valueOf(DATE_1)))
                        .weatherDescription(WEATHER_DESCRIPTION_0)
                        .location(location)
                        .build(),
                Weather.builder()
                        .temp(15.0)
                        .windSpeed(anyDouble())
                        .humidity(anyDouble())
                        .pressure(anyDouble())
                        .time((Date.valueOf(DATE_2)))
                        .weatherDescription(WEATHER_DESCRIPTION_1)
                        .location(location)
                        .build()
        ));
    }

    @AfterEach
    void clearDB() {
        weatherRepositoryJpa.deleteAll();
        locationRepositoryJpa.deleteAll();
    }

    @Test
    void getLastWeatherFromDBByLocationName_shouldCorrectlyReturnNewestWeather() throws Exception {
        assertThat(weatherRepositoryJpa.findAll().size()).isEqualTo(3);
        String contentAsString = mvc.perform(get(apiUrl + "/weather?location=" + LOCATION_NAME_0))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isNotNull();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(contentAsString);
        assertThat(jsonNode.get("weatherDescription").asText()).isEqualTo(WEATHER_DESCRIPTION_1);
    }

    @Test
    void getAverageTempByLocationName_shouldCorrectlyReturnAVGTemp() throws Exception {
        double AVG_TEMP = 10.0;
        assertThat(weatherRepositoryJpa.findAll().size()).isEqualTo(3);
        String contentAsString = mvc.perform(get(apiUrl + "/weather/avg?location=" + LOCATION_NAME_0
                        + "&from=" + DATE_0 + "&to=" + DATE_2))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isNotNull();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(contentAsString);
        assertThat(jsonNode.get("avgTemp").asDouble()).isEqualTo(AVG_TEMP);
    }
}
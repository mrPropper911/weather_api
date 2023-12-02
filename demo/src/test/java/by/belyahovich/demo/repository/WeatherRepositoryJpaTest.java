package by.belyahovich.demo.repository;

import by.belyahovich.demo.domain.Location;
import by.belyahovich.demo.domain.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;

@DisplayName("WeatherRepositoryJpa unit-testing")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
class WeatherRepositoryJpaTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    private static final String WEATHER_DESCRIPTION_0 = "SUNNY";
    private static final String WEATHER_DESCRIPTION_1 = "SUNNY";
    private static final String LOCATION_NAME_0 = "Minsk";
    private static final String DATE_0 = "2023-12-1";
    private static final String DATE_1 = "2023-12-2";
    private static final String DATE_2 = "2023-12-3";
    @Autowired
    private WeatherRepositoryJpa weatherRepositoryJpa;
    @Autowired
    private LocationRepositoryJpa locationRepositoryJpa;

    @BeforeEach
    void clearDB() {
        weatherRepositoryJpa.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(weatherRepositoryJpa).isNotNull();
        assertThat(locationRepositoryJpa).isNotNull();
    }

    @Test
    public void save_shouldCorrectlySaveWeather() {
        assertThat(weatherRepositoryJpa.findAll()).isEmpty();
        Location actualLocation = locationRepositoryJpa.save(generateAnyLocation());
        Weather expected = generateAnyWeatherList(actualLocation).get(0);

        Weather actual = weatherRepositoryJpa.save(expected);

        assertThat(actual).isNotNull();
        List<Weather> weatherList = weatherRepositoryJpa.findAll();
        assertThat(weatherList.size()).isEqualTo(1);
        assertThat(weatherList.get(0).getWeatherDescription()).isEqualTo(WEATHER_DESCRIPTION_0);
    }

    @Test
    public void findFirstByLocationOrderByIdDesc_shouldCorrectlyReturnNewestWeather() {
        Location actualLocation = locationRepositoryJpa.save(generateAnyLocation());
        List<Weather> weathersList = generateAnyWeatherList(actualLocation);
        weatherRepositoryJpa.saveAll(weathersList);

        Weather actual = weatherRepositoryJpa.findFirstByLocationOrderByIdDesc(actualLocation).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getWeatherDescription()).isEqualTo(WEATHER_DESCRIPTION_1);
    }

    @Test
    public void findAllByLocationSorterByDate_shouldCorrectlyReturnWeather() {
        Location actualLocation = locationRepositoryJpa.save(generateAnyLocation());
        List<Weather> weathersList = generateAnyWeatherList(actualLocation);
        weatherRepositoryJpa.saveAll(weathersList);

        List<Weather> allByLocationSorterByDate = weatherRepositoryJpa.findAllByLocationSorterByDate(actualLocation.getName(),
                Date.valueOf(DATE_0), Date.valueOf(DATE_1));

        assertThat(allByLocationSorterByDate).isNotEmpty();
        assertThat(allByLocationSorterByDate.size()).isEqualTo(2);
        assertThat(allByLocationSorterByDate.get(0)).isEqualTo(weathersList.get(0));
    }

    private List<Weather> generateAnyWeatherList(Location location) {
        return List.of(
                Weather.builder()
                        .temp(anyDouble())
                        .windSpeed(anyDouble())
                        .humidity(anyDouble())
                        .pressure(anyDouble())
                        .time((Date.valueOf(DATE_0)))
                        .weatherDescription(WEATHER_DESCRIPTION_0)
                        .location(location)
                        .build(),
                Weather.builder()
                        .temp(anyDouble())
                        .windSpeed(anyDouble())
                        .humidity(anyDouble())
                        .pressure(anyDouble())
                        .time((Date.valueOf(DATE_1)))
                        .weatherDescription(WEATHER_DESCRIPTION_1)
                        .location(location)
                        .build(),
                Weather.builder()
                        .temp(anyDouble())
                        .windSpeed(anyDouble())
                        .humidity(anyDouble())
                        .pressure(anyDouble())
                        .time((Date.valueOf(DATE_2)))
                        .weatherDescription(WEATHER_DESCRIPTION_1)
                        .location(location)
                        .build()
        );
    }

    private Location generateAnyLocation() {
        return Location.builder()
                .lat(anyDouble())
                .lon(anyDouble())
                .name(LOCATION_NAME_0)
                .build();
    }

}
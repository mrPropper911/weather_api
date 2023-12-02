package by.belyahovich.demo.repository;

import by.belyahovich.demo.domain.Location;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;

@DisplayName("LocationRepositoryJpa unit-testing")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
class LocationRepositoryJpaTest {

    @Container
    private final static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @Autowired
    private LocationRepositoryJpa locationRepositoryJpa;

    @BeforeEach
    void clearDB() {
        locationRepositoryJpa.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(locationRepositoryJpa).isNotNull();
    }

    @Test
    public void save_shouldCorrectlySaveLocation() {
        assertThat(locationRepositoryJpa.findAll()).isEmpty();

        String LOCATION_NAME = "Minsk";
        Location expected = Location.builder()
                .lat(anyDouble())
                .lon(anyDouble())
                .name(LOCATION_NAME)
                .build();
        Location actual = locationRepositoryJpa.save(expected);

        assertThat(actual).isNotNull();
        List<Location> locationList = locationRepositoryJpa.findAll();
        assertThat(locationList.size()).isEqualTo(1);
        assertThat(locationList.get(0).getName()).isEqualTo(LOCATION_NAME);
    }
}
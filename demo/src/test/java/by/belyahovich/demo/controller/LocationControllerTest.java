package by.belyahovich.demo.controller;

import by.belyahovich.demo.domain.Location;
import by.belyahovich.demo.repository.LocationRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("LocationController integration test")
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTest {

    @Container
    private final static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @Autowired
    private LocationRepositoryJpa locationRepositoryJpa;
    @Autowired
    private MockMvc mvc;

    @Value("${apiUrl}")
    private String apiUrl;

    @BeforeEach
    void clearDB() {
        locationRepositoryJpa.deleteAll();
    }

    @Test
    void saveLocationByName_shouldCorrectlySaveLocationToDB() throws Exception {
        String LOCATION_NAME = "Brest";
        assertThat(locationRepositoryJpa.findAll()).isEmpty();
        MvcResult mvcResult = mvc.perform(post(apiUrl + "/location?name=" + LOCATION_NAME))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isNotNull();
        List<Location> locationList = locationRepositoryJpa.findAll();
        assertThat(locationList.size()).isEqualTo(1);
        assertThat(locationList.get(0).getName()).isEqualTo(LOCATION_NAME);
    }

    @Test
    void saveLocationByName_shouldReturnException() throws Exception {
        String LOCATION_NAME = "NOT_EXISTING_NAME";
        MvcResult mvcResult = mvc.perform(post(apiUrl + "/location?name=" + LOCATION_NAME))
                .andExpect(status().isNotFound())
                .andReturn();
        assertThat(locationRepositoryJpa.findAll()).isEmpty();
    }

}
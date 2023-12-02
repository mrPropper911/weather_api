package by.belyahovich.demo.service.impl;

import by.belyahovich.demo.domain.Location;
import by.belyahovich.demo.repository.LocationRepositoryJpa;
import by.belyahovich.demo.repository.WeatherRepositoryJpa;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@DisplayName("LocationServiceOpenWeather unit-test")
@ExtendWith(MockitoExtension.class)
class LocationServiceOpenWeatherTest {

    @Mock
    LocationRepositoryJpa locationRepositoryJpa;

    @Mock
    WeatherRepositoryJpa weatherRepositoryJpa;

    @InjectMocks
    LocationServiceOpenWeather locationServiceOpenWeather;

    @Mock
    private WebClient webClientMock;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    // TODO: 12/1/2023 https://www.arhohuttunen.com/spring-boot-webclient-mockwebserver/ has exception
    void shouldCreateOneLocation() throws SQLException {
        String LOCATION_NAME = "Minsk";
        Location[] minsklocation = new Location[]{
                Location.builder()
                        .lat(anyDouble())
                        .lon(anyDouble())
                        .name(LOCATION_NAME)
                        .build()};
        when(locationRepositoryJpa.findByName(anyString())).thenReturn(Optional.empty());

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(Location[].class)).thenReturn(Mono.just(minsklocation));
        when(locationRepositoryJpa.save(any())).thenReturn(minsklocation[0]);

        Location location = locationServiceOpenWeather.saveLocationByNameToDataBase("Minsk");
        Assertions.assertThat(location.getName()).isEqualTo(LOCATION_NAME);
    }

}
package by.belyahovich.demo.service.impl;

import by.belyahovich.demo.domain.Location;
import by.belyahovich.demo.repository.LocationRepositoryJpa;
import by.belyahovich.demo.service.LocationService;
import by.belyahovich.demo.service.WeatherService;
import by.belyahovich.demo.utils.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceOpenWeather implements LocationService {

    private final URI URI_GEOCODING_API = URI.create("http://api.openweathermap.org/geo/1.0");
    private final LocationRepositoryJpa locationRepositoryJpa;
    private final WeatherService weatherService;
    @Value("${openWeatherMapToken}")
    private String openWeatherToken;

    /**
     * <p> Use  <a href="http://api.openweathermap.org/geo/1.0">OpenWeatherMap api</a>
     * to find location latitude and longitude by location name and save to DB
     *
     * @param locationName name of any major city
     * @return {@link Location}
     * @see <a href="https://www.latlong.net">Similar website for conversion</a>
     */
    @Override
    public Location saveLocationByNameToDataBase(String locationName) {
        Optional<Location> locationRepositoryJpaByName = locationRepositoryJpa.findByName(locationName);
        if (locationRepositoryJpaByName.isPresent()) {
            throw new ReservationException("Location name: " + locationName + " already exist");
        }

        WebClient webClient = WebClient.builder()
                .baseUrl(String.valueOf(URI_GEOCODING_API))
                .build();

        Location[] location = webClient.get()
                .uri(String.join("", String.format("/direct?q=%s&appid=%s", locationName, openWeatherToken)))
                .retrieve()
                .bodyToMono(Location[].class)
                .block();

        if (location != null && location.length == 0) {
            throw new IllegalArgumentException("Location name: " + locationName + " not found");
        }

        //after adding a new location, the weather values need to be updated with the new location
        weatherService.scheduledUpdateWeatherByLocationSet();

        return locationRepositoryJpa.save(Objects.requireNonNull(location)[0]);
    }
}

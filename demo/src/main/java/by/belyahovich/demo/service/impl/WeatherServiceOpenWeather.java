package by.belyahovich.demo.service.impl;

import by.belyahovich.demo.domain.Location;
import by.belyahovich.demo.domain.Weather;
import by.belyahovich.demo.dto.WeatherAvgResponse;
import by.belyahovich.demo.dto.WeatherResponse;
import by.belyahovich.demo.repository.LocationRepositoryJpa;
import by.belyahovich.demo.repository.WeatherRepositoryJpa;
import by.belyahovich.demo.service.WeatherService;
import by.belyahovich.demo.utils.ReservationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherServiceOpenWeather implements WeatherService {

    private final LocationRepositoryJpa locationRepositoryJpa;
    private final WeatherRepositoryJpa weatherRepositoryJpa;
    private final String URI_OPEN_WEATHER_API = "https://api.openweathermap.org/data/2.5/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${openWeatherMapToken}")
    private String openWeatherToken;

    /**
     * According to the locations in the database, updates the weather according to schedule
     */
    @Scheduled(fixedDelayString = "${scheduledDelayOnMillisecond}")
    @Override
    public void scheduledUpdateWeatherByLocationSet() {
        List<Location> locationList = locationRepositoryJpa.findAll();

        Set<String> setUriRequest = locationList.stream()
                .map(location -> URI_OPEN_WEATHER_API + String.join("", String.format("weather?lat=%f&lon=%f&appid=%s&units=metric",
                        location.getLat(), location.getLon(), openWeatherToken)))
                .collect(Collectors.toSet());

        WebClient webClient = WebClient.builder().build();

        List<String> weatherListByLocation = Flux.fromIterable(setUriRequest)
                .log()
                .flatMap(s -> webClient.get().uri(s).retrieve().bodyToMono(String.class))
                .collectList()
                .block();

        List<Weather> collect = Objects.requireNonNull(weatherListByLocation).stream()
                .map(this::mapJsonToWeather)
                .toList();

        // TODO: 12/1/2023 think about how to remove the cycle
        for (int i = 0; i < locationList.size(); ++i) {
            Weather weather = collect.get(i);
            weather.setLocation(locationList.get(i));
            weatherRepositoryJpa.save(weather);
        }
    }

    @Override
    public WeatherResponse getLastWeatherByLocationName(String locationName) {
        Optional<Location> locationRepositoryJpaByName = locationRepositoryJpa.findByName(locationName);
        if (locationRepositoryJpaByName.isEmpty()) {
            throw new ReservationException("Location with name: " + locationName + " not exist");
        }

        Optional<Weather> lastWeatherByLocation =
                weatherRepositoryJpa.findFirstByLocationOrderByIdDesc(locationRepositoryJpaByName.get());

        return lastWeatherByLocation
                .map(WeatherResponse::mapWeatherToWeatherResponse)
                .orElseThrow(() -> new ReservationException("Not have weather data for location: " + locationName));
    }

    @Override
    public WeatherAvgResponse getAverageWeatherByLocationOrderByDate(String locationName, Date from, Date to) {
        Optional<Location> locationRepositoryJpaByName = locationRepositoryJpa.findByName(locationName);
        if (locationRepositoryJpaByName.isEmpty()) {
            throw new ReservationException("Location with name: " + locationName + " not exist");
        }
        List<Weather> allByLocation =
                weatherRepositoryJpa.findAllByLocationSorterByDate(locationName, from, to).stream().toList();

        double averageTemp = allByLocation.stream()
                .mapToDouble(Weather::getTemp)
                .average()
                .orElseThrow(() -> new ReservationException("Not have temperature data for location: " + locationName));

        return WeatherAvgResponse.builder()
                .avgTemp(averageTemp)
                .locationName(locationName)
                .build();
    }

    private Weather mapJsonToWeather(String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            return Weather.builder()
                    .temp(Double.parseDouble(jsonNode.get("main").get("temp").asText()))
                    .windSpeed(Double.parseDouble(jsonNode.get("wind").get("speed").asText()))
                    .pressure(Double.parseDouble(jsonNode.get("main").get("pressure").asText()))
                    .humidity(Double.parseDouble(jsonNode.get("main").get("humidity").asText()))
                    .weatherDescription(jsonNode.get("weather").get(0).get("description").asText())
                    .time(new Date(Calendar.getInstance().getTimeInMillis()))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
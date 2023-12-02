package by.belyahovich.demo.controller;

import by.belyahovich.demo.dto.WeatherAvgResponse;
import by.belyahovich.demo.dto.WeatherResponse;
import by.belyahovich.demo.service.WeatherService;
import by.belyahovich.demo.utils.AppError;
import by.belyahovich.demo.utils.ReservationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
@RequestMapping(value = "/api/v1/weather", produces = "application/json")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Get the latest weather data for a specified location
     *
     * @param location location(city or big village) name
     * @return {@link WeatherResponse}
     */
    @GetMapping
    public ResponseEntity<?> getLastWeatherFromDBByLocationName(@RequestParam String location) {
        try {
            WeatherResponse lastWeatherByLocationName =
                    weatherService.getLastWeatherByLocationName(location);
            return new ResponseEntity<>(lastWeatherByLocationName, HttpStatus.OK);
        } catch (ReservationException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Returns the average temperature over a specified period of time for a given location
     *
     * @param location location(city or big village) name
     * @param from     counting start {@link Date}
     * @param to       count end {@link Date}
     * @return {@link WeatherAvgResponse}
     */
    @GetMapping("/avg")
    public ResponseEntity<?> getAverageTempByLocationName(@RequestParam String location,
                                                          @RequestParam Date from,
                                                          @RequestParam Date to) {
        try {
            WeatherAvgResponse weatherAvgResponse =
                    weatherService.getAverageWeatherByLocationOrderByDate(location, from, to);
            return new ResponseEntity<>(weatherAvgResponse, HttpStatus.OK);
        } catch (ReservationException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }
}

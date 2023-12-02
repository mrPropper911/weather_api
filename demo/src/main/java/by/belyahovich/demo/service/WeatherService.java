package by.belyahovich.demo.service;

import by.belyahovich.demo.dto.WeatherAvgResponse;
import by.belyahovich.demo.dto.WeatherResponse;

import java.sql.Date;

public interface WeatherService {
    void scheduledUpdateWeatherByLocationSet();

    WeatherResponse getLastWeatherByLocationName(String locationName);

    WeatherAvgResponse getAverageWeatherByLocationOrderByDate(String locationName, Date from, Date to);

}

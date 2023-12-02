package by.belyahovich.demo.dto;

import by.belyahovich.demo.domain.Weather;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WeatherResponse {

    private double temp;

    private double windSpeed;

    private double pressure;

    private double humidity;

    private String weatherDescription;

    private String locationName;

    public static WeatherResponse mapWeatherToWeatherResponse(Weather weather) {
        return WeatherResponse.builder()
                .temp(weather.getTemp())
                .windSpeed(weather.getWindSpeed())
                .pressure(weather.getPressure())
                .humidity(weather.getHumidity())
                .weatherDescription(weather.getWeatherDescription())
                .locationName(weather.getLocation().getName())
                .build();
    }
}

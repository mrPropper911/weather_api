package by.belyahovich.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WeatherAvgResponse {

    private double avgTemp;

    private String locationName;
}

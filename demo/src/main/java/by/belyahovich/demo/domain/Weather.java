package by.belyahovich.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "weathers")
public class Weather implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @JsonProperty("temp")
    private double temp;

    @JsonProperty("speed")
    @Column(name = "wind_speed")
    private double windSpeed;

    @JsonProperty("pressure")
    private double pressure;

    @JsonProperty("humidity")
    private double humidity;

    @JsonProperty("description")
    @Column(name = "weather_description")
    private String weatherDescription;

    @JsonProperty("time")
    private Date time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", temp=" + temp +
                ", windSpeed=" + windSpeed +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", time=" + time +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;

        if (id != weather.id) return false;
        if (Double.compare(weather.temp, temp) != 0) return false;
        if (Double.compare(weather.windSpeed, windSpeed) != 0) return false;
        if (Double.compare(weather.pressure, pressure) != 0) return false;
        if (Double.compare(weather.humidity, humidity) != 0) return false;
        if (!weatherDescription.equals(weather.weatherDescription)) return false;
        if (!time.equals(weather.time)) return false;
        return Objects.equals(location, weather.location);
    }

    @Override
    public int hashCode() {
        int result;
        long temp1;
        result = (int) (id ^ (id >>> 32));
        temp1 = Double.doubleToLongBits(temp);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(windSpeed);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(pressure);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(humidity);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        result = 31 * result + weatherDescription.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}

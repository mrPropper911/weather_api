package by.belyahovich.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "locations")
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private double lon;

    private double lat;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    private Set<Weather> weather;

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", lon=" + lon +
                ", lat=" + lat +
                ", name='" + name + '\'' +
                ", weather=" + weather +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (id != location.id) return false;
        if (Double.compare(location.lon, lon) != 0) return false;
        if (Double.compare(location.lat, lat) != 0) return false;
        if (!name.equals(location.name)) return false;
        return Objects.equals(weather, location.weather);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + (weather != null ? weather.hashCode() : 0);
        return result;
    }
}

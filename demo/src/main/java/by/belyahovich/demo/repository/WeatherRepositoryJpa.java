package by.belyahovich.demo.repository;

import by.belyahovich.demo.domain.Location;
import by.belyahovich.demo.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepositoryJpa extends JpaRepository<Weather, Long> {

    @Query("SELECT w FROM Weather w WHERE w.location.name = :location AND w.time >= :from AND w.time <= :to")
    List<Weather> findAllByLocationSorterByDate(@Param("location") String location,
                                                @Param("from") Date from,
                                                @Param("to") Date to);

    Optional<Weather> findFirstByLocationOrderByIdDesc(Location location);

}

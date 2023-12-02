package by.belyahovich.demo.repository;

import by.belyahovich.demo.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepositoryJpa extends JpaRepository<Location, Long> {
    Optional<Location> findByName(String locationName);
}

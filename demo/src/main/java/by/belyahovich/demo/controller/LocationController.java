package by.belyahovich.demo.controller;

import by.belyahovich.demo.domain.Location;
import by.belyahovich.demo.service.LocationService;
import by.belyahovich.demo.utils.AppError;
import by.belyahovich.demo.utils.ReservationException;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/location", produces = "application/json")
public class LocationController {

    private final Logger log = Logger.getLogger(LocationController.class);

    private final LocationService locationService;

    /**
     * <p> Adds a location to the database, weather updates will occur based on this database
     *
     * @param name location(city or big village) name
     * @return {@link Location}
     */
    @PostMapping
    public ResponseEntity<?> saveLocationByName(@RequestParam String name) {
        try {
            Location saveLocationByNameToDataBase =
                    locationService.saveLocationByNameToDataBase(name);
            return new ResponseEntity<>(saveLocationByNameToDataBase, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Location controller:" + e.getMessage());
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (ReservationException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.FOUND.value(), e.getMessage()),
                    HttpStatus.FOUND);
        }
    }
}

package by.belyahovich.demo.service;

import by.belyahovich.demo.domain.Location;

public interface LocationService {
    Location saveLocationByNameToDataBase(String locationName);
}

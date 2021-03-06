package com.spenceuk.cardealer.service;

import static java.util.stream.Collectors.toList;

import com.spenceuk.cardealer.api.dto.VehicleDto;
import com.spenceuk.cardealer.api.exception.ApiException;
import com.spenceuk.cardealer.dao.entity.Vehicle;
import com.spenceuk.cardealer.dao.repo.VehicleRepo;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {
  private final VehicleRepo repo;
  private final ModelMapper mapper;

  /**
   * Returns a single vehicle by ID.
   *
   * @return a Vehicle Object.
   * @throws ApiException.class 404 NOT FOUND.
   */
  public VehicleDto oneByID(Long id) {
    var vehicle = repo.findById(id).orElseThrow(
        () -> ApiException.idNotFound(id.longValue()));
    return mapper.map(vehicle, VehicleDto.class);
  }

  /**
   * Returns all vehicles converted to VehicleDto Objects.
   *
   * @return List of VehicleDto Objects.
   */
  public List<VehicleDto> all() {
    return allVehicles().stream()
      .map(vehicle -> mapper.map(vehicle, VehicleDto.class))
          .collect(toList());
  }

  /**
   * Save a new vehicle to the database.
   *
   * @param newVehicle A new Vehicle Object to save.
   * @return A copy of the new saved Vehicle.
   */
  public VehicleDto save(VehicleDto newVehicle) {
    var unsaved = mapper.map(newVehicle, Vehicle.class);
    var saved = repo.save(unsaved);
    return mapper.map(saved, VehicleDto.class);
  }

  /**
   * Updates all vehicle values, for use with PUT requests.
   * @param updateDto new vehicle values.
   */
  public void update(VehicleDto updateDto) {
    var id = Long.valueOf(updateDto.getId());
    if (id.longValue() <= 0) {
      throw ApiException.noId();
    }
    if (!repo.existsById(id)) {
      throw ApiException.idNotFound(id.longValue());
    }
    repo.save(mapper.map(updateDto, Vehicle.class));
  }

  /**
   * Delete vehicle by ID.
   *
   * @param id ID of vehicle to DELETE.
   * @throws ApiException 404 NOT FOUND.
   */
  public void delete(Long id) {
    if (!repo.existsById(id)) {
      throw ApiException.idNotFound(id.longValue());
    }
    repo.deleteById(id);
  }

  /**
   * Finds all vehicles in the database.
   *
   * @return a List of all Vehicles.
   */
  private List<Vehicle> allVehicles() {
    return repo.findAll();
  }

}

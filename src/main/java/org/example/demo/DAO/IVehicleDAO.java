package org.example.demo.DAO;

import org.example.demo.Entity.Vehicle;

import java.util.List;

public interface IVehicleDAO {
    public Vehicle findById(Long id);
    public List<Vehicle> findAll();
    public void save(Vehicle vehicle);
    public void delete(Long id);
    public List<Vehicle> findByOwnerId(Long ownerId);
}

package org.example.demo.Decorators;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import org.example.demo.DAO.IVehicleDAO;
import org.example.demo.DAO.VehicleDAO;
import org.example.demo.Entity.Vehicle;

import java.sql.SQLOutput;
import java.util.List;

@Decorator
public abstract class VehicleDecorator implements IVehicleDAO {
    @Inject
    @Delegate
    private VehicleDAO vehicleDAO;

    @Override
    public Vehicle findById(Long id) {
        System.out.println("Looking for vehicle with ID: " + id + " ...");
        return vehicleDAO.findById(id);
    }

    @Override
    public List<Vehicle> findAll(){
        System.out.println("Looking for vehicles ...");
        return vehicleDAO.findAll();
    }

    @Override
    public void save(Vehicle vehicle){
        System.out.println("Saving vehicle: " + vehicle.getId() + " ...");
        vehicleDAO.save(vehicle);
    }

    @Override
    public void delete(Long id){
        System.out.println("Deleting vehicle with ID: " + id + " ...");
        vehicleDAO.delete(id);
    }

    @Override
    public List<Vehicle> findByOwnerId(Long ownerId){
        System.out.println("Looking for vehicles owned by client with ID: " + ownerId + " ...");
        return vehicleDAO.findByOwnerId(ownerId);
    }


}

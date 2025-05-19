package org.example.demo.Services;


import jakarta.ejb.AsyncResult;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.demo.DAO.VehicleDAO;
import org.example.demo.Entity.Vehicle;

import java.util.List;
import java.util.concurrent.Future;

@Stateless
public class AsyncCalculationService {
    @Inject
    private VehicleDAO vehicleDAO;

    @Asynchronous
    public Future<Double> calculateTotalInventoryValue() {
        try {
            System.out.println("SERVICE: Async method started");
            Thread.sleep(5000);
            System.out.println("SERVICE: Sleep finished");
            List<Vehicle> allVehicles = vehicleDAO.findAll();
            double totalValue = allVehicles.stream()
                    .mapToDouble(Vehicle::getPrice)
                    .sum();
            System.out.println("SERVICE: Async method finished, total value: " + totalValue);
            return new AsyncResult<>(totalValue);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new AsyncResult<>(0.0);
        } catch (Exception e) {
            System.out.println("SERVICE: Exception in async calculation: " + e.getMessage());
            e.printStackTrace();
            return new AsyncResult<>(0.0);
        }
    }
}
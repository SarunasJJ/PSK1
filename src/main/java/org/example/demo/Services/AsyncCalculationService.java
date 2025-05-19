package org.example.demo.Services;

import jakarta.ejb.AsyncResult;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.demo.DAO.VehicleDAO;
import org.example.demo.Entity.Vehicle;

import java.util.List;
import java.util.concurrent.Future;

@Stateless
public class AsyncCalculationService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private VehicleDAO vehicleDAO;

    @Asynchronous
    public Future<Double> calculateTotalInventoryValue() {
        try {
            Thread.sleep(5000);

            List<Vehicle> allVehicles = vehicleDAO.findAll();
            double totalValue = allVehicles.stream()
                    .mapToDouble(Vehicle::getPrice)
                    .sum();

            return new AsyncResult<>(totalValue);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new AsyncResult<>(0.0);
        } catch (Exception e) {
            return new AsyncResult<>(0.0);
        }
    }
}

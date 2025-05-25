package org.example.demo.Services;

import jakarta.ejb.AsyncResult;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.demo.Entity.Vehicle;

import java.util.List;
import java.util.concurrent.Future;

@Stateless
public class AsyncCalculationService {

    @PersistenceContext
    private EntityManager entityManager;

    @Asynchronous
    public Future<Double> calculateTotalInventoryValue() {
        try {
            System.out.println("SERVICE: Async method started");

            Thread.sleep(5000);

            System.out.println("SERVICE: Sleep finished, fetching vehicles");

            List<Vehicle> allVehicles = entityManager.createQuery(
                            "SELECT v FROM Vehicle v", Vehicle.class)
                    .getResultList();

            double totalValue = allVehicles.stream()
                    .mapToDouble(Vehicle::getPrice)
                    .sum();

            System.out.println("SERVICE: Async method finished, total value: " + totalValue);
            return new AsyncResult<>(totalValue);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("SERVICE: Calculation interrupted");
            return new AsyncResult<>(0.0);
        } catch (Exception e) {
            System.err.println("SERVICE: Exception in async calculation: " + e.getMessage());
            e.printStackTrace();
            return new AsyncResult<>(0.0);
        }
    }
}
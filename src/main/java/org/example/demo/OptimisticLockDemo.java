package org.example.demo;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.example.demo.Entity.Vehicle;
import org.example.demo.Services.DealerService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "optimisticLockDemo", value = "/optimistic-lock-demo")
public class OptimisticLockDemo extends HttpServlet {

    @Inject
    private DealerService dealerService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");

        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setManufacturer("Toyota");
            vehicle.setModel("Corolla");
            vehicle.setYear(2022);
            vehicle.setPrice(25000.0);

            dealerService.addVehicle(vehicle);
            Long vehicleId = vehicle.getId();

            out.println("<p>Created vehicle with ID: " + vehicleId + "</p>");

            demonstrateOptimisticLock(vehicleId, out);

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }

        out.println("</body></html>");
    }

    @Transactional
    private void demonstrateOptimisticLock(Long vehicleId, PrintWriter out) {
        try {
            Vehicle vehicle1 = dealerService.getVehicleById(vehicleId);
            out.println("<p>Transaction 1: Loaded vehicle: " + vehicle1.getManufacturer() + " " +
                    vehicle1.getModel() + ", Version: " + vehicle1.getVersion() + "</p>");

            Vehicle vehicle2 = entityManager.find(Vehicle.class, vehicleId);
            out.println("<p>Transaction 2: Loaded vehicle: " + vehicle2.getManufacturer() + " " +
                    vehicle2.getModel() + ", Version: " + vehicle2.getVersion() + "</p>");

            vehicle1.setPrice(27000.0);
            dealerService.updateVehicle(vehicle1);
            out.println("<p>Transaction 1: Updated price to 27000.0, Version " + vehicle1.getVersion() + "</p>");

            try {
                vehicle2.setPrice(26000.0);
                dealerService.updateVehicle(vehicle2);
                out.println("<p>Transaction 2: This should not be visible due to OptimisticLockException</p>");
            } catch (OptimisticLockException e) {
                out.println("<p>Transaction 2: OptimisticLockException caught! Version: " + vehicle2.getVersion() + "</p>");
                out.println("<p>Transaction 2: OptimisticLockException message: " + e.getMessage() + "</p>");
                Vehicle refreshedVehicle = dealerService.getVehicleById(vehicleId);
                out.println("<p>Refreshed vehicle from database, Version: " + refreshedVehicle.getVersion() + "</p>");
            }
        } catch (Exception e) {
            out.println("<p>Unexpected error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
}
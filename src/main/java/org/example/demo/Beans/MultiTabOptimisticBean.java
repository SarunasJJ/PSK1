package org.example.demo.Beans;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.OptimisticLockException;
import lombok.Getter;
import lombok.Setter;
import org.example.demo.Entity.Vehicle;
import org.example.demo.Services.DealerService;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class MultiTabOptimisticBean implements Serializable {

    @Inject
    private DealerService dealerService;

    private Long selectedVehicleId;
    private Vehicle vehicle;
    private String errorMessage = "";
    private String successMessage = "";

    // Fields for editing
    private Double newPrice;

    // Store the version when vehicle is loaded (simulates what each tab "remembers")
    private Integer loadedVersion;

    public List<Vehicle> getAllVehicles() {
        return dealerService.getAllVehicles();
    }

    public String loadVehicle() {
        System.out.println("LoadVehicle called - selectedVehicleId: " + selectedVehicleId);

        if (selectedVehicleId != null) {
            try {
                vehicle = dealerService.getVehicleById(selectedVehicleId);
                if (vehicle != null) {
                    // Initialize form fields and remember the version this tab loaded
                    newPrice = vehicle.getPrice();
                    loadedVersion = vehicle.getVersion();

                    successMessage = "Vehicle loaded with version: " + loadedVersion;
                    errorMessage = "";
                    System.out.println("Vehicle loaded successfully: " + vehicle.getManufacturer() + " " + vehicle.getModel());
                } else {
                    errorMessage = "Vehicle not found!";
                }
            } catch (Exception e) {
                errorMessage = "Error loading vehicle: " + e.getMessage();
                e.printStackTrace();
            }
        } else {
            errorMessage = "Please select a vehicle first!";
        }
        return null; // Stay on same page
    }

    public String updateVehicle() {
        System.out.println("UpdateVehicle called - selectedVehicleId: " + selectedVehicleId +
                ", newPrice: " + newPrice + ", loadedVersion: " + loadedVersion);

        if (selectedVehicleId == null) {
            errorMessage = "No vehicle selected!";
            return null;
        }

        if (newPrice == null) {
            errorMessage = "Please enter a price!";
            return null;
        }

        if (loadedVersion == null) {
            errorMessage = "No version information - please reload the vehicle!";
            return null;
        }

        try {
            // Get the vehicle we originally loaded (with the old version)
            Vehicle vehicleToUpdate = new Vehicle();
            vehicleToUpdate.setId(selectedVehicleId);
            vehicleToUpdate.setVersion(loadedVersion); // Use the version this tab remembers
            vehicleToUpdate.setPrice(newPrice);
            vehicleToUpdate.setManufacturer(vehicle.getManufacturer());
            vehicleToUpdate.setModel(vehicle.getModel());
            vehicleToUpdate.setYear(vehicle.getYear());
            vehicleToUpdate.setOwner(vehicle.getOwner());
            vehicleToUpdate.setOptions(vehicle.getOptions());

            // This will throw OptimisticLockException if version changed
            dealerService.updateVehicle(vehicleToUpdate);

            // If we get here, update was successful
            // Refresh the vehicle to get the new version
            vehicle = dealerService.getVehicleById(selectedVehicleId);
            loadedVersion = vehicle.getVersion(); // Update our loaded version

            successMessage = "Vehicle updated successfully! New version: " + vehicle.getVersion();
            errorMessage = "";

            System.out.println("Update successful, new version: " + vehicle.getVersion());

        } catch (OptimisticLockException e) {
            errorMessage = "REAL OPTIMISTIC LOCK EXCEPTION: " + e.getMessage();
            successMessage = "";

            System.out.println("OptimisticLockException caught: " + e.getMessage());

            // Reload current data to show what's actually in the database
            try {
                vehicle = dealerService.getVehicleById(selectedVehicleId);
                newPrice = vehicle.getPrice();
                errorMessage += " Current data: $" + vehicle.getPrice() + " (version " + vehicle.getVersion() + ")";
            } catch (Exception ex) {
                // Ignore refresh error
            }

        } catch (Exception e) {
            errorMessage = "Error updating vehicle: " + e.getMessage();
            successMessage = "";
            e.printStackTrace();
        }
        return null; // Stay on same page
    }

    public String resetForm() {
        selectedVehicleId = null;
        vehicle = null;
        newPrice = null;
        loadedVersion = null;
        errorMessage = "";
        successMessage = "";
        return null; // Stay on same page
    }

    public boolean isVehicleLoaded() {
        return vehicle != null;
    }
}
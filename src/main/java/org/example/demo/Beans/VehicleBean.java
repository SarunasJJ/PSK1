package org.example.demo.Beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.demo.Entity.Client;
import org.example.demo.Entity.Vehicle;
import org.example.demo.Services.DealerService;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
@NoArgsConstructor
@Named
@SessionScoped  // Changed to SessionScoped to maintain state between requests
public class VehicleBean implements Serializable {
    @Inject
    private DealerService dealerService;

    private Vehicle vehicle = new Vehicle();
    private Long vehicleId;
    private Long clientId;

    private List<Client> allClients;

    public List<Vehicle> getAllVehicles() {
        return dealerService.getAllVehicles();
    }

    public String saveVehicle() {
        try {
            dealerService.addVehicle(vehicle);
            vehicle = new Vehicle();
            return "vehicles?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "vehicles?faces-redirect=true";
        }
    }

    public String assignOwner(Long vehicleId) {
        try {
            this.vehicleId = vehicleId;
            allClients = dealerService.getAllClients();
            return "assign-owner?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "vehicles?faces-redirect=true";
        }
    }

    public String purchaseVehicle() {
        try {
            if (vehicleId == null || clientId == null) {
                return "vehicles?faces-redirect=true";
            }

            dealerService.purchaseVehicle(vehicleId, clientId);
            return "vehicles?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "vehicles?faces-redirect=true";
        }
    }
}
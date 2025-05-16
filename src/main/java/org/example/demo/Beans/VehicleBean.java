package org.example.demo.Beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.demo.Entity.Client;
import org.example.demo.Entity.Vehicle;
import org.example.demo.Services.DealerService;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Named
@RequestScoped
public class VehicleBean {
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
        dealerService.addVehicle(vehicle);
        vehicle = new Vehicle(); // Reset the vehicle object after saving
        return "vehicles";
    }

    public String assignOwner(Long vehicleId) {
        this.vehicleId = vehicleId;
        allClients = dealerService.getAllClients();
        return "assign-owner";
    }

    public String purchaseVehicle() {
        dealerService.purchaseVehicle(vehicleId, clientId);
        return "vehicles";
    }



}

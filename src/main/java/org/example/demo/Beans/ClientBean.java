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

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
@NoArgsConstructor
@Named
@RequestScoped
public class ClientBean implements Serializable {
    @Inject
    private DealerService dealerService;

    private Client client = new Client();
    private Client selectedClient;
    private List<Vehicle> clientVehicles;

    public List<Client> getAllClients() {
        return dealerService.getAllClients();
    }

    public String viewClientVehicles(Long clientId) {
        try {
            selectedClient = dealerService.getClientById(clientId);
            if (selectedClient == null) {
                return "clients?faces-redirect=true";
            }

            clientVehicles = dealerService.getVehiclesByClientId(clientId);
            return "clientVehicles?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "clients?faces-redirect=true";
        }
    }

    public String saveClient() {
        try {
            dealerService.addClient(client);
            client = new Client();
            return "clients?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "clients?faces-redirect=true";
        }
    }
}
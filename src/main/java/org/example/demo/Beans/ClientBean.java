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
public class ClientBean {
    @Inject
    private DealerService dealerService;

    private Client client = new Client();
    private Client selectedClient;
    private List<Vehicle> clientVehicles;

    public List<Client> getAllClients() {
        return dealerService.getAllClients();
    }

    public String viewClientVehicles(Long clientId) {
        selectedClient = dealerService.getClientById(clientId);
        clientVehicles = dealerService.getVehiclesByClientId(clientId);
        return "clientVehicles";
    }

    public String saveClient() {
        dealerService.addClient(client);
        client = new Client(); // Reset the client object after saving
        return "clients";
    }
}

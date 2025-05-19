package org.example.demo.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.example.demo.DAO.ClientDAO;
import org.example.demo.DAO.SalesPersonDAO;
import org.example.demo.DAO.VehicleDAO;
import org.example.demo.Entity.Client;
import org.example.demo.Entity.SalesPerson;
import org.example.demo.Entity.Vehicle;

import java.util.List;

@Alternative
@ApplicationScoped
public class AlternativeDealerService extends DealerService {
    @Inject
    private VehicleDAO vehicleDAO;

    @Inject
    private ClientDAO clientDAO;

    @Inject
    private SalesPersonDAO salesPersonDAO;

    @Override
    @Transactional
    public void purchaseVehicle(Long vehicleId, Long clientId) {
        System.out.println("Using alternative service for purchase");
        Vehicle vehicle = vehicleDAO.findById(vehicleId);
        Client client = clientDAO.findById(clientId);

        if (vehicle != null && client != null) {
            if (vehicle.getOwner() != null) {
                Client oldOwner = vehicle.getOwner();
                oldOwner.getVehicles().remove(vehicle);
                clientDAO.save(oldOwner);
            }

            vehicle.setOwner(client);

            if (!client.getVehicles().contains(vehicle)) {
                client.getVehicles().add(vehicle);
            }

            vehicleDAO.save(vehicle);
            clientDAO.save(client);
            System.out.println("Alternative purchase complete");
        }
    }

    @Override
    @Transactional
    public void updateVehicle(Vehicle vehicle) {
        try {
            vehicleDAO.save(vehicle);
        } catch (OptimisticLockException e) {
            throw e;
        }
    }

    @Override
    public List<Client> getAllClients() {
        return clientDAO.findAll();
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.findAll();
    }

    @Override
    public List<SalesPerson> getAllSalesPersons() {
        return salesPersonDAO.findAll();
    }

    @Override
    public List<Vehicle> getVehiclesByClientId(Long clientId) {
        return vehicleDAO.findByOwnerId(clientId);
    }

    @Override
    public Client getClientById(Long clientId) {
        return clientDAO.findById(clientId);
    }

    @Override
    public Vehicle getVehicleById(Long vehicleId) {
        return vehicleDAO.findById(vehicleId);
    }

    @Override
    public SalesPerson getSalesPersonById(Long salesPersonId) {
        return salesPersonDAO.findById(salesPersonId);
    }

    @Override
    @Transactional
    public void addClient(Client client) {
        clientDAO.save(client);
    }

    @Override
    @Transactional
    public void addVehicle(Vehicle vehicle) {
        vehicleDAO.save(vehicle);
    }

    @Override
    @Transactional
    public void addSalesPerson(SalesPerson salesPerson) {
        salesPersonDAO.save(salesPerson);
    }
}

package org.example.demo.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.demo.DAO.ClientDAO;
import org.example.demo.DAO.SalesPersonDAO;
import org.example.demo.DAO.VehicleDAO;
import org.example.demo.DAO.VehicleOptionDAO;
import org.example.demo.Entity.Client;
import org.example.demo.Entity.SalesPerson;
import org.example.demo.Entity.Vehicle;
import org.example.demo.Entity.VehicleOption;
import org.example.demo.Interceptors.Loggable;

import java.util.List;
import java.util.Set;

@ApplicationScoped
public class DealerService {
    @Inject
    private VehicleDAO vehicleDAO;

    @Inject
    private ClientDAO clientDAO;

    @Inject
    private SalesPersonDAO salesPersonDAO;

    @Inject
    private VehicleOptionDAO vehicleOptionDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void purchaseVehicle(Long vehicleId, Long clientId) {
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
        }
    }

    @Transactional
    public void assignOptionsToVehicle(Long vehicleId, List<Long> optionIds) {
        Vehicle vehicle = vehicleDAO.findById(vehicleId);
        if(vehicle != null){
            Set<VehicleOption> currentOptions = vehicle.getOptions();
            for(VehicleOption option : currentOptions){
                option.getVehicles().remove(vehicle);
            }
            currentOptions.clear();

            for(Long optionId : optionIds){
                VehicleOption option = vehicleOptionDAO.findById(optionId);
                if(option != null){
                    vehicle.addOption(option);
                }
            }
            vehicleDAO.save(vehicle);
        }
    }

    @Transactional
    public void updateVehicle(Vehicle vehicle) {
        try {
            Vehicle managedVehicle = entityManager.merge(vehicle);
            entityManager.flush();
        } catch (OptimisticLockException e) {
            throw e;
        }
    }

    @Transactional
    public void deleteVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleDAO.findById(vehicleId);
        if (vehicle != null) {
            Client client = vehicle.getOwner();
            if (client != null) {
                client.getVehicles().remove(vehicle);
                clientDAO.save(client);
            }
            vehicleDAO.delete(vehicleId);
        }
    }

    @Transactional
    public List<Client> getAllClients() {
        return clientDAO.findAll();
    }

    @Transactional
    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.findAll();
    }

    @Transactional
    public List<SalesPerson> getAllSalesPersons() {
        return salesPersonDAO.findAll();
    }

    @Transactional
    public List<VehicleOption> getAllVehicleOptions() {
        return vehicleOptionDAO.findAll();
    }

    @Transactional
    public List<Vehicle> getVehiclesByClientId(Long clientId) {
        return vehicleDAO.findByOwnerId(clientId);
    }

    @Transactional
    public Client getClientById(Long clientId) {
        return clientDAO.findById(clientId);
    }

    @Transactional
    public Vehicle getVehicleById(Long vehicleId) {
        return vehicleDAO.findById(vehicleId);
    }

    @Transactional
    public VehicleOption getVehicleOptionById(Long vehicleOptionId) {
        return vehicleOptionDAO.findById(vehicleOptionId);
    }

    @Transactional
    public SalesPerson getSalesPersonById(Long salesPersonId) {
        return salesPersonDAO.findById(salesPersonId);
    }

    @Transactional
    public void addClient(Client client) {
        clientDAO.save(client);
    }

    @Transactional
    public void addVehicle(Vehicle vehicle) {
        vehicleDAO.save(vehicle);
    }

    @Transactional
    public void addSalesPerson(SalesPerson salesPerson) {
        salesPersonDAO.save(salesPerson);
    }

    @Transactional
    public void addVehicleOption(VehicleOption vehicleOption) {
        vehicleOptionDAO.save(vehicleOption);
    }
}
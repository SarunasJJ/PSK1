package org.example.demo.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
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

import java.util.List;
import java.util.Set;

@Alternative
@ApplicationScoped
public class AlternativeDealerService extends DealerService {
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

    @Override
    @Transactional
    public void purchaseVehicle(Long vehicleId, Long clientId) {
        System.out.println("using alternative dealer service ...");
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

    @Override
    @Transactional
    public void assignOptionsToVehicle(Long vehicleId, List<Long> optionIds) {
        System.out.println("using alternative dealer service ...");
        Vehicle vehicle = vehicleDAO.findById(vehicleId);
        if(vehicle != null){
            // Clear existing options
            Set<VehicleOption> currentOptions = vehicle.getOptions();
            for(VehicleOption option : currentOptions){
                option.getVehicles().remove(vehicle);
            }
            currentOptions.clear();

            // Add new options
            for(Long optionId : optionIds){
                VehicleOption option = vehicleOptionDAO.findById(optionId);
                if(option != null){
                    vehicle.addOption(option);
                }
            }
            vehicleDAO.save(vehicle);
        }
    }

    @Override
    @Transactional
    public void updateVehicle(Vehicle vehicle) {
        System.out.println("using alternative dealer service ...");
        try {
            Vehicle managedVehicle = entityManager.merge(vehicle);
            entityManager.flush();
        } catch (OptimisticLockException e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteVehicle(Long vehicleId) {
        System.out.println("using alternative dealer service ...");
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

    @Override
    @Transactional
    public List<Client> getAllClients() {
        System.out.println("using alternative dealer service ...");
        return clientDAO.findAll();
    }

    @Override
    @Transactional
    public List<Vehicle> getAllVehicles() {
        System.out.println("using alternative dealer service ...");
        return vehicleDAO.findAll();
    }

    @Override
    @Transactional
    public List<SalesPerson> getAllSalesPersons() {
        System.out.println("using alternative dealer service ...");
        return salesPersonDAO.findAll();
    }

    @Override
    @Transactional
    public List<VehicleOption> getAllVehicleOptions() {
        System.out.println("using alternative dealer service ...");
        return vehicleOptionDAO.findAll();
    }

    @Override
    @Transactional
    public List<Vehicle> getVehiclesByClientId(Long clientId) {
        System.out.println("using alternative dealer service ...");
        return vehicleDAO.findByOwnerId(clientId);
    }

    @Override
    @Transactional
    public Client getClientById(Long clientId) {
        System.out.println("using alternative dealer service ...");
        return clientDAO.findById(clientId);
    }

    @Override
    @Transactional
    public Vehicle getVehicleById(Long vehicleId) {
        System.out.println("using alternative dealer service ...");
        return vehicleDAO.findById(vehicleId);
    }

    @Override
    @Transactional
    public VehicleOption getVehicleOptionById(Long vehicleOptionId) {
        System.out.println("using alternative dealer service ...");
        return vehicleOptionDAO.findById(vehicleOptionId);
    }

    @Override
    @Transactional
    public SalesPerson getSalesPersonById(Long salesPersonId) {
        System.out.println("using alternative dealer service ...");
        return salesPersonDAO.findById(salesPersonId);
    }

    @Override
    @Transactional
    public void addClient(Client client) {
        System.out.println("using alternative dealer service ...");
        clientDAO.save(client);
    }

    @Override
    @Transactional
    public void addVehicle(Vehicle vehicle) {
        System.out.println("using alternative dealer service ...");
        vehicleDAO.save(vehicle);
    }

    @Override
    @Transactional
    public void addSalesPerson(SalesPerson salesPerson) {
        System.out.println("using alternative dealer service ...");
        salesPersonDAO.save(salesPerson);
    }

    @Override
    @Transactional
    public void addVehicleOption(VehicleOption vehicleOption) {
        System.out.println("using alternative dealer service ...");
        vehicleOptionDAO.save(vehicleOption);
    }
}

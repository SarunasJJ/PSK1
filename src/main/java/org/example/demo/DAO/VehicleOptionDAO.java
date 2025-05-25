package org.example.demo.DAO;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.demo.Entity.Vehicle;
import org.example.demo.Entity.VehicleOption;

import java.util.List;

@RequestScoped
public class VehicleOptionDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public VehicleOption findById(Long id) {
        // Use JOIN FETCH to eagerly load vehicles
        return entityManager.createQuery(
                        "SELECT vo FROM VehicleOption vo LEFT JOIN FETCH vo.vehicles WHERE vo.id = :id",
                        VehicleOption.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<VehicleOption> findAll() {
        // Use JOIN FETCH to eagerly load vehicles
        return entityManager.createQuery(
                        "SELECT DISTINCT vo FROM VehicleOption vo LEFT JOIN FETCH vo.vehicles",
                        VehicleOption.class)
                .getResultList();
    }

    @Transactional
    public void save(VehicleOption vehicleOption) {
        if (vehicleOption.getId() == null) {
            entityManager.persist(vehicleOption);
        } else {
            entityManager.merge(vehicleOption);
        }
    }

    @Transactional
    public void delete(Long id) {
        VehicleOption vehicleOption = entityManager.find(VehicleOption.class, id);
        if (vehicleOption != null) {
            // Clear the many-to-many relationship first
            for (Vehicle vehicle : vehicleOption.getVehicles()) {
                vehicle.getOptions().remove(vehicleOption);
            }
            vehicleOption.getVehicles().clear();
            entityManager.merge(vehicleOption);
            entityManager.remove(vehicleOption);
        }
    }
}
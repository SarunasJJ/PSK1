package org.example.demo.DAO;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.demo.Entity.Vehicle;
import org.example.demo.Interceptors.Loggable;

import java.util.List;

@Loggable
@RequestScoped
public class VehicleDAO implements IVehicleDAO{
    @PersistenceContext
    private EntityManager entityManager;

    public Vehicle findById(Long id) {
        // Use JOIN FETCH to eagerly load options
        return entityManager.createQuery(
                        "SELECT v FROM Vehicle v LEFT JOIN FETCH v.options LEFT JOIN FETCH v.owner WHERE v.id = :id",
                        Vehicle.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<Vehicle> findAll() {
        // Use JOIN FETCH to eagerly load options and owner
        return entityManager.createQuery(
                        "SELECT DISTINCT v FROM Vehicle v LEFT JOIN FETCH v.options LEFT JOIN FETCH v.owner",
                        Vehicle.class)
                .getResultList();
    }

    @Transactional
    public void save(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            entityManager.persist(vehicle);
        } else {
            entityManager.merge(vehicle);
        }
    }

    @Transactional
    public void delete(Long id) {
        Vehicle vehicle = entityManager.find(Vehicle.class, id);
        if (vehicle != null) {
            // Clear the many-to-many relationship first
            vehicle.getOptions().clear();
            entityManager.merge(vehicle);
            entityManager.remove(vehicle);
        }
    }

    public List<Vehicle> findByOwnerId(Long ownerId) {
        return entityManager.createQuery(
                        "SELECT DISTINCT v FROM Vehicle v LEFT JOIN FETCH v.options WHERE v.owner.id = :ownerId",
                        Vehicle.class)
                .setParameter("ownerId", ownerId)
                .getResultList();
    }
}
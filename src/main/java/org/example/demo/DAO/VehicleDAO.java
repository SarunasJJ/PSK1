package org.example.demo.DAO;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.example.demo.Entity.Vehicle;

import java.util.List;

@RequestScoped
public class VehicleDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public Vehicle findById(Long id) {
        return entityManager.find(Vehicle.class, id);
    }

    public List<Vehicle> findAll() {
        return entityManager.createQuery("SELECT v FROM Vehicle v", Vehicle.class).getResultList();
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
        Vehicle vehicle = findById(id);
        if (vehicle != null) {
            entityManager.remove(vehicle);
        }
    }

    public List<Vehicle> findByOwnerId(Long ownerId) {
        return entityManager.createQuery("SELECT v FROM Vehicle v WHERE v.owner.id = :ownerId", Vehicle.class)
                .setParameter("ownerId", ownerId)
                .getResultList();
    }
}

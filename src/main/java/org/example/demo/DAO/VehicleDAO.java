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

    @Transactional
    public Vehicle findById(Long id) {
        return entityManager.find(Vehicle.class, id);
    }

    @Transactional
    public List<Vehicle> findAll() {
        return entityManager.createQuery("SELECT v FROM Vehicle v LEFT JOIN FETCH v.owner", Vehicle.class).getResultList();
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
        return entityManager.createQuery(
                        "SELECT v FROM Vehicle v WHERE v.owner.id = :ownerId",
                        Vehicle.class)
                .setParameter("ownerId", ownerId)
                .getResultList();
    }
}

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
        return entityManager.find(VehicleOption.class, id);
    }

    public List<VehicleOption> findAll() {
        return entityManager.createQuery("SELECT vo FROM VehicleOption vo", VehicleOption.class).getResultList();
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
        VehicleOption vehicleOption = findById(id);
        if (vehicleOption != null) {
            entityManager.remove(vehicleOption);
        }
    }
}

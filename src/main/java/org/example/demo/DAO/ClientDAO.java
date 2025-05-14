package org.example.demo.DAO;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.demo.Entity.Client;

import java.util.List;

@RequestScoped
public class ClientDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public Client findById(Long id) {
        return entityManager.find(Client.class, id);
    }

    public List<Client> findAll() {
        return entityManager.createQuery("SELECT c FROM Client c", Client.class).getResultList();
    }

    @Transactional
    public void save(Client client) {
        if (client.getId() == null) {
            entityManager.persist(client);
        } else {
            entityManager.merge(client);
        }
    }

    @Transactional
    public void delete(Long id) {
        Client client = findById(id);
        if (client != null) {
            entityManager.remove(client);
        }
    }
}

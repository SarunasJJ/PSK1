package org.example.demo.Mappers;

import org.example.demo.Entity.SalesPerson;

import java.util.List;

public interface SalesPersonMapper {
    SalesPerson findById(Long id);
    List<SalesPerson> findAll();
    void insert(SalesPerson salesPerson);
    void update(SalesPerson salesPerson);
    void delete(Long id);
}

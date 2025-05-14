package org.example.demo.Mappers;

import org.example.demo.Entity.SalesPerson;

import java.util.List;

public interface SalesPersonMapper {
    SalesPerson findById(Long id);
    List<SalesPerson> findAll();
    int insert(SalesPerson salesPerson);
    int update(SalesPerson salesPerson);
    int delete(Long id);
}

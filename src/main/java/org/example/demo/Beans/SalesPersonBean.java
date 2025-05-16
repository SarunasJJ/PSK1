package org.example.demo.Beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.demo.Entity.SalesPerson;
import org.example.demo.Services.DealerService;

import java.util.List;

@Getter
@Setter
@Named
@RequestScoped
public class SalesPersonBean {
    @Inject
    private DealerService dealershipService;

    private SalesPerson salesPerson = new SalesPerson();
    private SalesPerson selectedSalesperson;

    public List<SalesPerson> getSalespeople() {
        return dealershipService.getAllSalesPersons();
    }

    public String saveSalesperson() {
        dealershipService.addSalesPerson(salesPerson);
        salesPerson = new SalesPerson();
        return "salesperson";
    }
}

package org.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String socialSecurityNumber;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    @ManyToOne
    @JoinColumn(name = "sales_person_id")
    private SalesPerson salesPerson;

    @ManyToMany
    @JoinTable(
            name = "client_vehicle",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    private List<Vehicle> vehicles;

}
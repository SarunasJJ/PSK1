package org.example.demo.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String licensePlate;

    private String manufacturer;
    private String model;
    private int year;
    private String color;
    private String type;

    @ManyToMany(mappedBy = "vehicles")
    private List<Client> clients;

}

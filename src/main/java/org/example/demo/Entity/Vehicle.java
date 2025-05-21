package org.example.demo.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String manufacturer;
    private String model;
    private int year;
    private double price;

    @Version
    private Integer version;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client owner;

    @ManyToMany
    @JoinTable(name = "purchased_vehicle_options",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id"))
    private Set<VehicleOption> options = new HashSet<>();

    public void addOption(VehicleOption option) {
        this.options.add(option);
        option.getVehicles().add(this);
    }

    public void removeOption(VehicleOption option) {
        this.options.remove(option);
        option.getVehicles().remove(this);
    }
}

package org.example.demo.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}

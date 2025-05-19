package org.example.demo.Entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "email_address")
    private String email;
    private String phone;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonbTransient
    private List<Vehicle> vehicles = new ArrayList<>();
}
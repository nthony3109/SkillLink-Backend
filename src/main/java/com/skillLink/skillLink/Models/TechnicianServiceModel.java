package com.skillLink.skillLink.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "technicianServiceModels")
public class TechnicianServiceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(nullable = false,unique = true)
    private  String name;

    @ManyToMany(mappedBy = "serviceModels")
    private Set<Technician> technician = new HashSet<>();


}

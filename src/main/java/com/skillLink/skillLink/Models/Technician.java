package com.skillLink.skillLink.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "technicians")
public class Technician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String firstname;
    private String latename;
    private String othernames;
    @Column(nullable = false,updatable = true)
    private String phone;
    @Column(nullable = false,updatable = true)
    private  String email;
    private String profileImageUrl;

    // Spatial location stored as POINT (longitude, latitude)
    @Column(columnDefinition = "POINT SRID 4326", nullable = false)
    private Point location;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TechnicianService> skills;

    // method for more efficient bidirectional relationship
    public void addSkill(TechnicianService skill) {
        if (skills.size() > 3) {
            throw new IllegalArgumentException("A technician can have a maximum of 3 skills.");
        }
        skill.setTechnician(this);
        this.skills.add(skill);
    }

    public void removeSkill(TechnicianService skill) {
        skill.setTechnician(null);
        this.skills.remove(skill);
    }
}


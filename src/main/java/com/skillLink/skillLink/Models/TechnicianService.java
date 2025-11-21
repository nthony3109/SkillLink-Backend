package com.skillLink.skillLink.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "skills")
@Data
public class TechnicianService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "technician_id",nullable = false)
 private Technician technician;

 @Column(nullable = false)
    private String skillname;

    // Helper to assign technician
    public void assignTechnician(Technician technician) {
        this.technician = technician;
        if (!technician.getSkills().contains(this)) {
            technician.getSkills().add(this);
        }
    }

}

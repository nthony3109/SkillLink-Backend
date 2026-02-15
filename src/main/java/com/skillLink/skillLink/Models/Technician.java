package com.skillLink.skillLink.Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jspecify.annotations.Nullable;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "technicians")
public class Technician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String firstname;
    private String lastname;
    private String othernames;
    private String username;
    @Column(nullable = true,updatable = true)
    private String phone;

    @Column(nullable = false,updatable = true)
    private  String email;

    private String password;

    private String role;

    private String profileImageUrl;

    private  String locationName;

    private String bio;

    // Spatial location stored as POINT (longitude, latitude)
    @Column(columnDefinition = "POINT SRID 4326")
    private Point location;

    @Column(nullable = false)
    private boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "technician_service",
    joinColumns = @JoinColumn(name = "technician_id"),
    inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<TechnicianServiceModel>  serviceModels = new HashSet<>();

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefreshToken> refreshTokens = new HashSet<>();

}


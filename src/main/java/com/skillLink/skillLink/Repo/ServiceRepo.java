package com.skillLink.skillLink.Repo;

import com.skillLink.skillLink.Models.TechnicianServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceRepo extends JpaRepository<TechnicianServiceModel, Long> {
    Optional<TechnicianServiceModel> findByNameIgnoreCase(String t);
    Optional<TechnicianServiceModel> findByName(String t);

    @Query("""
    SELECT s.name
    FROM Technician t
    JOIN t.serviceModels s
    WHERE t.id = :techId
""")
    List<String> findServiceNamesByTechnicianId(@Param("techId") Long techId);


}

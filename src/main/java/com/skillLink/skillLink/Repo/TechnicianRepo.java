package com.skillLink.skillLink.Repo;

import com.skillLink.skillLink.DTOs.NearByTProjection;
import com.skillLink.skillLink.DTOs.NearByTechnicianProjection;
import com.skillLink.skillLink.Models.Technician;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TechnicianRepo extends JpaRepository<Technician, Long> {

    @Query(value = """
SELECT t.*
FROM technicians t
JOIN technician_service ts ON t.id = ts.technician_id
JOIN technician_service_models s ON ts.service_id = s.id
WHERE (:serviceName IS NULL OR  LOWER(s.name) LIKE CONCAT('%',LOWER(:serviceName),'%'))
  AND ST_Distance_Sphere(
        t.location,
        ST_GeomFromText(CONCAT('POINT(', :lng, ' ', :lat, ')'), 4326)
      ) <= :radius
ORDER BY ST_Distance_Sphere(
        t.location,
        ST_GeomFromText(CONCAT('POINT(', :lng, ' ', :lat, ')'), 4326)
      )
""", nativeQuery = true)

    List<Technician> findNearByTechnicians(
                    @Param("lng") double lng,
                    @Param("lat") double lat,
                    @Param("radius") double radius,
                    @Param("serviceName") String serviceName
            );

    Optional<Technician> findByEmail(String email);
    @Query("SELECT t FROM Technician t LEFT JOIN FETCH t.serviceModels sm")
    List<Technician> findAllWithServices();

    Optional<Technician> findByUsername(String username);

    @Modifying
    @Query(value = """
UPDATE technicians 
SET location = ST_GeomFromText(CONCAT('POINT(', :lng, ' ', :lat, ')'), 4326) 
WHERE id = :technicianId
""", nativeQuery = true)
    void updateLocation(
            @Param("technicianId") Long technicianId,
            @Param("lat") double lat,
            @Param("lng") double lng
    );

    @Query("SELECT t FROM Technician t LEFT JOIN FETCH t.serviceModels WHERE t.id = :id")
    Optional<Technician> findByIdWithServiceModels(@Param("id") Long id);

    @Query(value = """
SELECT t.id,
       t.firstname,
       t.lastname,
       t.username,
       t.phone,
       t.profile_image_url,
       t.location_name,
       t.bio,
       GROUP_CONCAT(s.name) AS services,
       ST_Distance_Sphere(
         t.location,
         ST_GeomFromText(CONCAT('POINT(', :lng, ' ', :lat, ')'), 4326)
       ) AS distance
FROM technicians t
JOIN technician_service ts ON t.id = ts.technician_id
JOIN technician_service_models s ON ts.service_id = s.id
WHERE (:serviceName IS NULL OR LOWER(s.name) LIKE CONCAT('%', LOWER(:serviceName), '%'))
  AND ST_Distance_Sphere(
        t.location,
        ST_GeomFromText(CONCAT('POINT(', :lng, ' ', :lat, ')'), 4326)
      ) <= :radius
GROUP BY t.id, t.firstname, t.lastname, t.username, t.phone,
         t.profile_image_url, t.location_name, t.bio, t.location
ORDER BY distance
""", nativeQuery = true)
    List<NearByTProjection> findNearByTechniciansWithDistance(
            @Param("lng") double lng,
            @Param("lat") double lat,
            @Param("radius") double radius,
            @Param("serviceName") String serviceName
    );

        boolean existsByEmail(String email);


}

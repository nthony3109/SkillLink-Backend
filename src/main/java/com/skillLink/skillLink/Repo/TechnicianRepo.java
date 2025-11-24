package com.skillLink.skillLink.Repo;

import com.skillLink.skillLink.Models.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TechnicianRepo extends JpaRepository<Technician, Long> {

    @Query(value = """
SELECT t.*
FROM technicians t
LEFT JOIN technician_service ts ON t.id = ts.technician_id
LEFT JOIN services s ON ts.service_id = s.id
WHERE (:serviceName IS NULL OR s.name = :serviceName)
  AND ST_Distance_Sphere(
        t.location,
        ST_SRID(POINT(:lon, :lat), 4326)
      ) <= :radius
ORDER BY ST_Distance_Sphere(
        t.location,
        ST_SRID(POINT(:lon, :lat), 4326)
      )

    """, nativeQuery = true)

    List<Technician> findNearByTechnicians(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("radius") double radius,
            @Param("skillName") String serviceName
    );

}

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
    LEFT JOIN skills s ON t.id = s.technician_id
    WHERE (:skillName IS NULL OR s.skillname = :skillName)
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
            @Param("skillName") String skillName
    );

}

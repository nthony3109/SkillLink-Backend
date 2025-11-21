package com.skillLink.skillLink.Service;

import com.skillLink.skillLink.DTOs.SkillsDTO;
import com.skillLink.skillLink.Models.Technician;
import com.skillLink.skillLink.Models.TechnicianService;
import com.skillLink.skillLink.Repo.TechnicianServiceRepo;
import com.skillLink.skillLink.Repo.TechnicianRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechService {
    // fields injections
    private final TechnicianServiceRepo technicianServiceRepo;
    private final TechnicianRepo technicianRepo;

    //methods to
    @Transactional
     public Technician addskills(Long technicianId, SkillsDTO skillsDTO) {
         Technician technician = technicianRepo.findById(technicianId)
                 .orElseThrow(() -> new RuntimeException("technician not found"));
 if (skillsDTO.getSkillName().size() > 3 ) {
     throw new IllegalArgumentException("maximum of 3 skills per technician");
 }
  for (String skillname : skillsDTO.getSkillName())
  {
      TechnicianService technicianService = TechnicianService.builder()
              .skillname(skillname)
              .build();
      technician.addSkill(technicianService);
  }
    return technicianRepo.save(technician);
     }
     // to  delete skills
    @Transactional
    public Technician deleteSkill(Long technicianId, SkillsDTO skillsDTO) {
        Technician technician = technicianRepo.findById(technicianId)
                .orElseThrow( () -> new IllegalCallerException("technician not found"));

        for (String name : skillsDTO.getSkillName()) {
            technician.getSkills()
                    .removeIf(skill -> skill.getSkillname()
                            .equalsIgnoreCase(name));

        }
        return  technicianRepo.save(technician);
    }

    @Transactional
    public Technician upddateTechnicianLocation(Long technicianId, double lat, double lon){
        Technician technician = technicianRepo.findById(technicianId)
                .orElseThrow(()-> new IllegalCallerException("technician not found"));
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint( new Coordinate(lon,lat));
        point.setSRID(4326);
        technician.setLocation(point);
        return technicianRepo.save(technician);
    }

    // find near by technicians
    public List<Technician> getNearByTechnicians(double lat, double lon, double radMters, String skillName){
        return technicianRepo.findNearByTechnicians(lat,lon,radMters,skillName);
    }



}

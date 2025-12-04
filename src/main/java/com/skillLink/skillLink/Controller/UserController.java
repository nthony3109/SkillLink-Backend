package com.skillLink.skillLink.Controller;

import com.skillLink.skillLink.DTOs.GetNearByTechniciansReq;
import com.skillLink.skillLink.DTOs.NearByTechnciansRes;
import com.skillLink.skillLink.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User API", description = "API for managing users")
public class UserController {
    private  final UserService userService;
    private double radiusInKm = 10.0;



    @PostMapping("/nearBy_technicians")
    @Operation(summary = "to get nearby technicians for a user")
    public ResponseEntity<?> getNearbyTechnicians(@Valid @RequestBody GetNearByTechniciansReq nearByDTO) {
        List<NearByTechnciansRes> nearByTechnciansRes = userService.getNearbyTechnicians(nearByDTO,radiusInKm);
        if (nearByTechnciansRes.isEmpty()) {
         radiusInKm = 20.0;
            nearByTechnciansRes   = userService.getNearbyTechnicians(nearByDTO, radiusInKm);
        }
        if (nearByTechnciansRes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("no user technician found");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(nearByTechnciansRes);
    }
}

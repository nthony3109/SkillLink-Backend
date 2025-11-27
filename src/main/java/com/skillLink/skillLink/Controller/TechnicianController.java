package com.skillLink.skillLink.Controller;


import com.skillLink.skillLink.DTOs.TechnicianRegisterReq;
import com.skillLink.skillLink.Models.Technician;
import com.skillLink.skillLink.Service.TechService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("api/technician")
@Tag( name= "Technician API", description = "API for managing technicians")
public class TechnicianController {
// for field injections
    @Autowired
    private TechService techService;

    @GetMapping("/test")
    @Operation(summary = "to test the swagger setup aand the app")
    public String returnText() {
        return "hello from technician controller";
    }

    @PostMapping("/register")
    @Operation(summary = "to register a technician")
    public ResponseEntity<?> registerTechnician(@Valid
                                                    @RequestBody TechnicianRegisterReq req,
                                                BindingResult result) {
        if ( result.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(result.getAllErrors()
                            .stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .toList());
        }
        return techService.registerTechnician( req)? ResponseEntity
                .status(HttpStatus.CREATED).body("technician registered ") : ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("registration failed");
    }

    @PutMapping("/{technicianId}/profile-image")
    @Operation(summary = "to add profile image to technician profile")
    public ResponseEntity<?> addProfileImage(@PathVariable Long technicianId,
                                             @RequestParam("file") MultipartFile file) {
        String imageUrl = techService.uploadimageToImgbb(file);
       Technician technicianUpdatedProfilePicture= techService.updateProfilePicture(technicianId, imageUrl);
        return ResponseEntity.ok(Map.of(
                "technicianId", technicianUpdatedProfilePicture.getId(),
                "profileImageUrl", technicianUpdatedProfilePicture.getProfileImageUrl()
        ));
    }

    // to get registered technician by id
    @GetMapping("/{technicianId}")
    @Operation(summary = "to get technician by id")
    public ResponseEntity<?> getTechnicianById(@PathVariable Long technicianId) {

        return ResponseEntity.ok(techService.getTechnicianById(technicianId));
    }
    @GetMapping("/all")
    @Operation(summary = "to get all registered technicians")
    public ResponseEntity<?> getAllTechnicians() {
        return ResponseEntity.ok(techService.getAllTechnicians());
    }
}

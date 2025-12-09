package com.skillLink.skillLink.Controller;


import com.skillLink.skillLink.DTOs.*;
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
        LoginRes res = techService.registerTechnician(req);
//        return res == null ? ResponseEntity
//                .status(HttpStatus.CREATED).body(res) : ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body("registration failed");
        return ResponseEntity.ok(res);
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

    @PutMapping("/{technicianId}/update/location")
    @Operation(summary = "to update technician location by lat and lng")
    public ResponseEntity<?> updateTechnicianLocation(@Valid @RequestBody UpdateLocationReq dto,
            @PathVariable Long technicianId) {
        boolean isUpdated = techService.updateTechnicianLocation(
                technicianId,
                dto.getLat(), dto.getLng()
        );
        if (!isUpdated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update location");
        }
        return ResponseEntity.ok("Location updated successfully");
    }

    @PutMapping("/{technicianId}/update/bio")
    @Operation(summary = "to update technician bio")
    public ResponseEntity<?> updateTechnicianBio(@Valid
                                                     @RequestBody UpdateBioReq req,
                                                 @PathVariable Long technicianId) {
        boolean isBioUpdated = techService.updateTechnicianBio(
                technicianId,
                req.getBio()
        );
        if (!isBioUpdated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update bio");
        }
        return  ResponseEntity.status(HttpStatus.ACCEPTED).body("bio updated successful");
    }

    @PutMapping("/{technicianId}/update/location-name")
    @Operation(summary = "to update technician location name")
    public ResponseEntity<?> updateTechnicianLocationName(@Valid
                                                          @RequestBody UpdateLocationNameReq req,
                                                          @PathVariable Long technicianId) {
        boolean isLocationNameUpdated = techService.updateTechnicianLocationName(
                technicianId,
                req.getLocationName());
        if (!isLocationNameUpdated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update location name");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("location name updated successful");
    }

    @PutMapping("/{technicianId}/update/phone")
    @Operation(summary = "to update technician phone number")
    public ResponseEntity<?> updateTechnicianPhone(@PathVariable Long technicianId,
                                                   @Valid @RequestBody UpdatePhoneReq req) {
        boolean isPhoneUpdated = techService.updateTechnicianPhone(
                technicianId,
                req.getPhone());
        if (!isPhoneUpdated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update phone number");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("phone number updated successful");
    }

    @PutMapping("/{technicianId}/add/service")
    @Operation(summary = "to add service to technician")
    public ResponseEntity<?> addServiceToTechnician(@PathVariable Long technicianId,
                                                    @Valid @RequestBody AddServiceToTechnicianReq req) {
         techService.addServiceToTechnician(technicianId, req.getName());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("service added to technician successful");
    }

    @PutMapping("/{technicianId}/update/service")
    @Operation(summary = "to update service of technician")
    public ResponseEntity<?> updateServiceOfTechnician(@PathVariable Long technicianId,
                                                       @Valid @RequestBody UpdateServiceReq req) {
        techService.updateService(technicianId, req.getOldServiceName(), req.getNewServiceName());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("service updated successful");
    }

    @DeleteMapping("/{technicianId}/delete/service")
    @Operation(summary = "to delete service from technician")
    public ResponseEntity<?> deleteServiceFromTechnician(@PathVariable Long technicianId, @Valid @RequestBody DeleteServiceReq req) {
        techService.deleteServiceFromTechnician(technicianId, req.getServiceName());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("service deleted from technician successful");
    }

    @PostMapping("/login")
    @Operation(summary = "technician login")
    public ResponseEntity<?> technicianLogin(@Valid @RequestBody TechnicianLoginReq req){
        LoginRes res = techService.loginTechnicianIn(req);
        if (res == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login failed");
        }
            return ResponseEntity.ok(res);
    }

    @PostMapping("check_email")
    @Operation(summary = "check if the the email exist before Registration")
    public ResponseEntity<?> checkIfEmailAlreadyExist(@RequestBody String email) {
        boolean isExisting= techService.checkIfEmailAlreadyExist(email);
        if (isExisting) {
            return ResponseEntity.status(HttpStatus.FOUND).body("email already taken");
        }
        return ResponseEntity.ok(null);
    }
}

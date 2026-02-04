package com.skillLink.skillLink.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillLink.skillLink.DTOs.*;
import com.skillLink.skillLink.Email.EmailContent;
import com.skillLink.skillLink.Email.VerificationCodeEmail;
import com.skillLink.skillLink.Models.RefreshToken;
import com.skillLink.skillLink.Models.Technician;
import com.skillLink.skillLink.Models.TechnicianServiceModel;
import com.skillLink.skillLink.Repo.ServiceRepo;
import com.skillLink.skillLink.Repo.TechnicianRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import java.io.IOException;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class TechService {
    // fields injections
    private final ServiceRepo serviceRepo;
    private final TechnicianRepo technicianRepo;
    @Value("${imgBB.api.key}")
    private  String apiKey;

    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private  final RestTemplate restTemplate = new RestTemplate();
    private  final RedisService redisService;
    private  EmailService emailService;

    @Transactional
    public LoginRes registerTechnician(TechnicianRegisterReq req) {

        //  check if email exists
        technicianRepo.findByEmail(req.getEmail())
                .ifPresent(t -> {
                    throw new RuntimeException("Email already in use");
                });

        //  Create spatial location point
        GeometryFactory geometryFactory = new GeometryFactory();
        Point locationPoint = geometryFactory.createPoint(
                new Coordinate(req.getLat(), req.getLng())
        );
        locationPoint.setSRID(4326);

        //  Build technician
        Technician technician = Technician.builder()
                .firstname(req.getFirstname())
                .lastname(req.getLastname())
                .othernames(req.getOthernames())
                .location(locationPoint)
                .locationName(req.getLocationName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .username(req.getUsername())
                .role("technician")
                .password(passwordEncoder.encode(req.getPassword().trim()))
                .bio(req.getBio())
                .build();

        // managing services (existing or create new)
        Set<TechnicianServiceModel> services =
                req.getServiceNames().stream()
                        .map(name -> serviceRepo.findByName(name)
                                .orElseGet(() -> serviceRepo.save(
                                        TechnicianServiceModel.builder()
                                                .name(name.trim().toLowerCase())
                                                .build()
                                ))
                        )
                        .collect(Collectors.toSet());

        //  Attaching  the services
        technician.setServiceModels(services);

        //  Save tecchnican
        technicianRepo.save(technician);

        // to return tokens after registration;
        Technician t = technicianRepo.findByEmail(req.getEmail())
                .orElseThrow( () -> new RuntimeException("technician not found"));

        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", t.getId());
        claims.put("provider","local");
        String token = jwtService.generateToken(claims,t.getEmail());
        RefreshToken rToken = refreshTokenService.generateRefreshToken(t);
        String refreshToken = rToken.getRefreshToken();
        // System.out.println(token);
        return LoginRes.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .userId(t.getId())
                .build();
    }


    public String uploadimageToImgbb(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            throw new RuntimeException("Invalid file type. Only JPEG and PNG are allowed.");
        }

        //  Validate file size (max 3MB)
        long maxSize = 3 * 1024 * 1024; // 3 MB
        if (file.getSize() > maxSize) {
            throw new RuntimeException("File is too large. Maximum size allowed is 3 MB.");
        }

       try {
           byte[] bytes = file.getBytes();
           String base64 = Base64.getEncoder().encodeToString(bytes);

           MultiValueMap<String, String>  body = new LinkedMultiValueMap<>();
           body.add("key", apiKey);
              body.add("image", base64);
           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

           // Make POST
           RestTemplate restTemplate = new RestTemplate();
           ResponseEntity<String> response = restTemplate.postForEntity(
                   "https://api.imgbb.com/1/upload",
                   request,
                   String.class
           );
//           ObjectMapper mapper = new ObjectMapper();
//           JsonNode jsonResponse = mapper.readTree(response.getBody().toString());

           if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
               // Extract image URL
               ObjectMapper mapper = new ObjectMapper();
               JsonNode jsonResponse = mapper.readTree(response.getBody());
               return jsonResponse.path("data").path("url").asText();
           } else {
               throw new RuntimeException("ImgBB upload failed with status: " + response.getStatusCode());
           }

       } catch (IOException e) {
           throw new RuntimeException("failed to upload url", e);
       }
    }

    public Technician updateProfilePicture(Long technicianId, String imageUrl) {

        Technician technician = technicianRepo.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        // Update technician entity
        technician.setProfileImageUrl(imageUrl);

        return technicianRepo.save(technician);
    }

    public TechnicianRes getTechnicianById(Long technicianId) {

        Technician technician = technicianRepo.findById(technicianId)
                .orElseThrow( () -> new RuntimeException("Technician not found"));

        List<String> services = technician.getServiceModels().stream()
                .map(TechnicianServiceModel::getName)
                .collect(Collectors.toList());

        TechnicianRes technicianRes = TechnicianRes.builder()
                .technicianId(technician.getId())
                .firstname(technician.getFirstname())
                .lastname(technician.getLastname())
                .username(technician.getUsername())
                .phone(technician.getPhone())
                .email(technician.getEmail())
                .locationName(technician.getLocationName())
                .bio(technician.getBio())
                .services(services)
                .build();

    return technicianRes;
    }

    @Transactional
    public AllTechnicians getAllTechnicians() {
        List<Technician> technician = technicianRepo.findAll();
        List<TechnicianRes> technicianRes = technician.stream()
                .map(t -> TechnicianRes.builder()
                .technicianId(t.getId())
                .firstname(t.getFirstname())
                .lastname(t.getLastname())
                .username(t.getUsername())
                .phone(t.getPhone())
                .email(t.getEmail())
                .locationName(t.getLocationName())
                .bio(t.getBio())
                .services(
                       new ArrayList<>(t.getServiceModels())
                               .stream()
                                .map(TechnicianServiceModel::getName)
                                .collect(Collectors.toList())
                )
                .build()
        ).collect(Collectors.toList());
        return AllTechnicians.builder()
                .alltechnician(technicianRes)
                .build();
    }

    @Transactional
    public boolean updateTechnicianLocation(Long technicianId, double lat, double lng) {
        boolean exists = technicianRepo.existsById(technicianId);
        if (!exists) {
            throw new RuntimeException("Technician not found");
        }
        try {
            technicianRepo.updateLocation(technicianId, lat, lng);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("failed to update location", e);
        }

    }


    public boolean updateTechnicianBio(Long technicianId,  String bio) {
        try {
            Technician technician = technicianRepo.findById(technicianId)
                    .orElseThrow(() -> new RuntimeException("Technician not found"));
            technician.setBio(bio);
            technicianRepo.save(technician);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("failed to update bio", e);
        }
    }

    public boolean updateTechnicianLocationName(Long technicianId, String locationName) {
        try {
            Technician technician = technicianRepo.findById(technicianId)
                    .orElseThrow(() -> new RuntimeException("Technician not found"));
            technician.setLocationName(locationName);
            technicianRepo.save(technician);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("failed to update location name", e);
        }
    }

    public boolean updateTechnicianPhone(Long technicianId,  String phone) {
        try {
            Technician technician = technicianRepo.findById(technicianId)
                    .orElseThrow(() -> new RuntimeException("Technician not found"));
            technician.setPhone(phone);
            technicianRepo.save(technician);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("failed to update phone number", e);
        }
    }

    private TechnicianRes mapToRes(Technician technician) {
        System.out.println("in mapToDTO method");
        List<String> services = technician.getServiceModels().stream()
                .map(TechnicianServiceModel::getName)
                .toList();

        TechnicianRes res = TechnicianRes.builder()
                .firstname(technician.getFirstname())
                .lastname(technician.getLastname())
                .username(technician.getUsername())
                .phone(technician.getPhone())
                .profileImageUrl(technician.getProfileImageUrl())
                .locationName(technician.getLocationName())
                .bio(technician.getBio())
                .services(services)
                .build();
        System.out.println("Mapped TechnicianRes: " + res);
        return res;
    }

    @Transactional
    public TechnicianRes addServiceToTechnician( Long technicianId, String name) {
        Technician technician = technicianRepo.findByIdWithServiceModels(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        String serviceName = name.trim().toLowerCase();
        TechnicianServiceModel serviceModel = serviceRepo.findByNameIgnoreCase(serviceName)
                .orElseGet(
                        () -> {
                            TechnicianServiceModel m = new TechnicianServiceModel();
                            m.setName(serviceName);
                            return serviceRepo.save(m);
                        }
                );
        // to avoid duplicate services
        if (technician.getServiceModels().contains(serviceModel)) {
            throw new RuntimeException("Service already added to technician");
        }
        technician.getServiceModels().add(serviceModel);
        technicianRepo.save(technician);
        return mapToRes(technician);
    }

    public TechnicianRes updateService(Long technicianId, String oldName, String newName) {
        Technician technician = technicianRepo.findByIdWithServiceModels(technicianId)
                .orElseThrow( () -> new RuntimeException("Technician not found"));
        String oldServiceName = oldName.trim().toLowerCase();
        String newServiceName = newName.trim().toLowerCase();

        TechnicianServiceModel oderService = technician.getServiceModels().stream()
                .filter(s -> s.getName().equalsIgnoreCase(oldServiceName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Old service not found for technician"));

        TechnicianServiceModel newService = serviceRepo.findByNameIgnoreCase(newServiceName)
                .orElseGet(() -> {
                    TechnicianServiceModel m = new TechnicianServiceModel();
                    m.setName(newServiceName);
                    return serviceRepo.save(m);
                });
        technician.getServiceModels().remove(oderService);
        technician.getServiceModels().add(newService);
        technicianRepo.save(technician);
        return mapToRes(technician);
    }

    public void deleteServiceFromTechnician(Long technicianId,  String name) {
        Technician technician = technicianRepo.findByIdWithServiceModels(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        String serviceName = name.trim().toLowerCase();

        TechnicianServiceModel serviceModel = technician.getServiceModels().stream()
                .filter(s -> s.getName().equalsIgnoreCase(serviceName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Service not found for technician"));

        technician.getServiceModels().remove(serviceModel);
        technicianRepo.save(technician);
    }

    public LoginRes loginTechnicianIn(TechnicianLoginReq req) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail().trim(),req.getPassword().trim()
                )
        );
        UserDetails technician = (UserDetails) authentication.getPrincipal();
        assert technician != null;
        Technician t = technicianRepo.findByEmail(technician.getUsername())
                .orElseThrow( () -> new RuntimeException("technician not found"));
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", t.getId());
        claims.put("provider","local");
        String token = jwtService.generateToken(claims,technician.getUsername());
        RefreshToken rToken = refreshTokenService.generateRefreshToken(t);
        String refreshToken = rToken.getRefreshToken();
        return LoginRes.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .userId(t.getId())
                .build();

    }

    public boolean checkIfEmailAlreadyExist(@NotBlank(message = "email is blank") String email) {
        boolean exists = technicianRepo.existsByEmail(email);
        if (!exists) {
           String code =  redisService.GenerateCode();
           redisService.saveCode(email,code);
            EmailContent content = new VerificationCodeEmail(code);
            emailService.sendEmail(email,"Verification Code",content);
        }
        return technicianRepo.existsByEmail(email);
    }
}

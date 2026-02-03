package com.skillLink.skillLink.Service;


import com.skillLink.skillLink.DTOs.GetNearByTechniciansReq;
import com.skillLink.skillLink.DTOs.NearByTProjection;
import com.skillLink.skillLink.DTOs.NearByTechniciansRes;
import com.skillLink.skillLink.Repo.TechnicianRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private  final TechnicianRepo technicianRepo;



//    public List<NearByTechniciansRes> getNearbyTechnicians(GetNearByTechniciansReq dto, double radius ){
//        double lng = dto.getLng();
//        double lat = dto.getLat();
//        String serviceName = dto.getServiceName().trim().toLowerCase();
//        List<NearByTProjection> projections = technicianRepo.findAllNearByTechnicians(lng, lat, radius, serviceName);
//
//        return projections.stream().map(t -> {
//
//            List<String> services = t.getServices() == null || t.getServices().isEmpty()
//                    ? List.of()
//                    : Arrays.asList(t.getServices().split(","));
//
//                    return NearByTechniciansRes.builder()
//                            .id(t.getId())
//                            .firstname(t.getFirstname())
//                            .lastname(t.getLastname())
//                            .username(t.getUsername())
//                            .phone(t.getPhone())
//                            .profileImageUrl(t.getProfileImageUrl())
//                            .locationName(t.getLocationName())
//                            .bio(t.getBio())
//                            .distanceInKm(Math.round((t.getDistance()/1000) * 100) / 10.0) // 1 decimal place
//                            .services(services)
//                            .build();
//
//        }).collect(Collectors.toList());
//    }

//    public List<NearByTechniciansRes> getNearbyTechnicians(GetNearByTechniciansReq dto, double radius) {
//        double lng = dto.getLng();
//        double lat = dto.getLat();
//        String serviceName = dto.getServiceName().trim().toLowerCase();
//
//        List<NearByTProjection> projections =
//                technicianRepo.findAllNearByTechnicians(lng, lat, radius, serviceName);
//
//        return projections.stream()
//               // .filter(t -> t.getDistance() <= radius) // filter first
//                .map(t -> {
//
//                    List<String> services = (t.getServices() == null || t.getServices().isEmpty())
//                            ? List.of()
//                            : Arrays.asList(t.getServices().split(","));
//
//                    double distanceInKm = Math.round((t.getDistance() / 1000) * 10) / 10.0;
//                    System.out.println(t.getDistance());
//
//                    return NearByTechniciansRes.builder()
//                            .id(t.getId())
//                            .firstname(t.getFirstname())
//                            .lastname(t.getLastname())
//                            .username(t.getUsername())
//                            .phone(t.getPhone())
//                            .profileImageUrl(t.getProfileImageUrl())
//                            .locationName(t.getLocationName())
//                            .bio(t.getBio())
//                            .services(services)
//                            .distanceInKm(distanceInKm)
//                            .build();
//                })
//                .collect(Collectors.toList());
//    }

    private NearByTechniciansRes mapToRes(NearByTProjection projection) {
        List<String> services = List.of(projection.getServices().split(","));
        return NearByTechniciansRes.builder()
                .id(projection.getId())
                .firstname(projection.getFirstname())
                .lastname(projection.getLastname())
                .username(projection.getUsername())
                .phone(projection.getPhone())
                .profileImageUrl(projection.getProfileImageUrl())
                .locationName(projection.getLocationName())
                .bio(projection.getBio())
                .services(services)
                .distanceInKm(projection.getDistance() / 1000.0) // convert meters → km
                .build();
    }

    public List<NearByTechniciansRes> getNearbyTechnicians(GetNearByTechniciansReq dto, double radiusKm) {
        double radiusMeters = radiusKm * 1000; // convert km → meters
        String serviceName = dto.getServiceName().trim().toLowerCase();

        List<NearByTProjection> projections =
                technicianRepo.findNearByTechniciansWithDistance(dto.getLng(), dto.getLat(), radiusMeters, serviceName);

        return projections.stream()
                .map(this::mapToRes)
                .toList();
    }

}

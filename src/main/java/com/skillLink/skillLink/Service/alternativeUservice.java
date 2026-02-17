//package com.skillLink.skillLink.Service;
//
//
//import com.skillLink.skillLink.DTOs.GetNearByTechniciansReq;
//import com.skillLink.skillLink.DTOs.NearByTechniciansRes;
//import com.skillLink.skillLink.Models.Technician;
//import com.skillLink.skillLink.Models.TechnicianServiceModel;
//import com.skillLink.skillLink.Repo.TechnicianRepo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class alternativeUservice {
//    //  for  field injection
//    private  final TechnicianRepo technicianRepo;
//
//    private NearByTechniciansRes mapToRes(Technician technician,double distanceInMeters) {
//       // System.out.println("in mapToDTO method");
//        List<String> services = technician.getServiceModels().stream()
//                .map(TechnicianServiceModel::getName)
//                .toList();
//        double km = distanceInMeters / 1000.0;
//        double distanceInKm = Math.round(km * 10.0) / 10.0;
//
//        NearByTechniciansRes res = NearByTechniciansRes.builder()
//                .firstname(technician.getFirstname())
//                .lastname(technician.getLastname())
//                .username(technician.getUsername())
//                .phone(technician.getPhone())
//                .profileImageUrl(technician.getProfileImageUrl())
//                .locationName(technician.getLocationName())
//                .bio(technician.getBio())
//                .distanceInKm(distanceInKm)
//                .services(services)
//                .build();
//        System.out.println("Mapped TechnicianRes: " + res);
//        return res;
//    }
//    public List<NearByTechniciansRes> getNearByTechnicians(GetNearByTechniciansReq nearByDTo, double radiusInMeters) {
//         double lat = nearByDTo.getLat();
//         double lng = nearByDTo.getLng();
//         System.out.println("in get nearby technicians method");
//          //double radius = nearByDTo.getRadius() * 1000; // convert km to meters
//            String serviceName = nearByDTo.getServiceName().trim().toLowerCase();
//        List<Object[]> result= technicianRepo.findNearByTechniciansWithDistance(lat,lng,radiusInMeters,serviceName);
//        return result.stream()
//                .map( row -> {
//                    Technician t = (Technician) row[0];
//                    double distance = (double) row[1];
//                    return mapToRes(t, distance);
//                })
//                .toList();
//
//
//    }
//}

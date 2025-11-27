package com.skillLink.skillLink.Repo;

import com.skillLink.skillLink.Models.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicianServiceRepo extends JpaRepository<Service, Long> {
}

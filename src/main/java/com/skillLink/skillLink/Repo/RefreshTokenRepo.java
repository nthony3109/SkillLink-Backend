package com.skillLink.skillLink.Repo;

import com.skillLink.skillLink.Models.RefreshToken;
import com.skillLink.skillLink.Models.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByRefreshToken(String token);

    void deleteAllRefreshTokenByTechnician(Technician t);
}

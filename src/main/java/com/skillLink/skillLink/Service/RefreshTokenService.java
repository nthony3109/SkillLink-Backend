package com.skillLink.skillLink.Service;


import com.skillLink.skillLink.Models.RefreshToken;
import com.skillLink.skillLink.Models.Technician;
import com.skillLink.skillLink.Repo.RefreshTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  private final RefreshTokenRepo refreshTokenRepo;

    public RefreshToken generateRefreshToken(Technician t) {
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString())
                .expiryTime(LocalDateTime.now().plusDays(30))
                .technician(t)
                .build();

        return refreshTokenRepo.save(refreshToken);
    }

    public  boolean isTokenValid(String token) {
        return  refreshTokenRepo.findByRefreshToken(token)
                .filter(tk -> tk.getExpiryTime()
                        .isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public void deleteAllRefreshToken(Technician t) {
        refreshTokenRepo.deleteAllRefreshTokenByTechnician(t);
    }
}

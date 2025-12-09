package com.skillLink.skillLink.Config;


import com.skillLink.skillLink.Models.RefreshToken;
import com.skillLink.skillLink.Models.Technician;
import com.skillLink.skillLink.Repo.RefreshTokenRepo;
import com.skillLink.skillLink.Repo.TechnicianRepo;
import com.skillLink.skillLink.Service.JwtService;
import com.skillLink.skillLink.Service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class Oauth2Handler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final TechnicianRepo tRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final RefreshTokenService refreshTokenService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_OK);

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        assert oAuth2User != null;
        String email = (String) oAuth2User.getAttribute("email");
        String name = (String) oAuth2User.getAttribute("name");

        if (email == null) {
            Object sub = oAuth2User.getAttribute("sub");
            email = sub != null ? sub.toString() : null;
        }
        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"email not found ");
        }
        String finalEmail = email;
        Technician t = tRepo.findByEmail(finalEmail)
                .orElseGet( () -> {
                            Technician createTechnician = Technician.builder()
                                    .username(name)
                                    .email(finalEmail)
                                    .role("user")
                                    .build();
                            return tRepo.save(createTechnician);
                        }
                );
        Map<String,Object> claims = new HashMap<>();
        claims.put("email",email);
        claims.put("role","user");
        claims.put("provider","google");
        claims.put("UserId",t.getId());

        // return userId to front end
        String userId = String.valueOf(t.getId());

        String token = jwtService.generateToken(claims,email);
        RefreshToken rToken = refreshTokenService.generateRefreshToken(t);
        String refreshToken = rToken.getRefreshToken();


    // send the details to UI
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{ \"token\": \"" + token + "\", \"email\": \"" + email + "\", \"refreshToken\": \"" + refreshToken + "\", \"userId\": \"" + userId + "\"  }"
        );
    }
}

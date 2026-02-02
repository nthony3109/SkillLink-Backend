package com.skillLink.skillLink.Service;

import com.skillLink.skillLink.Models.Technician;
import com.skillLink.skillLink.Repo.TechnicianRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private  final TechnicianRepo technicianRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Technician t = technicianRepo.findByEmail(email)
                .orElseThrow( () -> new UsernameNotFoundException("technician not found"));
        return User.builder()
                .username(t.getEmail())
                .password(t.getPassword())
                .roles(t.getRole())
                .build();

    }
}

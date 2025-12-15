package com.skillLink.skillLink.DTOs;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class AllTechnicians {
    private List<TechnicianRes> alltechnician;
}

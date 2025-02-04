package com.example.jobportal.dto;

import lombok.*;

@Data  // Lombok will generate getters and setters
@NoArgsConstructor
@AllArgsConstructor
public class JobRequestDto {
    private String title;
    private String description;
    private String company;
    private String location;
    private String requiredSkills;

    // ✅ Manually add getter methods if needed
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }
}


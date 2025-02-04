package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Data  // Lombok will generate getters and setters
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String company;
    private String location;
    private String requiredSkills;
    private double salary;

    // ✅ Manually add getter methods if needed
    public Long getId(){
        return id;
    }
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

    public double getSalary() {
        return salary;
    }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setCompany(String company) { this.company = company; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }

}

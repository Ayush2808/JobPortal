package com.example.jobportal.controller;

import com.example.jobportal.dto.JobRequestDto;
import com.example.jobportal.dto.JobResponseDto;
import com.example.jobportal.model.Job;
import com.example.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

    // POST method to create a job
    @PostMapping
    public ResponseEntity<JobResponseDto> createJob(@RequestBody JobRequestDto dto) {
        // Create job using service
        Job createdJob = jobService.createJob(dto);

        // Convert Job to JobResponseDto
        JobResponseDto responseDto = new JobResponseDto(
                createdJob.getId(),
                createdJob.getTitle(),
                createdJob.getDescription(),
                createdJob.getLocation(),
                createdJob.getSalary(),
                createdJob.getCompany(),
                createdJob.getRequiredSkills()
        );

        // Return the created JobResponseDto
        return ResponseEntity.ok(responseDto);
    }

    // GET method to retrieve jobs by location and skills
    @GetMapping
    public ResponseEntity<List<Job>> getJobs(@RequestParam String location, @RequestParam String skills) {
        return ResponseEntity.ok(jobService.getJobsByLocationAndSkills(location, skills));
    }
}

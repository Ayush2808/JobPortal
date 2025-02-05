package com.example.jobportal.controller;

import com.example.jobportal.dto.JobRequestDto;
import com.example.jobportal.dto.JobResponseDto;
import com.example.jobportal.model.Job;
import com.example.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    // JobController.java
    @GetMapping
    public ResponseEntity<List<JobResponseDto>> getJobs(@RequestParam String location, @RequestParam String skills) {
        // Fetch jobs from the service
        List<Job> jobs = jobService.getJobsByLocationAndSkills(location, skills);

        if (jobs.isEmpty()) {
            // If no jobs found, return No Content status
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        // Convert List<Job> to List<JobResponseDto>
        List<JobResponseDto> responseDtos = jobs.stream()
                .map(job -> new JobResponseDto(
                        job.getId(),
                        job.getTitle(),
                        job.getDescription(),
                        job.getLocation(),
                        job.getSalary(),
                        job.getCompany(),
                        job.getRequiredSkills()
                ))
                .collect(Collectors.toList());

        // Return the converted list of JobResponseDto with OK status
        return ResponseEntity.ok(responseDtos);
    }
    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDto> updateJob(@PathVariable Long id, @RequestBody JobRequestDto dto) {
        System.out.println("Updating Job with ID: " + id);
        System.out.println("JobRequestDto: " + dto);

        Job updatedJob = jobService.updateJob(id, dto);

        if (updatedJob == null) {
            return ResponseEntity.notFound().build();
        }

        // Convert Job to JobResponseDto
        JobResponseDto responseDto = new JobResponseDto(
                updatedJob.getId(),
                updatedJob.getTitle(),
                updatedJob.getDescription(),
                updatedJob.getLocation(),
                updatedJob.getSalary(),
                updatedJob.getCompany(),
                updatedJob.getRequiredSkills()
        );

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        boolean isDeleted = jobService.deleteJob(id);

        if (isDeleted) {
            return ResponseEntity.ok("Job with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Job with ID " + id + " not found.");
        }
    }


}




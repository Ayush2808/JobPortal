package com.example.jobportal.controller;

import com.example.jobportal.dto.JobRequestDto;
import com.example.jobportal.dto.JobResponseDto;
import com.example.jobportal.model.Job;
import com.example.jobportal.service.JobService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponseDto> createJob(@RequestBody JobRequestDto dto) throws JsonProcessingException {
        Job createdJob = jobService.createJob(dto);
        JobResponseDto responseDto = convertToDto(createdJob);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<JobResponseDto>> getJobs(@RequestParam String location, @RequestParam String skills) {
        List<Job> jobs = jobService.getJobsByLocationAndSkills(location, skills);
        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<JobResponseDto> responseDtos = jobs.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDto> updateJob(@PathVariable Long id, @RequestBody JobRequestDto dto) throws JsonProcessingException {
        Job updatedJob = jobService.updateJob(id, dto);
        if (updatedJob == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(updatedJob));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        boolean isDeleted = jobService.deleteJob(id);
        if (isDeleted) {
            return ResponseEntity.ok("Job with ID " + id + " deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job with ID " + id + " not found.");
    }

    private JobResponseDto convertToDto(Job job) {
        return new JobResponseDto(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getSalary(),
                job.getCompany(),
                job.getRequiredSkills()
        );
    }
}

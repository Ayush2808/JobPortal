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

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody JobRequestDto dto) {
        return ResponseEntity.ok(jobService.createJob(dto));
    }

    @GetMapping
    public ResponseEntity<List<Job>> getJobs(@RequestParam String location, @RequestParam String skills) {
        return ResponseEntity.ok(jobService.getJobsByLocationAndSkills(location, skills));
    }


}


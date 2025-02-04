package com.example.jobportal.service;



import com.example.jobportal.dto.JobRequestDto;
import com.example.jobportal.model.Job;
import com.example.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    public Job createJob(JobRequestDto dto) {
        Job job = new Job();
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setCompany(dto.getCompany());
        job.setLocation(dto.getLocation());
        job.setRequiredSkills(dto.getRequiredSkills());
        job.setSalary(dto.getSalary());

        Job savedJob = jobRepository.save(job);
        return savedJob;
    }

    public List<Job> getJobsByLocationAndSkills(String location, String skills) {
        return jobRepository.findByLocationAndRequiredSkillsContaining(location, skills);
    }
}
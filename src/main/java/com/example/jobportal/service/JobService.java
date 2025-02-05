package com.example.jobportal.service;



import com.example.jobportal.dto.JobRequestDto;
import com.example.jobportal.model.Job;
import com.example.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public Job updateJob(Long id, JobRequestDto dto) {
        Optional<Job> optionalJob = jobRepository.findById(id);

        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();

            // Log the initial job data
            System.out.println("Existing Job before update: " + job);

            // Update only the fields that are not null
            if (dto.getTitle() != null) {
                job.setTitle(dto.getTitle());
            }

            if (dto.getDescription() != null) {
                job.setDescription(dto.getDescription());
            }

            if (dto.getLocation() != null) {
                job.setLocation(dto.getLocation());
            }

            if (dto.getSalary() != 0) { // Assuming salary is a valid number (you can modify this logic if salary can be zero)
                job.setSalary(dto.getSalary());
            }

            if (dto.getCompany() != null) {
                job.setCompany(dto.getCompany());
            }

            if (dto.getRequiredSkills() != null) {
                job.setRequiredSkills(dto.getRequiredSkills());
            }

            // Log the updated job data
            System.out.println("Updated Job: " + job);

            // Save the updated job
            return jobRepository.save(job);
        }

        return null; // If job not found, return null
    }

    @Transactional
    public boolean deleteJob(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);

        if (optionalJob.isPresent()) {
            jobRepository.deleteById(id);
            return true; // Job deleted successfully
        }

        return false; // Job not found
    }


}
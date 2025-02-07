package com.example.jobportal.service;

import com.example.jobportal.dto.JobRequestDto;
import com.example.jobportal.model.Job;
import com.example.jobportal.repository.JobRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String REDIS_KEY_PREFIX = "job:";

    public Job createJob(JobRequestDto dto) throws JsonProcessingException {
        Job job = new Job();
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setCompany(dto.getCompany());
        job.setLocation(dto.getLocation());
        job.setRequiredSkills(dto.getRequiredSkills());
        job.setSalary(dto.getSalary());

        Job savedJob = jobRepository.save(job);

        // Cache the job in Redis
        stringRedisTemplate.opsForValue().set(REDIS_KEY_PREFIX + savedJob.getId(), objectMapper.writeValueAsString(savedJob), 10, TimeUnit.MINUTES);

        return savedJob;
    }

    public List<Job> getJobsByLocationAndSkills(String location, String skills) {
        return jobRepository.findByLocationAndRequiredSkillsContaining(location, skills);
    }

    public Job getJobById(Long id) throws JsonProcessingException {
        // Try fetching from Redis first
        String jobJson = stringRedisTemplate.opsForValue().get(REDIS_KEY_PREFIX + id);
        if (jobJson != null) {
            return objectMapper.readValue(jobJson, Job.class);
        }

        // If not in Redis, fetch from database
        Optional<Job> optionalJob = jobRepository.findById(id);
        return optionalJob.orElse(null);
    }

    @Transactional
    public Job updateJob(Long id, JobRequestDto dto) throws JsonProcessingException {
        Optional<Job> optionalJob = jobRepository.findById(id);

        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();

            if (dto.getTitle() != null) {
                job.setTitle(dto.getTitle());
            }
            if (dto.getDescription() != null) {
                job.setDescription(dto.getDescription());
            }
            if (dto.getLocation() != null) {
                job.setLocation(dto.getLocation());
            }
            if (dto.getSalary() != 0) {
                job.setSalary(dto.getSalary());
            }
            if (dto.getCompany() != null) {
                job.setCompany(dto.getCompany());
            }
            if (dto.getRequiredSkills() != null) {
                job.setRequiredSkills(dto.getRequiredSkills());
            }

            Job updatedJob = jobRepository.save(job);

            // Update cache in Redis
            stringRedisTemplate.opsForValue().set(REDIS_KEY_PREFIX + updatedJob.getId(), objectMapper.writeValueAsString(updatedJob), 10, TimeUnit.MINUTES);

            return updatedJob;
        }

        return null;
    }

    @Transactional
    public boolean deleteJob(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);

        if (optionalJob.isPresent()) {
            jobRepository.deleteById(id);
            // Remove from Redis cache
            stringRedisTemplate.delete(REDIS_KEY_PREFIX + id);
            return true;
        }
        return false;
    }
}

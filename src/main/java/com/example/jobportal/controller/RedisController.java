package com.example.jobportal.controller;

import com.example.jobportal.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/save")
    public String saveToRedis(@RequestParam String key, @RequestParam String value) {
        redisService.saveToRedis(key, value);
        return "Data saved successfully!";
    }

    @GetMapping("/get")
    public String getFromRedis(@RequestParam String key) {
        return redisService.getFromRedis(key);
    }

    @DeleteMapping("/delete")
    public String deleteFromRedis(@RequestParam String key) {
        redisService.deleteFromRedis(key);
        return "Deleted Successfully!";
    }
}


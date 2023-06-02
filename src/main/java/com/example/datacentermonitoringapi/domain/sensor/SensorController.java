package com.example.datacentermonitoringapi.domain.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/sensor")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService sensorService;


    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(sensorService.findAllCategories());
    }

    @GetMapping("/categories/{category}")
    public ResponseEntity<List<SensorResponse>> getSensorsByCategory(@PathVariable("category") String category) {
        return ResponseEntity.ok(sensorService.findSensorsByCategory(category));
    }

    @GetMapping("/jwt/{id}")
    public ResponseEntity<String> getJwt(@PathVariable("id") long id) {
        return ResponseEntity.ok(sensorService.findJwtById(id));
    }

    @PostMapping
    public ResponseEntity<Long> newSensor(@RequestBody SensorRequest request) {
        return ResponseEntity.ok(sensorService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSensor(@PathVariable("id") long id,
                                             @RequestBody SensorRequest request) {
        sensorService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable("id") long id) {
        sensorService.delete(id);
        return ResponseEntity.ok().build();
    }
}

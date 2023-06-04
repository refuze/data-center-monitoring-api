package com.example.datacentermonitoringapi.domain.sensor;

import com.example.datacentermonitoringapi.component.EntityPatcher;
import com.example.datacentermonitoringapi.configuration.exception.HttpRuntimeException;
import com.example.datacentermonitoringapi.domain.data.Data;
import com.example.datacentermonitoringapi.domain.security.JwtService;
import com.example.datacentermonitoringapi.util.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;
    private final JwtService jwtService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public Sensor findById(long id) {
        return sensorRepository.findById(id).orElseThrow(() ->
                new HttpRuntimeException("There is no such sensor", HttpStatus.NOT_FOUND));
    }

    public Sensor findByUsername(String username) {
        long id = extractIdFromUsername(username);
        return findById(id);
    }

    @Transactional
    public void addData(Sensor sensor, Data data) {
        sensor.getData().add(data);
        sensorRepository.save(sensor);
    }

    @Transactional
    public long save(SensorRequest request) {
        Sensor sensor = SensorMapper.toEntity(request);
        Sensor saved = sensorRepository.save(sensor);
        saved.setJwtToken(jwtService.generateToken(saved));
        long id = sensorRepository.save(saved).getId();

        simpMessagingTemplate.convertAndSend("/topic/sensor/new/", id);

        return id;
    }

    @Transactional
    public void update(long id, SensorRequest request) {
        Sensor sensor = findById(id);
        Sensor patchedSensor = EntityPatcher.patch(sensor, request);
        long savedId = sensorRepository.save(patchedSensor).getId();

        simpMessagingTemplate.convertAndSend("/topic/sensor/update/", savedId);
    }

    public List<String> findAllCategories() {
        return sensorRepository.findAllCategories();
    }

    @Transactional
    public void delete(long id) {
        sensorRepository.deleteById(id);

        simpMessagingTemplate.convertAndSend("/topic/sensor/delete/", id);
    }

    public List<SensorResponse> findSensorsByCategory(String category) {
        return sensorRepository.findSensorsByCategory(category).stream().map(SensorMapper::toResponse).toList();
    }

    private long extractIdFromUsername(String username) {
        if (!username.startsWith("sensor_")) {
            throw new HttpRuntimeException("Incorrect username format", HttpStatus.BAD_REQUEST);
        }

        return Utils.extractIdFromUsername(username);
    }


    public String findJwtById(long id) {
        return sensorRepository.findJwtById(id);
    }
}

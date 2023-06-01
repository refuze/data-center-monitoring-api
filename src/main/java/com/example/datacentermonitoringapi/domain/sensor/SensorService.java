package com.example.datacentermonitoringapi.domain.sensor;

import com.example.datacentermonitoringapi.configuration.exception.HttpRuntimeException;
import com.example.datacentermonitoringapi.domain.data.Data;
import com.example.datacentermonitoringapi.component.EntityPatcher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;

    public Sensor findById(long id) {
        return sensorRepository.findById(id).orElseThrow(() ->
                new HttpRuntimeException("There is no such sensor", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void addData(Sensor sensor, Data data) {
        sensor.getData().add(data);
        sensorRepository.save(sensor);
    }

    @Transactional
    public long save(SensorRequest request) {
        return sensorRepository.save(SensorMapper.toEntity(request)).getId();
    }

    @Transactional
    public void update(long id, SensorRequest request) {
        Sensor sensor = findById(id);
        EntityPatcher<Sensor, SensorRequest> patcher = new EntityPatcher<>();
        Sensor patchedSensor = patcher.patch(sensor, request);
        sensorRepository.save(patchedSensor);
    }

    @Transactional
    public void setName(long id, String name) {
        sensorRepository.setNameById(name, id);
    }

    @Transactional
    public void setCategory(long id, String category) {
        sensorRepository.setCategoryById(category, id);
    }

    public List<String> findAllCategories() {
        return sensorRepository.findAllCategories();
    }

    @Transactional
    public void delete(long id) {
        sensorRepository.deleteById(id);
    }

    public List<SensorResponse> findSensorsByCategory(String category) {
        return sensorRepository.findSensorsByCategory(category).stream().map(SensorMapper::toResponse).toList();
    }
}

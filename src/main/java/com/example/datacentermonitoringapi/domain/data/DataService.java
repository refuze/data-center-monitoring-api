package com.example.datacentermonitoringapi.domain.data;

import com.example.datacentermonitoringapi.domain.email.EmailService;
import com.example.datacentermonitoringapi.domain.security.UserService;
import com.example.datacentermonitoringapi.domain.sensor.Sensor;
import com.example.datacentermonitoringapi.domain.sensor.SensorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {
    private final DataRepository dataRepository;
    private final SensorService sensorService;
    private final UserService userService;
    private final EmailService emailService;

    public List<DataResponse> findBySensorId(long sensorId) {
        return dataRepository.findBySensorId(sensorId).stream().map(DataMapper::toResponse).toList();
    }

    @Transactional
    public void addValue(long sensorId, double value) {
        Sensor sensor = sensorService.findById(sensorId);
        Data savedData = dataRepository.save(Data.builder()
                .value(value)
                .sensor(sensor)
                .date(LocalDateTime.now(ZoneId.of("Europe/Moscow")))
                .build());
        sensorService.addData(sensor, savedData);
        checkTemperature(savedData, sensor);
    }

    private void checkTemperature(Data data, Sensor sensor) {
        String subject;
        String text;

        if (data.getValue() > sensor.getMaxDataValue() ) {
            subject = "Высокие показатели на датчике: %s".formatted(sensor.getName());
            text = """
                    Высокие показатели (%.2f) на датчике:
                    id: %d
                    название: %s
                    """.formatted(data.getValue(), sensor.getId(), sensor.getName());
        } else if (data.getValue() < sensor.getMinDataValue()) {
            subject = "Низкие показатели на датчике: %s".formatted(sensor.getName());
            text = """
                    Низкие показатели (%.2f) на датчике:
                    id: %d
                    название: %s
                    """.formatted(data.getValue(), sensor.getId(), sensor.getName());
        } else {
            return;
        }

        List<String> emails = userService.getNotifiableEmails();
        for (String email : emails) {
            emailService.sendSimpleEmail(email, subject, text);
        }
    }


}

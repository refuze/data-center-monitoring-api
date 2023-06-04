package com.example.datacentermonitoringapi.domain.data;

import com.example.datacentermonitoringapi.domain.email.EmailService;
import com.example.datacentermonitoringapi.domain.security.UserService;
import com.example.datacentermonitoringapi.domain.sensor.Sensor;
import com.example.datacentermonitoringapi.domain.sensor.SensorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final SimpMessagingTemplate simpMessagingTemplate;

    public List<DataResponse> findBySensorId(long sensorId, Pageable pageable) {
        return dataRepository.findBySensorId(sensorId, pageable).stream().map(DataMapper::toResponse).toList();
    }

    @Transactional
    public void addValueAndNotify(double value) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Sensor sensor = sensorService.findByUsername(username);

        Data savedData = dataRepository.save(Data.builder()
                .value(value)
                .sensor(sensor)
                .date(LocalDateTime.now(ZoneId.of("Europe/Moscow")))
                .build());

        sensorService.addData(sensor, savedData);

        checkData(savedData, sensor);

        simpMessagingTemplate.convertAndSend("/topic/data/" + sensor.getId(), savedData.getValue());
    }

    private void checkData(Data data, Sensor sensor) {
        String subject;
        String text;

        if (data.getValue() > sensor.getMaxDataValue() ) {
            subject = "Высокие показатели на датчике: %s".formatted(sensor.getName());
            text = """
                    Высокие показатели (%.2f) на датчике:
                    id: %d
                    категория: %s
                    название: %s
                    """.formatted(data.getValue(), sensor.getId(), sensor.getCategory(), sensor.getName());
        } else if (data.getValue() < sensor.getMinDataValue()) {
            subject = "Низкие показатели на датчике: %s".formatted(sensor.getName());
            text = """
                    Низкие показатели (%.2f) на датчике:
                    id: %d
                    категория: %s
                    название: %s
                    """.formatted(data.getValue(), sensor.getId(), sensor.getCategory(), sensor.getName());
        } else {
            return;
        }

        simpMessagingTemplate.convertAndSend("/topic/data/error/", sensor.getId());

        List<String> emails = userService.getNotifiableEmails();
        for (String email : emails) {
            emailService.sendSimpleEmail(email, subject, text);
        }
    }

    public List<DataResponse> findErrorBySensorId(long sensorId, Pageable pageable) {
        return dataRepository.findErrorBySensorId(sensorId, pageable).stream()
                .map(DataMapper::toResponse).toList();
    }

    public List<DataResponse> findActualBySensorId(long sensorId) {
        return dataRepository.findActualBySensorId(
                sensorId,
                LocalDateTime.now(ZoneId.of("Europe/Moscow")).minusHours(1)
        ).stream().map(DataMapper::toResponse).toList();
    }
}

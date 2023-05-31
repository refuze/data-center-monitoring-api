package com.example.datacentermonitoringapi.domain.sensor;

public class SensorMapper {

    public static Sensor toEntity(SensorRequest request) {
        return Sensor.builder()
                .name(request.getName())
                .minDataValue(request.getMinDateValue())
                .maxDataValue(request.getMaxDateValue())
                .build();
    }

    public static SensorResponse toResponse(Sensor entity) {
        return SensorResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .minDataValue(entity.getMinDataValue())
                .maxDataValue(entity.getMaxDataValue())
                .build();
    }
}

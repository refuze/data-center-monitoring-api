package com.example.datacentermonitoringapi.domain.sensor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorResponse {

    private Long id;
    private String name;
    private double minDataValue;
    private double maxDataValue;

}

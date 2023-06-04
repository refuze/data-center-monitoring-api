package com.example.datacentermonitoringapi.domain.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorRequest {

    @JsonProperty("category")
    private String category;

    @JsonProperty("name")
    private String name;

    @JsonProperty("min_data_value")
    private double minDataValue;

    @JsonProperty("max_data_value")
    private double maxDataValue;

}

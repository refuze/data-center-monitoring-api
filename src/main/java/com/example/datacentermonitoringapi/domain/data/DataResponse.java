package com.example.datacentermonitoringapi.domain.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse {

    private double value;
    private LocalDateTime date;

}

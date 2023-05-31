package com.example.datacentermonitoringapi.domain.data;


public class DataMapper {

    public static DataResponse toResponse(Data entity) {
        return DataResponse.builder()
                .value(entity.getValue())
                .date(entity.getDate())
                .build();
    }

}

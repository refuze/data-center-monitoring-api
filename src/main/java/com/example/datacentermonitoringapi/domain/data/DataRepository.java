package com.example.datacentermonitoringapi.domain.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {

    @Query("""
            SELECT d
            FROM Data d
            WHERE d.sensor.id = :sensorId
            """)
    List<Data> findBySensorId(@Param("sensorId") long sensorId);
}

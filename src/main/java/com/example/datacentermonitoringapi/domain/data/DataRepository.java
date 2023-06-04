package com.example.datacentermonitoringapi.domain.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {

    @Query("""
            SELECT d
            FROM Data d
            WHERE d.sensor.id = :sensorId
            ORDER BY d.date DESC
            """)
    List<Data> findBySensorId(@Param("sensorId") long sensorId);

    @Query("""
            SELECT d
            FROM Data d
            WHERE d.sensor.id = :sensorId AND (d.value > d.sensor.maxDataValue OR d.value < d.sensor.minDataValue)
            ORDER BY d.date DESC
            """)
    List<Data> findErrorBySensorId(@Param("sensorId") long sensorId);

    @Query("""
            SELECT d
            FROM Data d
            WHERE d.date >= :date AND d.sensor.id = :sensorId
            ORDER BY d.date DESC
            """)
    List<Data> findActualBySensorId(@Param("sensorId") long sensorId,
                                    @Param("date") LocalDateTime date);
}

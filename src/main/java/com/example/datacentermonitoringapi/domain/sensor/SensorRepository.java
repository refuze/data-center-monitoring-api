package com.example.datacentermonitoringapi.domain.sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    @Query("""
            SELECT DISTINCT s.category
            FROM Sensor s
            ORDER BY s.category
            """)
    List<String> findAllCategories();

    @Query("""
            SELECT s
            FROM Sensor s
            WHERE s.category = :category
            ORDER BY s.name
            """)
    List<Sensor> findSensorsByCategory(@Param("category") String category);

    @Query("""
            SELECT s.jwtToken
            FROM Sensor s
            WHERE s.id = :id
            """)
    String findJwtById(@Param("id") long id);
}

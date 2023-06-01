package com.example.datacentermonitoringapi.domain.sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    @Modifying
    @Query("""
            UPDATE Sensor s
            SET s.name = :name
            WHERE s.id = :id
            """)
    void setNameById(@Param("name") String name,
                     @Param("id") long id);

    @Modifying
    @Query("""
            UPDATE Sensor s
            SET s.category = :category
            WHERE s.id = :id
            """)
    void setCategoryById(@Param("category") String category,
                         @Param("id") long id);

    @Query("""
            SELECT DISTINCT s.category
            FROM Sensor s
            """)
    List<String> findAllCategories();

    @Query("""
            SELECT s
            FROM Sensor s
            WHERE s.category = :category
            """)
    List<Sensor> findSensorsByCategory(@Param("category") String category);

    @Query("""
            SELECT s.jwtToken
            FROM Sensor s
            WHERE s.id = :id
            """)
    String findJwtById(@Param("id") long id);
}

package com.example.datacentermonitoringapi.domain.data;

import com.example.datacentermonitoringapi.domain.sensor.Sensor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "data", schema = "public")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Data {

    @Id
    @SequenceGenerator(
            allocationSize = 1,
            name = "data_seq",
            sequenceName = "data_seq"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "data_seq"
    )
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @Column(name = "value")
    private double value;

    @Column(name = "date")
    private LocalDateTime date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Data that = (Data) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

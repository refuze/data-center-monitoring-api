package com.example.datacentermonitoringapi.domain.sensor;

import com.example.datacentermonitoringapi.domain.data.Data;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "sensors", schema = "public")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sensor implements UserDetails {

    @Id
    @SequenceGenerator(
            allocationSize = 1,
            name = "sensor_seq",
            sequenceName = "sensor_seq"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sensor_seq"
    )
    private Long id;

    @OneToMany(mappedBy = "sensor", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Data> data = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "min_data_value")
    private double minDataValue;

    @Column(name = "max_data_value")
    private double maxDataValue;

    @Column(name = "jwt_token")
    private String jwtToken;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Sensor that = (Sensor) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of((GrantedAuthority) () -> "ROLE_SENSOR");
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "sensor_" + id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

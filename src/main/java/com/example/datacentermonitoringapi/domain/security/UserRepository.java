package com.example.datacentermonitoringapi.domain.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT u
            FROM User u
            WHERE u.role != 'ADMIN'
            """)
    List<User> findAllUsers();

    @Query("""
            SELECT u.email
            FROM User u
            WHERE u.isNotificationEnabled = true
            """)
    List<String> getNotifiableEmails();

    @Query("""
            SELECT u
            FROM User u
            WHERE u.email = :email
            """)
    Optional<User> findByEmail(@Param("email") String email);

    @Modifying
    @Query("""
            UPDATE User u
            SET u.email = :email
            WHERE u.id = :id
            """)
    void changeEmailById(@Param("email") String email,
                         @Param("id") long id);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.role = 'ADMIN'
            """)
    Optional<User> findAdmin();
}

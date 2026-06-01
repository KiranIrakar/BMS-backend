package com.BMS.BMS.repository;

import com.BMS.BMS.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByMobile_number(String mobile_number);

    Optional<User> findByEmailOrMobile_number(String email, String mobile_number);

    boolean existsByEmail(String email);

    boolean existsByMobile_number(String mobile_number);
}
package com.backend.importal.repository;

import com.backend.importal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPersonalEmail(String personalEmail);
    boolean existsByPersonalEmail(String personalEmail);


    long count();

    @Query("SELECT COUNT(u) FROM User u WHERE LOWER(u.role.masterRole.roleName) = 'student'")
    long countStudents();

    @Query("SELECT COUNT(u) FROM User u WHERE LOWER(u.role.masterRole.roleName) = 'lecturer'")
    long countLecturers();

    @Query("SELECT COUNT(u) FROM User u WHERE LOWER(u.role.masterRole.roleName) = 'junior staff'")
    long countJuniorStaff();


    //Find users by level
    List<User> findByLevel(String level);

    List<User> findByRole_RoleNameIgnoreCase(String roleName);
}
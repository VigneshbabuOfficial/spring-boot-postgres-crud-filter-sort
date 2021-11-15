package com.school.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.entities.Students;

@Repository
public interface StudentsRepository extends JpaRepository<Students, Long> {

}

package com.unistgympeople.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


@Component
public interface ExerciseRepository extends JpaRepository<Exercise, Long>{
}
